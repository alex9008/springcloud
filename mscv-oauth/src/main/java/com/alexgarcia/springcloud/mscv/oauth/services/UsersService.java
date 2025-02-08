package com.alexgarcia.springcloud.mscv.oauth.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import com.alexgarcia.springcloud.mscv.oauth.client.UserFeignClient;
import com.alexgarcia.springcloud.mscv.oauth.models.User;


@Component
public class UsersService implements UserDetailsService {

    //private final WebClient.Builder webClientBuilder;
    private final UserFeignClient userFeignClient;

    public UsersService(UserFeignClient userFeignClient) {
        this.userFeignClient = userFeignClient;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Map<String, String> params = new HashMap<>();
        params.put("username", username);

        try {
            User user = userFeignClient.findByUsername(username);

            List<SimpleGrantedAuthority> roles = user.getRoles().stream()
                    .map(role -> new SimpleGrantedAuthority(role.getName()))
                    .collect(Collectors.toList());

            return new org.springframework.security.core.userdetails.User(user.getName(), user.getPassword(),
                    user.isEnabled(),
                    true, true, true, roles);

        } catch (WebClientResponseException e) {
            throw new UsernameNotFoundException("Username or password incorrect" + e.getMessage());
        }
    }

}
