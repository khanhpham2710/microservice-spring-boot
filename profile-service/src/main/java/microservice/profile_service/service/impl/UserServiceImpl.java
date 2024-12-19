package microservice.profile_service.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import microservice.common_service.exception.AppException;
import microservice.common_service.exception.ErrorCode;
import microservice.common_service.exception.NotFoundException;
import microservice.profile_service.dto.KeycloakUser;
import microservice.profile_service.model.User;
import microservice.profile_service.proxy.AuthProxy;
import microservice.profile_service.repository.UserRepository;
import microservice.profile_service.service.UserService;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


@Slf4j
@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final AuthProxy authProxy;

    @PostAuthorize("hasRole('USER')")
    @Override
    public User save(User user) {
        if (userRepository.existsById(user.getId())){
            throw new AppException(ErrorCode.USER_EXISTED);
        }
        User newUser = userRepository.save(user);
        KeycloakUser keycloakUser = authProxy.getUserById(newUser.getId());
        newUser.setFirstName(keycloakUser.getFirstName());
        newUser.setLastName(keycloakUser.getLastName());
        newUser.setEmail(keycloakUser.getEmail());
        newUser.setId(keycloakUser.getId());
        return userRepository.save(user);
    }

    @PostAuthorize("hasRole('USER')")
    @Override
    public User update(User user, String userId) {
        User existingUser = getById(userId);
        existingUser.setDob(user.getDob());
        existingUser.setCountryIso2(user.getCountryIso2());
        existingUser.setLanguage(user.getLanguage());
        existingUser.setGender(user.getGender());
        existingUser.setAddress(user.getAddress());

//        existingUser.setFirstName(user.getFirstName());
//        existingUser.setLastName(user.getLastName());

        return userRepository.save(existingUser);
    }

    @PostAuthorize("hasRole('USER')")
    @Override
    public User getById(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(()->new NotFoundException("User does not exist for id "+ userId));
        if (!userId.equals(SecurityContextHolder.getContext().getAuthentication().getName())){
            throw new AccessDeniedException("You don't have permission to access this user");
        }
        return user;
    }

    @Override
    public void deleteUserById(String userId) {
        getById(userId);
        userRepository.deleteById(userId);
    }

    @Override
    public void uploadProfilePicture(String userId, String key, MultipartFile file) throws Exception {
        User user = getById(userId);
//        String savedKey =bunnyNetService.uploadProfilePicture(file,null,key);
//        user.setStorageProvider(storageProvider);
//        user.setStorageId(savedKey);
        userRepository.save(user);


    }
}
