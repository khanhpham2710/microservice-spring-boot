package microservice.gateway.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import microservice.common_service.exception.ErrorCode;
import microservice.common_service.exception.ErrorResponse;
import org.springframework.http.MediaType;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Date;

@Component
public class CustomAuthenticationEntryPoint implements ServerAuthenticationEntryPoint {
    @Override
    public Mono<Void> commence(ServerWebExchange exchange, org.springframework.security.core.AuthenticationException authException) {
        ErrorCode errorCode = ErrorCode.UNAUTHENTICATED;

        exchange.getResponse().setStatusCode(errorCode.getStatusCode());
        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);

        String requestURI = exchange.getRequest().getURI().getPath();
        String queryString = exchange.getRequest().getURI().getQuery();

        ErrorResponse apiResponse = ErrorResponse.builder()
                .errorCode(errorCode.getCode())
                .message(errorCode.getMessage())
                .path(queryString != null ? requestURI + "?" + queryString : requestURI)
                .timestamp(new Date())
                .build();

        ObjectMapper objectMapper = new ObjectMapper();
        return exchange.getResponse().writeWith(Mono.fromSupplier(() -> {
            try {
                return exchange.getResponse().bufferFactory().wrap(objectMapper.writeValueAsBytes(apiResponse));
            } catch (Exception e) {
                return null;
            }
        }));
    }
}