package microservice.product_service.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record ReturnRequest(
        @NotNull(message = "Product is mandatory")
        Integer productId,
        @Positive(message = "Quantity is mandatory")
        double quantity
) {
}