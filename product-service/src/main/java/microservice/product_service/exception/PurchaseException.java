package microservice.product_service.exception;

public class PurchaseException extends RuntimeException {
    public PurchaseException(String message) {
        super(message);
    }
}
