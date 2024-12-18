package microservice.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import reactor.core.publisher.Mono;

import java.util.Objects;

@SpringBootApplication
@ComponentScan({"microservice.common_service","microservice.gateway"})
public class GatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(GatewayApplication.class, args);
	}

	@Bean
	public KeyResolver keyResolver(){
		return exchange -> Mono.just(Objects.requireNonNull(exchange.getRequest()
				.getRemoteAddress()).getAddress().getHostAddress());
	}
}
