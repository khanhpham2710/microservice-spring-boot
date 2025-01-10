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
public class UserCreationRequest {
    private String language;
    private Address address;
    private LocalDate dob;
    private Gender gender;
//    private StorageProvider storageProvider;
}
