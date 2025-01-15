package microservice.order_service.product;

import microservice.order_service.config.FeignClientConfig;
import microservice.order_service.kafka.PurchaseResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "product-service", configuration = FeignClientConfig.class)
public interface ProductClient {
    @PostMapping("/purchase")
    List<PurchaseResponse> purchaseProducts(@RequestBody List<PurchaseRequest> requestBody);

    @PostMapping("/return")
    void returnProducts(@RequestBody List<PurchaseResponse> requestBody);
}
