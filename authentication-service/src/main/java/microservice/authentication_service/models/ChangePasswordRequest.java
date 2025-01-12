package microservice.authentication_service.models;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ChangePasswordRequest {
    @Min(value = 5, message = "Password must be at least 5 characters")
    private String password;

    @Min(value = 5, message = "Password must be at least 5 characters")
    private String confirmPassword;
}
