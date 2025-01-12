package microservice.profile_service.proxy;

import microservice.profile_service.config.FeignClientConfig;
import microservice.profile_service.dto.KeycloakUser;
import microservice.profile_service.model.UpdateUserRecord;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

@Component
@FeignClient(name = "authentication-service",configuration = FeignClientConfig.class)
public interface AuthProxy {
    @GetMapping("/{userId}")
    KeycloakUser getUserById(@PathVariable String userId);

    @DeleteMapping("/delete/{id}")
    void deleteUserById(@PathVariable String id);

    @PutMapping("/update-name/{userId}")
    void updateUser (UpdateUserRecord request, @PathVariable String userId);
}

