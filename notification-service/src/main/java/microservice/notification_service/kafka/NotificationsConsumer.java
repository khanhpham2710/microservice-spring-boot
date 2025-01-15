package microservice.notification_service.kafka;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import microservice.notification_service.email.EmailService;
import microservice.notification_service.kafka.order.OrderConfirmation;
import microservice.notification_service.kafka.payment.PaymentConfirmation;
import microservice.notification_service.notification.Notification;
import microservice.notification_service.notification.NotificationRepository;
import microservice.notification_service.notification.NotificationType;
import org.springframework.kafka.annotation.DltHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.retrytopic.DltStrategy;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.retry.annotation.Backoff;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationsConsumer {
    private final NotificationRepository repository;
    private final EmailService emailService;

    @RetryableTopic(
            attempts = "4", // Retry 3 lần + 1 lần vào DLT
            backoff = @Backoff(delay = 1000, multiplier = 2),
            autoCreateTopics = "true",
            dltStrategy = DltStrategy.FAIL_ON_ERROR
    )
    @Transactional
    @KafkaListener(topics = "payment-topic")
    public void consumePaymentSuccessNotifications(PaymentConfirmation paymentConfirmation) throws MessagingException, UnsupportedEncodingException {
        Notification notification = repository.save(
                Notification.builder()
                        .type(NotificationType.PAYMENT_CONFIRMATION)
                        .notificationDate(LocalDateTime.now())
                        .paymentConfirmation(paymentConfirmation)
                        .status(false)
                        .build()
        );
        log.info(paymentConfirmation.toString());

        String customerName = paymentConfirmation.firstName() + " " + paymentConfirmation.lastName();
//        emailService.sendPaymentSuccessEmail(
//                paymentConfirmation.customerEmail(),
//                customerName,
//                paymentConfirmation.amountReceive(),
//                paymentConfirmation.totalAmount()
//        );

        notification.setStatus(true);
        repository.save(notification);
    }

    @DltHandler
    void processDltPaymentSuccessNotifications(@Payload PaymentConfirmation paymentConfirmation) {
        log.info("DLT received message from payment-topic: {}", paymentConfirmation);

        repository.save(
                Notification.builder()
                        .type(NotificationType.PAYMENT_CONFIRMATION)
                        .notificationDate(LocalDateTime.now())
                        .paymentConfirmation(paymentConfirmation)
                        .status(false)
                        .build()
        );
    }



    @RetryableTopic(
            attempts = "4", // Retry 3 lần + 1 lần vào DLT
            backoff = @Backoff(delay = 1000, multiplier = 2),
            autoCreateTopics = "true",
            dltStrategy = DltStrategy.FAIL_ON_ERROR
    )
    @Transactional
    @KafkaListener(topics = "order-topic")
    public void consumeOrderConfirmationNotifications(OrderConfirmation orderConfirmation) throws MessagingException, UnsupportedEncodingException {
        Notification notification = repository.save(
                Notification.builder()
                        .type(NotificationType.ORDER_CONFIRMATION)
                        .notificationDate(LocalDateTime.now())
                        .orderConfirmation(orderConfirmation)
                        .status(false)
                        .build()
        );


        String customerName = orderConfirmation.customer().firstName() + " " + orderConfirmation.customer().lastName();

//        emailService.sendOrderConfirmationEmail(
//                orderConfirmation.customer().email(),
//                customerName,
//                orderConfirmation.id(),
//                orderConfirmation.totalAmount(),
//                orderConfirmation.products()
//        );

        notification.setStatus(true);
        repository.save(notification);
    }

    @DltHandler
    void processDltOrderConfirmationNotifications(@Payload OrderConfirmation orderConfirmation) {
        log.info("DLT received message from order-topic: {}", orderConfirmation);

        repository.save(
                Notification.builder()
                        .type(NotificationType.ORDER_CONFIRMATION)
                        .notificationDate(LocalDateTime.now())
                        .orderConfirmation(orderConfirmation)
                        .status(false)
                        .build()
        );
    }
}
