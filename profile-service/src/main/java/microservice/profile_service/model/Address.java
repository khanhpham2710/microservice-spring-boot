package microservice.profile_service.model;

import lombok.*;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.validation.annotation.Validated;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Validated
public class Address {
    @Id
    @GeneratedValue
    private Long id;

    private String street;
    private String houseNumber;
    private String zipCode;
}
