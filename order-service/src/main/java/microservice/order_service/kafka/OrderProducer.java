package microservice.order_service.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.springframework.kafka.support.KafkaHeaders.TOPIC;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderProducer {
    private final KafkaTemplate<String, OrderConfirmation> kafkaTemplate;
    private final FailedOrderConfirmationRepository repository;

    public void sendOrderConfirmation(OrderConfirmation orderConfirmation) {
        Message<OrderConfirmation> message = MessageBuilder
                .withPayload(orderConfirmation)
                .setHeader(TOPIC, "order-topic")
                .build();

        kafkaTemplate.send(message).whenComplete(
                (result, ex) -> {
            if (ex != null) {
                repository.save(orderConfirmation);
            }
        });
    }

    @Scheduled(fixedDelay = 1000)
    public void producer() {
        List<OrderConfirmation> orderConfirmations = repository.findAll();

        for (OrderConfirmation orderConfirmation : orderConfirmations) {
            Message<OrderConfirmation> message = MessageBuilder
                    .withPayload(orderConfirmation)
                    .setHeader(TOPIC, "order-topic")
                    .build();

            kafkaTemplate.send(message).whenComplete(
                    (result, ex) -> {
                        if (ex == null) {
                            orderConfirmation.setStatus(true);
                            repository.save(orderConfirmation);
                        } else {
                            log.error("Unable to send message của order [{}] due to : {}", orderConfirmation.getOrder().getId(), ex.getMessage());
                        }
                    }
            );
        }
    }

    @Scheduled(fixedDelay = 60000)
    public void delete() {
        List<OrderConfirmation> orderConfirmations = repository.findByStatus(true);
        repository.deleteAllInBatch(orderConfirmations);
    }
}
