package microservice.profile_service.service;

import microservice.profile_service.dto.UserCreationRequest;
import microservice.profile_service.model.User;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {
    User save(String userId, UserCreationRequest request);
    User update(String userId,UserCreationRequest request);
    User getById(String userId);
    void deleteUserById(String userId);
    void uploadProfilePicture(String userId, String key, MultipartFile file) throws Exception;
}

