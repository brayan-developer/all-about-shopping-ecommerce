package com.microservice.gateway.filter;

import com.microservice.gateway.exception.UnauthorizedException;
import com.microservice.gateway.util.JwtUtil;
import io.jsonwebtoken.JwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import java.util.Objects;

import static com.microservice.gateway.constants.ErrorMessageConstants.MISSING_AUTHORIZATION;

@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

    @Autowired
    private RouteValidator validator;

    @Autowired
    private JwtUtil jwtUtil;

    @Value("${gateway.custom-headers.user-id}")
    private String nameHeaderUserId;

    public AuthenticationFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain) -> {
            if (validator.isSecured.test(exchange.getRequest())) {
                //header contains token or not
                if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                    throw new UnauthorizedException(MISSING_AUTHORIZATION);
                }


                String authHeader = Objects.requireNonNull(exchange.getRequest().getHeaders()
                        .get(HttpHeaders.AUTHORIZATION)).get(0);
                if (authHeader != null && authHeader.startsWith("Bearer ")) {
                    authHeader = authHeader.substring(7);
                }

                try {

                    jwtUtil.isTokenValid(authHeader);
                    String userId = jwtUtil.extractUserId(authHeader);

                    // Agrega el userId al header de la solicitud
                    ServerWebExchange modifiedExchange = exchange.mutate()
                            .request(exchange.getRequest().mutate()
                                    .header( nameHeaderUserId, userId)
                                    .build())
                            .build();

                    return chain.filter(modifiedExchange);

                } catch (JwtException e) {
                    throw new JwtException(e.getMessage());
                }
            }
            return chain.filter(exchange);
        });
    }



    public static class Config {

    }



}