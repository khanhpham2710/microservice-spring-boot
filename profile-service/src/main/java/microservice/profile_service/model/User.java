package microservice.profile_service.model;

import lombok.*;
import microservice.profile_service.model.enums.Gender;
import microservice.profile_service.model.enums.Role;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Node
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {
    @Id
    private String id;

    private String firstName;

    private String lastName;

    private String email;

    private Address address;

    private Role role;

    private LocalDate dob;

    private String countryIso2;

    private Gender gender;

    private String language;

    @Relationship(type = "FRIEND")
    public Set<User> friends = new HashSet<>();

    @Relationship(type = "FOLLOW")
    public Set<User> following = new HashSet<>();

    @Relationship(type = "FOLLOWED")
    public Set<User> followers = new HashSet<>();
}
