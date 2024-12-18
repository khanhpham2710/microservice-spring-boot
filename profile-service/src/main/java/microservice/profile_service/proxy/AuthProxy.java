package microservice.profile_service.proxy;

import microservice.profile_service.config.FeignClientConfig;
import microservice.profile_service.dto.KeycloakUser;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Component
@FeignClient(name = "authentication-service",configuration = FeignClientConfig.class)
public interface AuthProxy {
    @GetMapping("/{userId}")
    KeycloakUser getUserById(@PathVariable String userId);
}

