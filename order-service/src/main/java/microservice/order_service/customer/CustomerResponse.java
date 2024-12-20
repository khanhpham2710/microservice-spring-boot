package microservice.order_service.customer;

import microservice.order_service.order.Address;

public record CustomerResponse(
        String id,
        String firstname,
        String lastname,
        String email,
        Address address
) {

}
