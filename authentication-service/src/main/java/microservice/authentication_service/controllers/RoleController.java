package microservice.authentication_service.controllers;

import lombok.RequiredArgsConstructor;
import microservice.authentication_service.models.Role;
import microservice.authentication_service.service.RoleService;
import microservice.common_service.validation.EnumPattern;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/role")
@RequiredArgsConstructor
@Validated
public class RoleController {
    private final RoleService roleService;

//    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/assign/{userId}")
    public ResponseEntity<?> assignRole(@PathVariable String userId,
                                        @RequestParam @EnumPattern(name = "", regexp = "USER|ADMIN|user|admin") String role) {
        Role roleName = Role.valueOf(role.toUpperCase());
        roleService.assignRole(userId, roleName);
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

//    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/remove/{userId}")
    public ResponseEntity<?> unAssignRole(@PathVariable String userId,
                                          @RequestParam @EnumPattern(name = "", regexp = "USER|ADMIN|user|admin") String role) {
        Role roleName = Role.valueOf(role.toUpperCase());
        roleService.deleteRoleFromUser(userId, roleName);
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

}


