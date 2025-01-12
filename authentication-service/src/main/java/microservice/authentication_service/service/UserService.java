package microservice.authentication_service.service;

import microservice.authentication_service.models.ChangePasswordRequest;
import microservice.authentication_service.models.LoginRequest;
import microservice.authentication_service.models.UpdateUserRecord;
import microservice.authentication_service.models.UserRecord;
import microservice.authentication_service.response.LoginResponse;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.representations.idm.GroupRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;

import java.util.List;

public interface UserService {

    UserRepresentation createUser(UserRecord user);
    String sendVerificationEmail(String userId);
    UserRepresentation updateUser(UpdateUserRecord request, String userId);
    void deleteUser(String userId);
    void changePassword(String userId, ChangePasswordRequest request);
    void forgotPassword(String username);
    LoginResponse login(LoginRequest request);
    String refreshToken(String token);
    UserResource getUserById(String userId);
    UserRepresentation getUserByUsername(String username);
    List<RoleRepresentation> getUserRoles(String userId);
    List<GroupRepresentation> getUserGroups(String userId);
}

