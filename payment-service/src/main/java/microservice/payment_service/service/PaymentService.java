package microservice.payment_service.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import microservice.payment_service.dto.PaymentMapper;
import microservice.payment_service.dto.PaymentRequest;
import microservice.payment_service.model.Payment;
import microservice.payment_service.notification.NotificationProducer;
import microservice.payment_service.notification.PaymentNotificationRequest;
import microservice.payment_service.repository.PaymentRepository;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository repository;
    private final PaymentMapper mapper;
    private final NotificationProducer notificationProducer;

    public Integer createPayment(PaymentRequest request) {
        Payment payment = repository.save(mapper.toPayment(request));

        notificationProducer.sendNotification(
                new PaymentNotificationRequest(
                        request.totalAmount(),
                        request.amountReceive(),
                        request.paymentMethod(),
                        request.customer().firstname(),
                        request.customer().lastname(),
                        request.customer().email()
                )
        );
        return payment.getId();
    }
}
