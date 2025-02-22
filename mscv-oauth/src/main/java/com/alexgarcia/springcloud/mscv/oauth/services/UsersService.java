package com.alexgarcia.springcloud.mscv.oauth.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import com.alexgarcia.springcloud.mscv.oauth.client.UserFeignClient;
import com.alexgarcia.springcloud.mscv.oauth.models.User;

import io.micrometer.tracing.Tracer;

@Component
public class UsersService implements UserDetailsService {

    private static final Logger log = LoggerFactory.getLogger(UsersService.class);

    // private final WebClient.Builder webClientBuilder;
    private final UserFeignClient userFeignClient;
    private final Tracer tracer;

    public UsersService(UserFeignClient userFeignClient, Tracer tracer) {
        this.userFeignClient = userFeignClient;
        this.tracer = tracer;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        log.info("Loading user by username: {}", username);

        Map<String, String> params = new HashMap<>();
        params.put("username", username);

        try {
            User user = userFeignClient.findByUsername(username);

            List<SimpleGrantedAuthority> roles = user.getRoles().stream()
                    .map(role -> new SimpleGrantedAuthority(role.getName()))
                    .collect(Collectors.toList());

            log.info("User loaded: {}", user.getName());
            tracer.currentSpan().tag("user.username", user.getName());

            return new org.springframework.security.core.userdetails.User(user.getName(), user.getPassword(),
                    user.isEnabled(),
                    true, true, true, roles);

        } catch (WebClientResponseException e) {
            log.error("Error loading user by username: {}", e.getMessage());
            tracer.currentSpan().tag("error.message", e.getMessage());
            throw new UsernameNotFoundException("Username or password incorrect" + e.getMessage());
        }
    }

}
