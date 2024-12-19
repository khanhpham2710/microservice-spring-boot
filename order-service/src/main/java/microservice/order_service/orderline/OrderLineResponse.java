package microservice.order_service.orderline;

public record OrderLineResponse(
        Integer id,
        double quantity
) { }
