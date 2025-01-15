package microservice.order_service.order;

import lombok.RequiredArgsConstructor;
import microservice.common_service.exception.AppException;
import microservice.common_service.exception.ErrorCode;
import microservice.common_service.exception.NotFoundException;
import microservice.order_service.customer.CustomerClient;
import microservice.order_service.customer.CustomerResponse;
import microservice.order_service.exception.OrderException;
import microservice.order_service.kafka.OrderConfirmation;
import microservice.order_service.kafka.OrderProducer;
import microservice.order_service.kafka.PurchaseResponse;
import microservice.order_service.orderline.*;
import microservice.order_service.payment.PaymentClient;
import microservice.order_service.payment.PaymentRequest;
import microservice.order_service.product.ProductClient;
import microservice.order_service.product.PurchaseRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
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

    private final OrderProducer orderProducer;

    @PreAuthorize("hasRole('USER')")
    @Transactional
    public OrderResponse createOrder(OrderRequest request) throws Exception {
        CustomerResponse customer = customerClient.findCustomerById(request.customerId())
                .orElseThrow(() -> new NotFoundException(
                        String.format("No customer found with the provided ID: %s", request.customerId())));

        List<PurchaseResponse> purchasedProducts = Optional.ofNullable(productClient.purchaseProducts(request.products()))
                .filter(response -> !response.isEmpty())
                .orElseThrow(() -> new Exception("An error occurred while processing the products purchase"));

        Order order = repository.save(mapper.toOrder(request));
        order.setAddress(customer.address());

        for (PurchaseRequest purchaseRequest : request.products()) {
            OrderLine orderLine = orderLineMapper.toOrderLine(new OrderLineRequest(
                    order.getId(),
                    purchaseRequest.productId(),
                    purchaseRequest.quantity()
            ),order);

            orderLineRepository.save(orderLine);
        }

        BigDecimal totalAmount = purchasedProducts.stream()
                .map(PurchaseResponse::price)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        order.setTotalAmount(totalAmount);

        BigDecimal changeAmount = request.amount().subtract(totalAmount);
        if (changeAmount.compareTo(BigDecimal.ZERO) < 0){
                productClient.returnProducts(purchasedProducts);
            throw new OrderException("Not enough money. The total amount is " + totalAmount);
        }

        order.setChangeAmount(request.amount().subtract(totalAmount));

        PaymentRequest paymentRequest = new PaymentRequest(
                totalAmount,
                request.amount(),
                request.paymentMethod(),
                order.getId(),
                customer
        );

        paymentClient.requestOrderPayment(paymentRequest);

        orderProducer.sendOrderConfirmation(
                OrderConfirmation.builder()
                        .id(order.getId())
                        .customer(customer)
                        .order(order)
                        .amountReceive(request.amount())
                        .totalAmount(totalAmount)
                        .paymentMethod(request.paymentMethod())
                        .status(false)
                        .products(purchasedProducts)
                        .build()
        );

        return mapper.fromOrder(repository.save(order));
    }

    @PreAuthorize("hasRole('ADMIN')")
    public List<OrderResponse> findAllOrders() {
        return repository.findAll()
                .stream()
                .map(mapper::fromOrder)
                .toList();
    }

    @PreAuthorize("hasRole('USER')")
    public OrderResponse findById(Long id) {
        return repository.findById(id)
                .map(mapper::fromOrder)
                .orElseThrow(() -> new NotFoundException(String.format("No order found with the provided ID: %d", id)));
    }
}
