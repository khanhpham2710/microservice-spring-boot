package microservice.authentication_service.service;

import microservice.authentication_service.models.UserRecord;
import microservice.authentication_service.response.LoginResponse;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.representations.idm.GroupRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;

import java.util.List;

public interface UserService {

    UserRepresentation createUser(UserRecord user);
    void sendVerificationEmail(String userId);
    void deleteUser(String userId);
    void forgotPassword(String username);
    LoginResponse login(UserRecord user);
    String refreshToken(String token);
    UserResource getUserById(String userId);
    UserRepresentation getUserByUsername(String username);
    List<RoleRepresentation> getUserRoles(String userId);
    List<GroupRepresentation> getUserGroups(String userId);
}

