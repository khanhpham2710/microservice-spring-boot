package microservice.order_service.customer;

import microservice.order_service.config.FeignClientConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

@Component
@FeignClient(name = "profile-service",configuration = FeignClientConfig.class)
public interface CustomerClient {
    @GetMapping("/{userId}")
    Optional<CustomerResponse> findCustomerById(@PathVariable("customer-id") String customerId);
}
