package microservice.chat_service.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import microservice.chat_service.config.FileUploadUtil;
import microservice.chat_service.dto.CloudinaryResponse;
import microservice.chat_service.dto.UpdateUserRequest;
import microservice.chat_service.model.User;
import microservice.chat_service.repository.UserRepository;
import microservice.chat_service.service.UserService;
import microservice.common_service.exception.NotFoundException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final CloudinaryService cloudinaryService;

    @Override
    public User findUserById(String id) throws NotFoundException {
        return userRepository.findById(id).orElseThrow(() -> new NotFoundException("The requested user is not found"));
    }

    @Override
    public List<User> searchUser(String query,String token) {
        return userRepository.findByEmailContaining(query);
    }


    @Override
    public User findUserProfile(String jwt) throws NotFoundException, BadCredentialsException {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        if (email == null) {
            throw new BadCredentialsException("Received invalid token...");
        }
        return userRepository.findByEmail(email);
    }

    @Transactional
    @Override
    public User updateUser(String userId, UpdateUserRequest req) throws NotFoundException {
        User user = findUserById(userId);

        if (!user.getProfile().isEmpty()) {
            if (StringUtils.isNotBlank(user.getProfileId())){
                cloudinaryService.delete(user.getProfileId());
            }
            MultipartFile file = req.getProfile();
            if (file != null) {
                FileUploadUtil.assertAllowed(file, FileUploadUtil.IMAGE_PATTERN);
                final String fileName = FileUploadUtil.getFileName(file.getOriginalFilename());
                CloudinaryResponse response = cloudinaryService.uploadFile(file, fileName);
                user.setProfile(response.getUrl());
                user.setProfileId(response.getPublicId());
            }
        }

        if (req.getName() != null) {
            user.setName(req.getName());
        }

        return this.userRepository.save(user);
    }
}
