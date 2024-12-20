package com.alexgarcia.springcloud.msvc.products.controllers;

import org.springframework.web.bind.annotation.RestController;

import com.alexgarcia.springcloud.msvc.products.entities.Product;
import com.alexgarcia.springcloud.msvc.products.services.ProductService;
import com.alexgarcia.springcloud.msvc.products.services.ProductServiceImpl;


import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@RestController
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductServiceImpl productService) {
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<List<Product>> list() {
        return ResponseEntity.ok(productService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        
        Optional<Product> product = productService.findById(id);

        if (product.isPresent()) {
            return ResponseEntity.ok(product.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    




}
