package com.alexgarcia.springcloud.app.gateway.filters;

import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;

@Component
public class SampleGlobalFilter implements GlobalFilter, Ordered {

    private static final Logger log = LoggerFactory.getLogger(SampleGlobalFilter.class);

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        log.info("Executing pre Global Filter");

        // Mutate the request and set it back into the exchange
        ServerHttpRequest mutatedRequest = exchange.getRequest().mutate()
                .headers(h -> h.add("token", UUID.randomUUID().toString()))
                .build();
        ServerWebExchange mutatedExchange = exchange.mutate().request(mutatedRequest).build();

        return chain.filter(mutatedExchange).then(Mono.fromRunnable(() -> {
            log.info("Executing post Global Filter");

            Optional.ofNullable(mutatedExchange.getRequest().getHeaders().getFirst("token"))
                    .ifPresent(value -> {
                        log.info("Header token: " + value);
                        mutatedExchange.getResponse().getHeaders().add("token", value);
                    });

            // add cookies
            mutatedExchange.getResponse().getCookies().add("color", ResponseCookie.from("color", "red").build());
            // modify response content type
            mutatedExchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);
        }));
    }

    @Override
    public int getOrder() {
        return 100;
    }

}
