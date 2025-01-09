package com.alexgarcia.springcloud.msvc.items.controllers;

import org.springframework.web.bind.annotation.RestController;

import com.alexgarcia.springcloud.msvc.items.models.Item;
import com.alexgarcia.springcloud.msvc.items.models.Product;
import com.alexgarcia.springcloud.msvc.items.services.ItemService;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
public class ItemController {

    private final ItemService itemService;
    private final CircuitBreakerFactory circuitBreakerFactory;

    public ItemController(@Qualifier("itemSerciceWebClient") ItemService itemService,
            CircuitBreakerFactory circuitBreakerFactory) {
        this.itemService = itemService;
        this.circuitBreakerFactory = circuitBreakerFactory;
    }

    @GetMapping
    public List<Item> list(@RequestParam(name = "name", required = false) String name,
            @RequestHeader(name = "X-Request-Foo", required = false) String token) {
        System.out.println("name = " + name);
        System.out.println("token = " + token);
        return itemService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> detail(@PathVariable Long id) {

        Optional<Item> item = circuitBreakerFactory.create("itemss").run(() -> itemService.findById(id),
                throwable -> {
                    System.out.println("throwable = " + throwable);
                    return Optional.empty();
                });

        if (item.isEmpty()) {
            return ResponseEntity.status(404).body(Collections.singletonMap("message", "Item not found"));
        }

        return ResponseEntity.ok(item.get());
    }

    @CircuitBreaker(name = "items", fallbackMethod = "getItemsFallback")
    @GetMapping("/circuit/{id}")
    public ResponseEntity<?> detailCircuit(@PathVariable Long id) {

        Optional<Item> item = itemService.findById(id);

        if (item.isEmpty()) {
            return ResponseEntity.status(404).body(Collections.singletonMap("message", "Item not found"));
        }

        return ResponseEntity.ok(item.get());
    }

    public ResponseEntity<?> getItemsFallback(Throwable throwable) {
        System.out.println("throwable = " + throwable);
        return ResponseEntity
                .status(500)
                .body(getItemsFallback1());
    }

    @CircuitBreaker(name = "items", fallbackMethod = "getItemsFallback1")
    @TimeLimiter(name = "items")
    @GetMapping("/timeout/{id}")
    public CompletableFuture<?> detailTimeout(@PathVariable Long id) {

        return CompletableFuture.supplyAsync(() -> {
            Optional<Item> item = itemService.findById(id);

            if (item.isEmpty()) {
                return ResponseEntity.status(404).body(Collections.singletonMap("message", "Item not found"));
            }

            return ResponseEntity.ok(item.get());
        });

    }

    public CompletableFuture<?> getItemsFallback1(Throwable throwable) {
        System.out.println("throwable = " + throwable);

        return CompletableFuture.completedFuture(ResponseEntity
                .status(500)
                .body(getItemsFallback1()));
    }

    private Item getItemsFallback1() {
        // create new Product
        Product product = new Product();
        product.setId(1L);
        product.setName("FallBack");
        product.setPrice(500.00);

        // create new Item
        Item item = new Item(product, null);
        item.setProduct(product);
        item.setQuantity(1);

        return item;
    }

}
