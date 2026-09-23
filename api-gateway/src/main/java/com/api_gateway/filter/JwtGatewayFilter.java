package com.api_gateway.filter;

import java.nio.charset.StandardCharsets;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import reactor.core.publisher.Mono;

@Component
public class JwtGatewayFilter implements GlobalFilter, Ordered {

    @Value("${jwt.secret}")
    private String secret;

    @Override
    public Mono<Void> filter(
            ServerWebExchange exchange,
            GatewayFilterChain chain) {

        String path = exchange.getRequest()
                .getURI()
                .getPath();

        HttpMethod method = exchange.getRequest()
                .getMethod();

        if (method == HttpMethod.OPTIONS
                || path.equals("/auth/login")
                || path.equals("/auth/register")) {

            return chain.filter(exchange);
        }

        String authHeader = exchange.getRequest()
                .getHeaders()
                .getFirst("Authorization");

        if (authHeader == null
                || !authHeader.startsWith("Bearer ")) {

            exchange.getResponse()
                    .setStatusCode(HttpStatus.UNAUTHORIZED);

            return exchange.getResponse().setComplete();
        }

        String token = authHeader.substring(7);

        try {

            Claims claims = Jwts.parser()
                    .verifyWith(
                            Keys.hmacShaKeyFor(
                                    secret.getBytes(
                                            StandardCharsets.UTF_8)))
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            String role = claims.get("role", String.class);

            if (role == null) {

                exchange.getResponse()
                        .setStatusCode(HttpStatus.FORBIDDEN);

                return exchange.getResponse().setComplete();
            }

            if (path.startsWith("/jobs")) {

                if (method == HttpMethod.POST
                        || method == HttpMethod.PUT
                        || method == HttpMethod.DELETE) {

                    if (!role.equals("RECRUITER")) {

                        exchange.getResponse()
                                .setStatusCode(HttpStatus.FORBIDDEN);

                        return exchange.getResponse().setComplete();
                    }
                }
            }

            if (path.startsWith("/applications")) {

                if (path.matches("/applications/job/.*")
                        || path.matches("/applications/.*/status")) {

                    if (!role.equals("RECRUITER")) {

                        exchange.getResponse()
                                .setStatusCode(HttpStatus.FORBIDDEN);

                        return exchange.getResponse().setComplete();
                    }
                }

                if (method == HttpMethod.POST) {

                    if (!role.equals("CANDIDATE")) {

                        exchange.getResponse()
                                .setStatusCode(HttpStatus.FORBIDDEN);

                        return exchange.getResponse().setComplete();
                    }
                }
            }

            return chain.filter(exchange);

        } catch (Exception e) {

            exchange.getResponse()
                    .setStatusCode(HttpStatus.UNAUTHORIZED);

            return exchange.getResponse().setComplete();
        }
    }

    @Override
    public int getOrder() {
        return -100;
    }
}