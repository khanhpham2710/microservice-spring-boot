package microservice.profile_service.controller;

import lombok.RequiredArgsConstructor;
import microservice.profile_service.dto.UserDTO;
import microservice.profile_service.mapper.DtoMapper;
import microservice.profile_service.model.User;
import microservice.profile_service.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@RestController
@RequestMapping
public class UserController{
    private  final UserService userService;
    private  final DtoMapper mapper;

    @PostMapping("/users")
    public ResponseEntity<UserDTO>  saveUser(@RequestBody UserDTO user){
        userService.save(mapper.map(user));
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO>  getUserById(@PathVariable String id){

        return ResponseEntity.ok(mapper.map(userService.getById(id)));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void  deleteUserById(@PathVariable String id){
        userService.deleteUserById(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDTO>  updateUser(@PathVariable String id,@RequestBody UserDTO user){
        User updatedUser = userService.update(mapper.map(user),id);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(mapper.map(updatedUser));
    }

    @PutMapping(value = "/{id}/upload-profile-picture",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UserDTO>  uploadProfileImage(@RequestParam(value = "file") MultipartFile file, @PathVariable String id
            , @RequestParam String key) throws Exception {

        userService.uploadProfilePicture(id,key,file);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

}


