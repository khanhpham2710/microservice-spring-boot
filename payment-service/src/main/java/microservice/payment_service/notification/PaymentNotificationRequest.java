package microservice.payment_service.notification;

import microservice.common_service.enums.PaymentMethod;

import java.math.BigDecimal;

public record PaymentNotificationRequest(
        BigDecimal totalAmount,
        BigDecimal amountReceive,
        PaymentMethod paymentMethod,
        String customerFirstname,
        String customerLastname,
        String customerEmail
) {
}
