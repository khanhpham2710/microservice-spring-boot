package microservice.authentication_service.service;

import microservice.authentication_service.models.Role;

public interface RoleService {
    void assignRole(String userId , Role roleName);
    void deleteRoleFromUser(String userId ,Role roleName);
}

