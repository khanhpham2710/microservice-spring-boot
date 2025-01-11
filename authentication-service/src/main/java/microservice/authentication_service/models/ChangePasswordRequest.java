package microservice.authentication_service.models;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ChangePasswordRequest {
    @NotBlank(message = "Password must not be blank")
    private String password;

    @NotBlank(message = "Confirm password must not be blank")
    private String confirmPassword;
}
