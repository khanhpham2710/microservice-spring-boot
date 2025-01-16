package microservice.authentication_service.models;

import jakarta.validation.constraints.Email;

public record LoginRequest(@Email String emailOrUsername, String password) {
}
