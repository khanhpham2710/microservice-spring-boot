package microservice.profile_service.mapper;

import microservice.profile_service.dto.UserDTO;
import microservice.profile_service.model.User;
import org.springframework.stereotype.Component;

@Component
public class DtoMapper {
    public User map(UserDTO userDTO){
        return User.builder()
                .id(userDTO.getId())
                .dob(userDTO.getDob())
                .role(userDTO.getRole())
                .language(userDTO.getLanguage())
                .address(userDTO.getAddress())
                .gender(userDTO.getGender())
                .email(userDTO.getEmail())
                .build();
    };
    public UserDTO map(User user){
        if (user == null) {
            return null;
        }

        return UserDTO.builder()
                .id(user.getId())
                .dob(user.getDob())
                .role(user.getRole())
                .language(user.getLanguage())
                .address(user.getAddress())
                .gender(user.getGender())
                .email(user.getEmail())
                .build();
    };
}
