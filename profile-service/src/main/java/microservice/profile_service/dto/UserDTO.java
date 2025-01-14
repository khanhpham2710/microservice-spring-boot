package microservice.profile_service.dto;

import lombok.*;
import microservice.profile_service.model.Address;
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
    private String language;
    private String email;
    private Address address;
    private String firstName;
    private String lastName;
    private Role role;
    private LocalDate dob;
    private String countryIso2;
    private Gender gender;

//    private StorageProvider storageProvider;
}


