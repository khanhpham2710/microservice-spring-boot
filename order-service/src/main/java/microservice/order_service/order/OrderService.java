package microservice.order_service.order;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import microservice.common_service.exception.AppException;
import microservice.common_service.exception.NotFoundException;
import microservice.order_service.customer.CustomerClient;
import microservice.order_service.orderline.OrderLineRequest;
import microservice.order_service.orderline.OrderLineService;
import microservice.order_service.payment.PaymentClient;
import microservice.order_service.payment.PaymentRequest;
import microservice.order_service.product.ProductClient;
import microservice.order_service.product.PurchaseRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository repository;
    private final OrderMapper mapper;
    private final CustomerClient customerClient;
    private final PaymentClient paymentClient;
    private final ProductClient productClient;
    private final OrderLineService orderLineService;


    @Transactional
    public Integer createOrder(OrderRequest request) throws Exception {
        var customer = customerClient.findCustomerById(request.customerId())
                .orElseThrow(() -> new NotFoundException(
                        String.format("No order found with the provided ID: %s", request.customerId())));

        var purchasedProducts = Optional.ofNullable(productClient.purchaseProducts(request.products()))
                .filter(response -> !response.isEmpty())
                .orElseThrow(() -> new Exception("An error occurred while processing the products purchase"));

        Order order = repository.save(mapper.toOrder(request));

        for (PurchaseRequest purchaseRequest : request.products()) {
            orderLineService.saveOrderLine(
                    new OrderLineRequest(
                            null,
                            order.getId(),
                            purchaseRequest.productId(),
                            purchaseRequest.quantity()
                    )
            );
        }
        var paymentRequest = new PaymentRequest(
                request.amount(),
                request.paymentMethod(),
                order.getId(),
                order.getReference(),
                customer
        );
        paymentClient.requestOrderPayment(paymentRequest);

        return order.getId();
    }

    public List<OrderResponse> findAllOrders() {
        return this.repository.findAll()
                .stream()
                .map(this.mapper::fromOrder)
                .collect(Collectors.toList());
    }

    public OrderResponse findById(Integer id) {
        return this.repository.findById(id)
                .map(this.mapper::fromOrder)
                .orElseThrow(() -> new NotFoundException(String.format("No order found with the provided ID: %d", id)));
    }
}
