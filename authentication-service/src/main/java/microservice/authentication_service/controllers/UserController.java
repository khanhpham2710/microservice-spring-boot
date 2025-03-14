package microservice.authentication_service.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.validation.Valid;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;
import microservice.authentication_service.models.*;
import microservice.authentication_service.response.LoginResponse;
import microservice.authentication_service.service.RoleService;
import microservice.authentication_service.service.UserService;
import microservice.common_service.exception.AppException;
import microservice.common_service.exception.ErrorCode;
import microservice.common_service.response.BaseResponse;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.representations.idm.UserRepresentation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping
@RequiredArgsConstructor
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<String> createUser(@Valid @RequestBody UserRecord userRecord) {
        userService.createUser(userRecord);
        return ResponseEntity.status(HttpStatus.CREATED).body("Check your email");
    }

//    @PostAuthorize("returnValue.id == #userId")
    @GetMapping("/{userId}")
    public UserRepresentation getUserById(@PathVariable String userId) {
        try{
            return userService.getUserById(userId).toRepresentation();
        }catch (Exception exception){
            if (exception instanceof NotFoundException){
                throw new AppException(ErrorCode.USER_NOT_EXISTED);
            } else {
                throw exception;
            }
        }
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        LoginResponse response = userService.login(request);
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(response);
    }

    @PostMapping("/refresh")
    public ResponseEntity<String> refreshToken(@RequestParam String token) throws JsonProcessingException {
        String user = userService.refreshToken(token);
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(user);
    }

    @PutMapping("/verify-email/{id}")
    public ResponseEntity<String> sendVerificationEmail(@PathVariable String id) {
        String email = userService.sendVerificationEmail(id);
        return ResponseEntity.status(HttpStatus.OK).body("Verify email have been sent to " + email);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable String id) {
        userService.deleteUser(id);
        BaseResponse<String> response = BaseResponse.<String>builder()
                .message("User deleted successfully")
                .data("")
                .build();
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
    }

    @PutMapping("/update-name/{userId}")
    public ResponseEntity<String> updateUsername(@Valid @RequestBody UpdateUserRecord request, @PathVariable String userId) {
        userService.updateUser(request, userId);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body("Update first name and last name successfully");
    }

    @PutMapping("/change-password/{userId}")
    public ResponseEntity<?> changePassword(@Valid @RequestBody ChangePasswordRequest request, @PathVariable String userId) {
        userService.changePassword(userId, request);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body("Update password successfully");
    }


    @PutMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestParam String email) {
        userService.forgotPassword(email);
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    @GetMapping("/roles/{id}")
    public ResponseEntity<?> getUserRoles(@PathVariable String id) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.getUserRoles(id));
    }

    @GetMapping("/groups/{id}")
    public ResponseEntity<?> getUserGroups(@PathVariable String id) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.getUserGroups(id));
    }

    @GetMapping("/hello")
//    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> test() {
        return ResponseEntity.status(HttpStatus.OK).body("Heloo");
    }
}