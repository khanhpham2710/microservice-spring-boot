package microservice.order_service.kafka;

import microservice.common_service.enums.PaymentMethod;
import microservice.common_service.model.PurchaseResponse;
import microservice.order_service.customer.CustomerResponse;

import java.math.BigDecimal;
import java.util.List;

public record OrderConfirmation (
        Long id,
        BigDecimal totalAmount,
        BigDecimal amountReceive,
        PaymentMethod paymentMethod,
        CustomerResponse customer,
        List<PurchaseResponse> products
) {
}
