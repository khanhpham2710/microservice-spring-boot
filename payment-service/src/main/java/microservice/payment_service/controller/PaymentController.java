package microservice.payment_service.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import microservice.payment_service.dto.PaymentRequest;
import microservice.payment_service.service.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService service;

    @PreAuthorize("hasRole('USER')")
    @PostMapping
    public ResponseEntity<Integer> createPayment(
            @RequestBody @Valid PaymentRequest request
    ) {
        return ResponseEntity.ok(service.createPayment(request));
    }
}
