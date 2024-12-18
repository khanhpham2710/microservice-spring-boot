package microservice.profile_service.service;

import microservice.profile_service.model.User;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {
    User save(User user);
    User update(User user, String userId);
    User getById(String userId);
    void deleteUserById(String userId);
    void uploadProfilePicture(String userId, String key, MultipartFile file) throws Exception;
}

