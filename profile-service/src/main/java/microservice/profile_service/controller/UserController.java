package microservice.profile_service.controller;

import lombok.RequiredArgsConstructor;
import microservice.profile_service.dto.UserCreationRequest;
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

    @PostMapping("/create/{id}")
    public ResponseEntity<UserDTO>  createUser(@PathVariable String id, @RequestBody UserCreationRequest request){
        User newUser = userService.save(id,request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(mapper.map(newUser));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO>  getUserById(@PathVariable String id){
        return ResponseEntity.ok(mapper.map(userService.getById(id)));
    }

    @DeleteMapping("/delete/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void  deleteUserById(@PathVariable String id){
        userService.deleteUserById(id);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<UserDTO>  updateUser(@PathVariable String id,@RequestBody UserCreationRequest request){
        User updatedUser = userService.update(id,request);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(mapper.map(updatedUser));
    }

    @PutMapping(value = "/{id}/upload-profile-picture",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UserDTO>  uploadProfileImage(@RequestParam(value = "file") MultipartFile file, @PathVariable String id
            , @RequestParam String key) throws Exception {

        userService.uploadProfilePicture(id,key,file);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

}


