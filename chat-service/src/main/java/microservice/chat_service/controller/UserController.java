package microservice.chat_service.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import microservice.chat_service.dto.UpdateUserRequest;
import microservice.chat_service.model.User;
import microservice.chat_service.service.UserService;
import microservice.common_service.exception.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    @GetMapping("/profile")
    public ResponseEntity<User> getUserProfileHandler(@RequestHeader("Authorization") String token)
            throws NotFoundException {
        User user = userService.findUserProfile(token);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping("/{query}")
    public ResponseEntity<List<User>> searchUserHandler(@PathVariable("query") String query, @RequestHeader("Authorization") String token) {
        List<User> users = userService.searchUser(query,token);
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @PutMapping("/update")
    public ResponseEntity<User> updateUserHandler(@RequestParam(value = "name", required = false) String name,
                                                  @RequestParam(value = "profile", required = false) MultipartFile profile,
                                                  @RequestHeader("Authorization") String token) throws NotFoundException {
        User user = userService.findUserProfile(token);
        UpdateUserRequest request = new UpdateUserRequest(name, profile);
        User updatedUser = userService.updateUser(user.getId(), request);

        return new ResponseEntity<>(updatedUser, HttpStatus.ACCEPTED);
    }
}
