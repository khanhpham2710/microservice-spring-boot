package microservice.order_service.orderline;

public record OrderLineRequest(
        Long orderId,
        Integer productId,
        double quantity
) {
}
