package microservice.notification_service.kafka.payment;

import microservice.common_service.enums.PaymentMethod;

import java.math.BigDecimal;

public record PaymentConfirmation(
        BigDecimal totalAmount,
        BigDecimal amountReceive,
        PaymentMethod paymentMethod,
        String firstName,
        String lastName,
        String customerEmail
) {
}