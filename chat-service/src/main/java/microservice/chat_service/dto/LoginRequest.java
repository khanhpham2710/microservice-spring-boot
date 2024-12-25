package microservice.chat_service.dto;

import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {
    private String email;
    private String password;
}
