package microservice.order_service.payment;

import microservice.common_service.enums.PaymentMethod;
import microservice.order_service.customer.CustomerResponse;

import java.math.BigDecimal;

public record PaymentRequest(
        BigDecimal amount,
        PaymentMethod paymentMethod,
        Integer orderId,
        CustomerResponse customer
) {
}