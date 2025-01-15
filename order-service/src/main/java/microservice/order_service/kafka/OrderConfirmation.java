package microservice.order_service.kafka;

import jakarta.persistence.*;
import lombok.*;
import microservice.common_service.enums.PaymentMethod;
import microservice.order_service.customer.CustomerResponse;
import microservice.order_service.order.Order;

import java.math.BigDecimal;
import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(schema = "order_confirmation_failed")
public class OrderConfirmation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "order_id")
    private Order order;

    private BigDecimal totalAmount;
    private BigDecimal amountReceive;

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    @Embedded
    @AttributeOverride(name = "id", column = @Column(name = "customer_id"))
    private CustomerResponse customer;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "order_confirmation_products", joinColumns = @JoinColumn(name = "order_confirmation_id"))
    private List<PurchaseResponse> products;

    private boolean status;
}