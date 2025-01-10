package microservice.authentication_service.service.impl;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.ws.rs.NotAuthorizedException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import microservice.authentication_service.models.KeyCloakResponse;
import microservice.authentication_service.models.UserRecord;
import microservice.authentication_service.response.LoginResponse;
import microservice.authentication_service.service.UserService;
import microservice.common_service.exception.AppException;
import microservice.common_service.exception.ErrorCode;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.AccessTokenResponse;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.GroupRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;

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
        sendVerificationEmail(userRepresentation1.getId());
        return userRepresentation1;
    }

    private UserRepresentation getUserRepresentation(UserRecord userRecord) {
        UserRepresentation userRepresentation = new UserRepresentation();
        userRepresentation.setEnabled(true);
        userRepresentation.setUsername(userRecord.email());
        userRepresentation.setEmail(userRecord.email());
        userRepresentation.setEmailVerified(false);

        CredentialRepresentation credentialRepresentation = new CredentialRepresentation();
        credentialRepresentation.setValue(userRecord.password());
        credentialRepresentation.setType(CredentialRepresentation.PASSWORD);

        userRepresentation.setCredentials(List.of(credentialRepresentation));
        return userRepresentation;
    }

    @Override
    public void sendVerificationEmail(String userId) {
        UsersResource usersResource = getUsersResource();
        try {
            usersResource.get(userId).sendVerifyEmail();
        } catch (Exception e) {
            throw new AppException(ErrorCode.EMAIL_SEND_FAILED);
        }
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
    public LoginResponse login(UserRecord userRecord) {
        UserRepresentation userRepresentation = getUserByUsername(userRecord.email());
        Keycloak keycloakClient = KeycloakBuilder.builder()
                .serverUrl(serverUrl)
                .realm(realm)
                .clientId(clientId)
                .clientSecret(clientSecret)
                .grantType(OAuth2Constants.PASSWORD)
                .username(userRecord.email())
                .password(userRecord.password())
                .build();
        try {
            AccessTokenResponse tokenSet = keycloakClient.tokenManager().grantToken();
            String id = userRepresentation.getId();
            return new LoginResponse(id, tokenSet.getToken(), tokenSet.getRefreshToken());
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return null;
    }


    public String refreshToken(String refreshToken) {
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

            if (response.getStatusCode() == HttpStatus.OK) {
                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                return objectMapper.readValue(response.getBody(), KeyCloakResponse.class).getAccessToken();
//                JsonNode jsonNode = objectMapper.readTree(response.getBody());
//                return jsonNode.get("access_token").asText();
            } else {
                throw new RuntimeException("Failed to refresh token. HTTP Status: " + response.getStatusCode());
            }
        } catch (Exception e) {
            throw new RuntimeException("Error occurred while refreshing token: " + e.getMessage(), e);
        }
    }

    @Override
    public UserResource getUserById(String userId) {
        UsersResource usersResource = getUsersResource();
        return usersResource.get(userId);
    }


    @Override
    public UserRepresentation getUserByUsername(String username) {
        UsersResource usersResource = getUsersResource();
        List<UserRepresentation> userRepresentations = usersResource.searchByUsername(username, true);

        if (userRepresentations.isEmpty()) {
            throw new AppException(ErrorCode.USER_NOT_EXISTED);
        }

        return userRepresentations.getFirst();
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