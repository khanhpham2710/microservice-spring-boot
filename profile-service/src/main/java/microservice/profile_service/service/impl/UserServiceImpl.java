package microservice.profile_service.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import microservice.common_service.exception.AppException;
import microservice.common_service.exception.ErrorCode;
import microservice.common_service.exception.NotFoundException;
import microservice.profile_service.dto.KeycloakUser;
import microservice.profile_service.dto.UserCreationRequest;
import microservice.profile_service.model.Address;
import microservice.profile_service.model.UpdateUserRecord;
import microservice.profile_service.model.User;
import microservice.profile_service.proxy.AuthProxy;
import microservice.profile_service.repository.AddressRepository;
import microservice.profile_service.repository.UserRepository;
import microservice.profile_service.service.UserService;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashSet;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final AddressRepository addressRepository;
    private final AuthProxy authProxy;

    @PreAuthorize("hasRole('USER')")
    @Override
    public User save(String userId, UserCreationRequest request) {
        if (userRepository.existsById(userId)){
            throw new AppException(ErrorCode.USER_EXISTED);
        }
        KeycloakUser keycloakUser = authProxy.getUserById(userId);

        User newUser = User.builder()
                .userName(keycloakUser.getUserName())
                .firstName(keycloakUser.getFirstName())
                .lastName(keycloakUser.getLastName())
                .id(userId)
                .email(keycloakUser.getEmail())
                .dob(request.getDob())
                .gender(request.getGender())
                .language(request.getLanguage())
                .address(request.getAddress())
                .followers(new HashSet<>())
                .following(new HashSet<>())
                .friends(new HashSet<>())
                .build();

        return userRepository.save(newUser);
    }

    @PreAuthorize("hasRole('USER')")
    @Override
    public User update(String userId, UserCreationRequest request) {
        User existingUser = getById(userId);
        UpdateUserRecord record = new UpdateUserRecord(
                request.getUserName(),
                request.getFirstName(),
                request.getLastName());
        authProxy.updateUser(record,userId);

        existingUser.setFirstName(request.getFirstName());
        existingUser.setLastName(request.getLastName());
        existingUser.setDob(request.getDob());
        existingUser.setLanguage(request.getLanguage());
        existingUser.setGender(request.getGender());

        Address newAddress = request.getAddress();

        Address address = addressRepository.findById(existingUser.getAddress().getId())
                .orElseThrow(() ->new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION));
        address.setStreet(newAddress.getStreet());
        address.setHouseNumber(newAddress.getHouseNumber());
        address.setZipCode(newAddress.getZipCode());
        addressRepository.save(address);

        existingUser.setAddress(address);

        return userRepository.save(existingUser);
    }

    @PreAuthorize("hasRole('USER')")
    @Override
    public User getById(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(()->new NotFoundException("User does not exist for id "+ userId));
        if (!userId.equals(SecurityContextHolder.getContext().getAuthentication().getName())){
            throw new AccessDeniedException("You don't have permission to access this user");
        }
        return user;
    }

    @PreAuthorize("hasRole('USER')")
    @Override
    @Transactional
    public void deleteUserById(String userId) {
        User user = getById(userId);

        if (user.getAddress() != null) {
            addressRepository.delete(user.getAddress());
        }

        authProxy.deleteUserById(userId);
        userRepository.deleteById(userId);
    }

    @PreAuthorize("hasRole('USER')")
    @Override
    public void uploadProfilePicture(String userId, String key, MultipartFile file) throws Exception {
        User user = getById(userId);
//        String savedKey =bunnyNetService.uploadProfilePicture(file,null,key);
//        user.setStorageProvider(storageProvider);
//        user.setStorageId(savedKey);
        userRepository.save(user);
    }
}
