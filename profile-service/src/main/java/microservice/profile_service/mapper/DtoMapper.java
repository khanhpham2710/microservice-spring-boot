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
                .countryIso2(userDTO.getCountryIso2())
                .role(userDTO.getRole())
                .firstName(userDTO.getFirstName())
                .lastName(userDTO.getLastName())
                .language(userDTO.getLanguage())
                .gender(userDTO.getGender())
                .email(userDTO.getEmail())
                .build();
    };
    public UserDTO map(User user){
        return UserDTO.builder()
                .id(user.getId())
                .dob(user.getDob())
                .countryIso2(user.getCountryIso2())
                .role(user.getRole())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .language(user.getLanguage())
                .gender(user.getGender())
                .email(user.getEmail())
                .build();
    };
}
