package microservice.authentication_service.models;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record UserRecord(
        @NotBlank String userName,
        @Email(message = "INVALID_EMAIL") String email,
        @Min(value = 5, message = "Password must be at least 5 characters") String password,
        @NotBlank String firstName,
        @NotBlank String lastName) {
}

