package microservice.profile_service.proxy;

import jakarta.validation.Valid;
import microservice.common_service.response.BaseResponse;
import microservice.profile_service.config.FeignClientConfig;
import microservice.profile_service.dto.KeycloakUser;
import microservice.profile_service.model.UpdateUserRecord;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

@Component
@FeignClient(name = "authentication-service",configuration = FeignClientConfig.class)
public interface AuthProxy {
    @GetMapping("/{userId}")
    KeycloakUser getUserById(@PathVariable String userId);

    @DeleteMapping("/delete/{id}")
    String deleteUserById(@PathVariable String id);

    @PutMapping("/update-name/{userId}")
    String updateUser (UpdateUserRecord request, @PathVariable String userId);
}

