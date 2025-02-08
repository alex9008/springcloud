package com.alexgarcia.springcloud.mscv.oauth.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.alexgarcia.springcloud.mscv.oauth.models.User;

@FeignClient(name = "msvc-users")
public interface UserFeignClient {

    @GetMapping("/username/{username}")
    public User findByUsername(@PathVariable String username);
    
}