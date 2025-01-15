package microservice.order_service.kafka;

import jakarta.persistence.Embeddable;

import java.math.BigDecimal;

@Embeddable
public record PurchaseResponse(
        Integer productId,
        String name,
        String description,
        BigDecimal price,
        double quantity
) {
}

