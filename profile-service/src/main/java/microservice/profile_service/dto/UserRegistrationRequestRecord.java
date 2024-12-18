package microservice.profile_service.dto;

import java.time.LocalDate;

public record UserRegistrationRequestRecord(
        String id,

        String name,

        LocalDate dob,

        String gender,

        String language,

        String countryIso2
) {
}

