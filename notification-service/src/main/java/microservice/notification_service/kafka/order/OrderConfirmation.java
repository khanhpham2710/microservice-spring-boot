package microservice.notification_service.kafka.order;

import microservice.common_service.enums.PaymentMethod;

import java.math.BigDecimal;
import java.util.List;


public record OrderConfirmation(
        Long id,
        BigDecimal totalAmount,
        BigDecimal amountReceive,
        PaymentMethod paymentMethod,
        Customer customer,
        List<PurchaseResponse> products
) {
}