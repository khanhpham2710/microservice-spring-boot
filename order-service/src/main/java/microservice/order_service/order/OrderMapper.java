package microservice.order_service.order;

import lombok.RequiredArgsConstructor;
import microservice.order_service.orderline.OrderLineMapper;
import microservice.order_service.orderline.OrderLineResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderMapper {
    private final OrderLineMapper mapper;

    public Order toOrder(OrderRequest request) {
        if (request == null) {
            return null;
        }
        return Order.builder()
                .paymentMethod(request.paymentMethod())
                .customerId(request.customerId())
                .build();
    }

    public OrderResponse fromOrder(Order order) {
        List<OrderLineResponse> orderLines = order.getOrderLines().stream()
                .map(mapper::toOrderLineResponse).toList();

        return new OrderResponse(
                order.getId(),
                order.getTotalAmount(),
                order.getPaymentMethod(),
                orderLines,
                order.getCustomerId()
        );
    }
}