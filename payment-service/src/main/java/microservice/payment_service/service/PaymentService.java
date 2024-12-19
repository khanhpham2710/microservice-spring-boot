package microservice.payment_service.service;

import lombok.RequiredArgsConstructor;
import microservice.payment_service.dto.PaymentMapper;
import microservice.payment_service.dto.PaymentRequest;
import microservice.payment_service.repository.PaymentRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository repository;
    private final PaymentMapper mapper;

    public Integer createPayment(PaymentRequest request) {
        var payment = repository.save(mapper.toPayment(request));

//        this.notificationProducer.sendNotification(
//                new PaymentNotificationRequest(
//                        request.orderReference(),
//                        request.amount(),
//                        request.paymentMethod(),
//                        request.customer().firstname(),
//                        request.customer().lastname(),
//                        request.customer().email()
//                )
//        );
        return payment.getId();
    }
}
