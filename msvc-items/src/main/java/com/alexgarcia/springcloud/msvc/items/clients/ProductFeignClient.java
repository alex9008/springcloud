package com.alexgarcia.springcloud.msvc.items.clients;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.alexgarcia.libs.msvc.commons.entities.Product;


@FeignClient(name = "msvc-products")
public interface ProductFeignClient {

    @GetMapping
    List<Product> findAll();

    @GetMapping("/{id}")
    Product findById(@PathVariable Long id);

    @PostMapping
    Product save(@RequestBody Product product);

    @PutMapping("/{id}")
    Product update(@RequestBody Product product, @PathVariable Long id);

    @DeleteMapping("/{id}")
    void delete(@PathVariable Long id);

}
