package microservice.order_service.customer;

import jakarta.persistence.Embeddable;
import microservice.order_service.order.Address;

@Embeddable
public record CustomerResponse(
        String id,
        String firstName,
        String lastName,
        String email,
        Address address
) {

}
