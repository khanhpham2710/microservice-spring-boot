package microservice.order_service.order;

import lombok.RequiredArgsConstructor;
import microservice.common_service.exception.NotFoundException;
import microservice.order_service.customer.CustomerClient;
import microservice.order_service.orderline.*;
import microservice.order_service.payment.PaymentClient;
import microservice.order_service.payment.PaymentRequest;
import microservice.order_service.product.ProductClient;
import microservice.order_service.product.PurchaseRequest;
import microservice.order_service.product.PurchaseResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository repository;
    private final OrderMapper mapper;
    private final OrderLineRepository orderLineRepository;
    private final OrderLineMapper orderLineMapper;


    private final CustomerClient customerClient;
    private final PaymentClient paymentClient;
    private final ProductClient productClient;


    @PreAuthorize("hasRole('USER')")
    @Transactional
    public Order createOrder(OrderRequest request) throws Exception {
        var customer = customerClient.findCustomerById(request.customerId())
                .orElseThrow(() -> new NotFoundException(
                        String.format("No customer found with the provided ID: %s", request.customerId())));

        List<PurchaseResponse> purchasedProducts = Optional.ofNullable(productClient.purchaseProducts(request.products()))
                .filter(response -> !response.isEmpty())
                .orElseThrow(() -> new Exception("An error occurred while processing the products purchase"));

        Order order = repository.save(mapper.toOrder(request));

        for (PurchaseRequest purchaseRequest : request.products()) {
            OrderLine orderLine = orderLineMapper.toOrderLine(new OrderLineRequest(
                    order.getId(),
                    purchaseRequest.productId(),
                    purchaseRequest.quantity()
            ),order);

            orderLineRepository.save(orderLine);
        }

        order.setTotalAmount(purchasedProducts.stream()
                .map(PurchaseResponse::price)
                .reduce(BigDecimal.ZERO, BigDecimal::add));

        var paymentRequest = new PaymentRequest(
                request.amount(),
                request.paymentMethod(),
                order.getId(),
                customer
        );

        paymentClient.requestOrderPayment(paymentRequest);

        return repository.save(order);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public List<OrderResponse> findAllOrders() {
        return repository.findAll()
                .stream()
                .map(mapper::fromOrder)
                .toList();
    }

    @PreAuthorize("hasRole('USER')")
    public OrderResponse findById(Integer id) {
        return repository.findById(id)
                .map(mapper::fromOrder)
                .orElseThrow(() -> new NotFoundException(String.format("No order found with the provided ID: %d", id)));
    }
}
