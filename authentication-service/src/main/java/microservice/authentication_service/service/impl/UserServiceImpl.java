package microservice.authentication_service.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.NotAuthorizedException;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import microservice.authentication_service.models.*;
import microservice.authentication_service.response.LoginResponse;
import microservice.authentication_service.service.UserService;
import microservice.common_service.exception.AppException;
import microservice.common_service.exception.ErrorCode;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RolesResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.AccessTokenResponse;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.GroupRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    @Value("${app.keycloak.realm}")
    private String realm;
    private final Keycloak keycloak;

    @Value("${app.keycloak.admin.clientId}")
    private String clientId;

    @Value("${app.keycloak.admin.clientSecret}")
    private String clientSecret;

    @Value("${app.keycloak.serverUrl}")
    private String serverUrl;


    @Override
    public UserRepresentation createUser(UserRecord userRecord) {
        UserRepresentation userRepresentation = getUserRepresentation(userRecord);
        UsersResource usersResource = getUsersResource();

        List<UserRepresentation> userRepresentations = usersResource.searchByUsername(userRecord.email(), true);

        if (!userRepresentations.isEmpty()) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }

        try {
            usersResource.create(userRepresentation);
        } catch (Exception e) {
            throw new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION);
        }

        userRepresentations = usersResource.searchByUsername(userRecord.email(), true);
        UserRepresentation userRepresentation1 = userRepresentations.getFirst();

        RolesResource rolesResource = keycloak.realm(realm).roles();
        RoleRepresentation representation = rolesResource.get(Role.USER.toString()).toRepresentation();

        UserResource user = getUserById(userRepresentation1.getId());
        user.roles().realmLevel().add(Collections.singletonList(representation));

        sendVerificationEmail(userRepresentation1.getId());
        return userRepresentation1;
    }

    private UserRepresentation getUserRepresentation(UserRecord userRecord) {
        UserRepresentation userRepresentation = new UserRepresentation();
        userRepresentation.setEnabled(true);
        userRepresentation.setUsername(userRecord.userName());
        userRepresentation.setEmail(userRecord.email());
        userRepresentation.setEmailVerified(false);
        userRepresentation.setFirstName(userRecord.firstName());
        userRepresentation.setLastName(userRecord.lastName());

        CredentialRepresentation credentialRepresentation = new CredentialRepresentation();
        credentialRepresentation.setValue(userRecord.password());
        credentialRepresentation.setType(CredentialRepresentation.PASSWORD);

        userRepresentation.setCredentials(List.of(credentialRepresentation));
        return userRepresentation;
    }

    @Override
    public String sendVerificationEmail(String userId) {
        UsersResource usersResource = getUsersResource();
        try {
            UserResource userResource = usersResource.get(userId);

            userResource.sendVerifyEmail();
            UserRepresentation user = userResource.toRepresentation();

            return user.getEmail();
        } catch (Exception e) {
            if (e instanceof NotFoundException){
                throw new AppException(ErrorCode.USER_NOT_EXISTED);
            }
            throw new AppException(ErrorCode.EMAIL_SEND_FAILED);
        }
    }

    @Override
    public UserRepresentation updateUser(UpdateUserRecord request, String userId) {
        UsersResource userResource = getUsersResource();

        UserRepresentation existingUser = userResource.get(userId).toRepresentation();
        if (existingUser == null) {
            throw new AppException(ErrorCode.USER_NOT_EXISTED);
        }


        existingUser.setUsername(request.userName());
        existingUser.setFirstName(request.firstName());
        existingUser.setLastName(request.lastName());
        userResource.get(userId).update(existingUser);

        return existingUser;
    }

    @Override
    public void changePassword(String userId, ChangePasswordRequest request) {
        UsersResource userResource = getUsersResource();
        String password= request.getPassword();

        if (!Objects.equals(password, request.getConfirmPassword())){
            throw new AppException(ErrorCode.PASSWORD_NOT_EQUAL);
        }

        UserRepresentation existingUser = userResource.get(userId).toRepresentation();
        CredentialRepresentation newCredential = new CredentialRepresentation();
        newCredential.setType(CredentialRepresentation.PASSWORD);
        newCredential.setValue(password);
        existingUser.setCredentials(Collections.singletonList(newCredential));

        userResource.get(userId).update(existingUser);
    }

    @Override
    public void deleteUser(String userId) {
        UsersResource usersResource = getUsersResource();
        try {
            usersResource.delete(userId);
        } catch (Exception e) {
            throw new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION);
        }
    }

    @Override
    public void forgotPassword(String username) {
        UsersResource usersResource = getUsersResource();
        List<UserRepresentation> userRepresentations = usersResource.searchByUsername(username, true);
        if (userRepresentations.isEmpty()) {
            throw new AppException(ErrorCode.USER_NOT_EXISTED);
        }

        UserRepresentation userRepresentation = userRepresentations.getFirst();
        UserResource userResource = usersResource.get(userRepresentation.getId());
        try {
            userResource.executeActionsEmail(List.of("UPDATE_PASSWORD"));
        } catch (Exception e) {
            throw new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION);
        }
    }

    @Override
    public LoginResponse login(LoginRequest request) {
        UserRepresentation userRepresentation = getUser(request.emailOrUsername());

        Keycloak keycloakClient = KeycloakBuilder.builder()
                .serverUrl(serverUrl)
                .realm(realm)
                .clientId(clientId)
                .clientSecret(clientSecret)
                .grantType(OAuth2Constants.PASSWORD)
                .username(userRepresentation.getUsername())
                .password(request.password())
                .build();
        try {
            AccessTokenResponse tokenSet = keycloakClient.tokenManager().grantToken();
            String id = userRepresentation.getId();
            return new LoginResponse(id, tokenSet.getToken(), tokenSet.getRefreshToken());
        } catch (Exception exception) {
            if (exception instanceof BadRequestException){
                throw new AppException(ErrorCode.NOT_VERIFY_EMAIL);
            } else if (exception instanceof NotAuthorizedException){
                throw new AppException(ErrorCode.WRONG_AUTHENTICATION);
            } else {
                exception.printStackTrace();
            }
        }
        return null;
    }


    public String refreshToken(String refreshToken) throws JsonProcessingException {
        RestTemplate restTemplate = new RestTemplate();

        String KEYCLOAK_URL = serverUrl + "/realms/" + realm + "/protocol/openid-connect/token";

        String requestBody = "client_id=" + clientId +
                "&client_secret=" + clientSecret +
                "&grant_type=refresh_token" +
                "&refresh_token=" + refreshToken;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    KEYCLOAK_URL,
                    HttpMethod.POST,
                    entity,
                    String.class
            );

            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            return objectMapper.readValue(response.getBody(), KeyCloakResponse.class).getAccessToken();

        } catch (HttpClientErrorException httpClientErrorException) {
            String responseBody = httpClientErrorException.getResponseBodyAsString();
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(responseBody);

            String errorDescription = jsonNode.get("error_description").asText();

            if (Objects.equals(errorDescription, "Invalid refresh token")) {
                throw new AppException(ErrorCode.INVALID_REFRESH_TOKEN);
            } else if (Objects.equals(errorDescription, "Token is not active")) {
                throw new AppException(ErrorCode.INACTIVE_REFRESH_TOKEN);
            } else {
                return errorDescription;
            }
        } catch (Exception ex) {
            throw new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION);
        }
    }

    @PreAuthorize("hasRole('USER')")
    @Override
    public UserResource getUserById(String userId) {
        boolean isAdmin = SecurityContextHolder.getContext().getAuthentication().getAuthorities().toString().contains("ROLE_ADMIN");
        boolean checkUser = userId.equals(SecurityContextHolder.getContext().getAuthentication().getName());

        if (!isAdmin && !checkUser) {
            throw new AccessDeniedException("You don't have permission to access this user");
        }

        UsersResource usersResource = getUsersResource();
        return usersResource.get(userId);
    }


    @Override
    public UserRepresentation getUser(String username) {
        UsersResource usersResource = getUsersResource();
        List<UserRepresentation> usersUsername = usersResource.searchByUsername(username, true);
        List<UserRepresentation> usersEmail = usersResource.searchByEmail(username, true);

        if (usersUsername.isEmpty() && usersEmail.isEmpty()) {
            throw new AppException(ErrorCode.USER_NOT_EXISTED);
        }

        if (usersUsername.isEmpty()){
           return usersEmail.getFirst();
        } else {
            return usersUsername.getFirst();
        }
    }

    @Override
    public List<RoleRepresentation> getUserRoles(String userId) {
        return getUserById(userId).roles().realmLevel().listAll();
    }

    @Override
    public List<GroupRepresentation> getUserGroups(String userId) {
        return getUserById(userId).groups();
    }


    private UsersResource getUsersResource() {
        return keycloak.realm(realm).users();
    }
}