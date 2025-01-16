package microservice.profile_service.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class KeycloakUser {
    protected String id;
    protected String origin;
    protected String userName;
    protected String email;
    protected Boolean enabled;
    protected Boolean emailVerified;
    protected String firstName;
    protected String lastName;
}

