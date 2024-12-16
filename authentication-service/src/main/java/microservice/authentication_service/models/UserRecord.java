package microservice.authentication_service.models;

import jakarta.validation.constraints.Email;

public record UserRecord(@Email(message = "INVALID_EMAIL") String email, String password) {

}

