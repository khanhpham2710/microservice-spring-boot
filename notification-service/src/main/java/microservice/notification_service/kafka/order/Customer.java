package microservice.notification_service.kafka.order;

public record Customer(
        String id,
        String firstName,
        String lastName,
        String email
) {

}