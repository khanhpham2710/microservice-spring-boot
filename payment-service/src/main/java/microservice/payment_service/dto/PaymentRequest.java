package microservice.payment_service.dto;

import microservice.common_service.enums.PaymentMethod;
import microservice.payment_service.model.Customer;

import java.math.BigDecimal;

public record PaymentRequest(
        Integer id,
        BigDecimal amount,
        PaymentMethod paymentMethod,
        Integer orderId,
        String orderReference,
        Customer customer
) {
}

