package microservice.order_service.order;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import microservice.common_service.enums.PaymentMethod;
import microservice.order_service.orderline.OrderLineResponse;

import java.math.BigDecimal;
import java.util.List;

@JsonInclude(Include.NON_EMPTY)
public record OrderResponse(
        Long id,
        BigDecimal totalAmount,
        BigDecimal amountReceived,
        BigDecimal changeAmount,
        PaymentMethod paymentMethod,
        List<OrderLineResponse> orderlines,
        String customerId,
        Address address
) {

}
