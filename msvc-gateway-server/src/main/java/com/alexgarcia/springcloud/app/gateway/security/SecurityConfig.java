package com.alexgarcia.springcloud.app.gateway.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpMethod;
import org.springframework.lang.Nullable;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.web.server.SecurityWebFilterChain;

import reactor.core.publisher.Mono;

import static org.springframework.security.config.Customizer.withDefaults;

import java.util.Collection;
import java.util.stream.Collectors;

@Configuration
public class SecurityConfig {

    @Bean
    SecurityWebFilterChain securityFilterChain(ServerHttpSecurity http) throws Exception {

        return http.authorizeExchange((authorizeExchange) -> {
            authorizeExchange
                    .pathMatchers("/authorized", "/logout").permitAll()
                    .pathMatchers(HttpMethod.GET, "/api/itmes", "/api/products", "/api/users").permitAll()
                    .pathMatchers(HttpMethod.GET, "/api/items/{id}", "/api/products/{id}", "/api/users/{id}")
                    .hasAnyRole("ADMIN", "USER")
                    .pathMatchers("/api/items", "/api/products", "/api/users").hasRole("ADMIN")
                    .anyExchange().authenticated();
        }).cors(csrf -> csrf.disable())
                .oauth2Login(withDefaults())
                .oauth2Client(withDefaults())
                .oauth2ResourceServer(oauth -> oauth.jwt(
                        jwt -> jwt.jwtAuthenticationConverter(new Converter<Jwt, Mono<AbstractAuthenticationToken>>() {

                            @Override
                            @Nullable
                            public Mono<AbstractAuthenticationToken> convert(@SuppressWarnings("null") Jwt source) {
                                Collection<String> authorities = source.getClaimAsStringList("roles");
                                Collection<GrantedAuthority> grantedAuthorities = authorities.stream()
                                        .map(SimpleGrantedAuthority::new)
                                        .collect(Collectors.toList());

                                return Mono.just(new JwtAuthenticationToken(source, grantedAuthorities));

                            }

                        })))
                .build();

    }

    // SecurityConfig class for Spring MVC (Servlet)

    // @Bean
    // SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    // return http.authorizeHttpRequests((authz) -> {
    // authz
    // .requestMatchers("/authorized", "/logout").permitAll()
    // .requestMatchers(HttpMethod.GET, "/api/products", "/api/items",
    // "/api/users").permitAll()
    // .requestMatchers(HttpMethod.GET, "/api/products/{id}", "/api/items/{id}",
    // "/api/users/{id}")
    // .hasAnyRole("ADMIN", "USER")
    // .requestMatchers("/api/products/**", "/api/items/**",
    // "/api/users/**").hasRole("ADMIN")
    // .anyRequest().authenticated();
    // })
    // .sessionManagement(session ->
    // session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
    // .csrf(csrf -> csrf.disable())
    // .oauth2Login(login -> login.loginPage("/oauth2/authorization/client-app"))
    // .oauth2Client(withDefaults())
    // .oauth2ResourceServer(withDefaults())
    // .build();
    // }

}
