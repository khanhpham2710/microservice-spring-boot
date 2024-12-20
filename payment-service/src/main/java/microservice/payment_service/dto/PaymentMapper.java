package microservice.payment_service.dto;

import microservice.payment_service.model.Payment;
import org.springframework.stereotype.Service;

@Service
public class PaymentMapper {

    public Payment toPayment(PaymentRequest request) {
        if (request == null) {
            return null;
        }
        return Payment.builder()
                .id(request.id())
                .paymentMethod(request.paymentMethod())
                .amount(request.totalAmount())
                .orderId(request.orderId())
                .build();
    }
}
