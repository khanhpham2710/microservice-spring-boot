package microservice.chat_service.service;

import microservice.chat_service.dto.UpdateUserRequest;
import microservice.chat_service.model.User;
import microservice.common_service.exception.NotFoundException;
import org.springframework.security.authentication.BadCredentialsException;

import java.util.List;

public interface UserService {
    public User findUserById(String id) throws NotFoundException;

    public List<User> searchUser(String query,String token);

    public User findUserProfile(String jwt) throws NotFoundException, BadCredentialsException;

    public User updateUser(String userId, UpdateUserRequest req) throws NotFoundException;
}
