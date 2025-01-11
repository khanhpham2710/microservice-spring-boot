package microservice.gateway.config;

import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionLocator;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;


@Configuration
public class SwaggerConfig {

    @Bean
    public RouteDefinitionLocator routeDefinitionLocator(RouteLocator routeLocator) {
        return () -> routeLocator.getRoutes()
                .map(route -> {
                    RouteDefinition definition = new RouteDefinition();
                    definition.setId(route.getId());
                    definition.setUri(route.getUri());

                    PredicateDefinition predicateDefinition = new PredicateDefinition();
                    predicateDefinition.setName(route.getPredicate().getClass().getSimpleName());

                    definition.setPredicates(Collections.singletonList(predicateDefinition));
                    return definition;
                });

    }
}
