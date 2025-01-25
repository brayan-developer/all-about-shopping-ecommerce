package com.microservice.gateway.filter;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

@Component
public class RouteValidator {

    private static final Map<String, List<HttpMethod>> openApiEndpoints = Map.of(
            "/api/users", List.of(HttpMethod.POST),
            "/api/orders/payment-notification", List.of(HttpMethod.POST)
    );

    public Predicate<ServerHttpRequest> isSecured =
            request -> openApiEndpoints.entrySet()
                    .stream()
                    .noneMatch(entry -> request.getURI().getPath().equals(entry.getKey())
                            && entry.getValue().contains(request.getMethod()));
}
