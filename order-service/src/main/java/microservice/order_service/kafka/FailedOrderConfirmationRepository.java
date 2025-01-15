package microservice.order_service.kafka;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FailedOrderConfirmationRepository extends JpaRepository<OrderConfirmation, Long> {
    List<OrderConfirmation> findByStatus(boolean status);
}
