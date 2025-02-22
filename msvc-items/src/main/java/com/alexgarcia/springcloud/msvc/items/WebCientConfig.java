package com.alexgarcia.springcloud.msvc.items;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.loadbalancer.reactive.ReactorLoadBalancerExchangeFilterFunction;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebCientConfig {

    // @Bean
    // @LoadBalanced
    // WebClient.Builder registrarWebClient() {
    // return WebClient.builder().baseUrl(baseUrl);
    // }

    @Bean
    WebClient registrarWebClient(WebClient.Builder webClientBuilder,
            ReactorLoadBalancerExchangeFilterFunction lbFunction,
            @Value("${config.baseurl.endpoint.msvc-products}") String baseUrl) {
        return webClientBuilder.baseUrl(baseUrl)
                .filter(lbFunction)
                .build();
    }

}
