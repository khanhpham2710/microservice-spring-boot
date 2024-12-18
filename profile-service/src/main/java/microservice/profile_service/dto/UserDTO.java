package microservice.profile_service.dto;

import lombok.*;
import microservice.profile_service.model.enums.Gender;
import microservice.profile_service.model.enums.Role;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDTO {
    private String id;
    private String firstName;
    private String lastName;
    private String language;
    private String email;
    private Role role;
    private LocalDate dob;
    private String countryIso2;
    private Gender gender;

//    private StorageProvider storageProvider;
}


