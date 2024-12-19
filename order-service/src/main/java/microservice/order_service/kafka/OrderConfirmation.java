package microservice.order_service.kafka;

import microservice.common_service.enums.PaymentMethod;
import microservice.order_service.customer.CustomerResponse;
import microservice.order_service.product.PurchaseResponse;

import java.math.BigDecimal;
import java.util.List;

public record OrderConfirmation (
        String orderReference,
        BigDecimal totalAmount,
        PaymentMethod paymentMethod,
        CustomerResponse customer,
        List<PurchaseResponse> products

) {
}
