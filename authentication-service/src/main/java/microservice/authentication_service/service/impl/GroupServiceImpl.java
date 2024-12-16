package microservice.authentication_service.service.impl;

import lombok.RequiredArgsConstructor;
import microservice.authentication_service.service.GroupService;
import microservice.authentication_service.service.UserService;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.UserResource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class GroupServiceImpl implements GroupService {
    private final Keycloak keycloak;
    private final UserService userService;

    @Value("${app.keycloak.realm}")
    private String realm;

    @Override
    public void assignGroup(String userId, String groupId) {
        UserResource user = userService.getUserById(userId);
        user.joinGroup(groupId);
    }

    @Override
    public void deleteGroupFromUser(String userId, String groupId) {
        UserResource user = userService.getUserById(userId);
        user.leaveGroup(groupId);
    }
}


