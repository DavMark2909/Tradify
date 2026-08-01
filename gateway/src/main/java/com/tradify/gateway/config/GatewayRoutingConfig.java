package com.tradify.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

import static org.springframework.cloud.gateway.server.mvc.filter.BeforeFilterFunctions.uri;
import static org.springframework.cloud.gateway.server.mvc.filter.TokenRelayFilterFunctions.tokenRelay;
import static org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions.route;
import static org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions.http;
import static org.springframework.cloud.gateway.server.mvc.predicate.GatewayRequestPredicates.path;

@Configuration
public class GatewayRoutingConfig {

    @Bean
    public RouterFunction<ServerResponse> customRouteLocator() {
        return route("backend-api-route")
                .route(path("/api/**"), http()) // 1. http() now takes 0 arguments
                .before(uri("http://localhost:8082"))
                .filter(tokenRelay()) // MAGIC: Grabs JWT from Redis and adds Authorization: Bearer
                .build();
    }
}
