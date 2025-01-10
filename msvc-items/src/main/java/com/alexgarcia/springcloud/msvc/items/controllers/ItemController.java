package com.alexgarcia.springcloud.msvc.items.controllers;

import org.springframework.web.bind.annotation.RestController;

import com.alexgarcia.libs.msvc.commons.entities.Product;
import com.alexgarcia.springcloud.msvc.items.models.Item;
import com.alexgarcia.springcloud.msvc.items.services.ItemService;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;


@RestController
@RefreshScope
public class ItemController {

    private final ItemService itemService;
    private final CircuitBreakerFactory circuitBreakerFactory;
    private final Environment env;

    @Value("${configurations.text}")
    private String text;

    @Value("${server.port}")
    private String port;

    @Value("${configurations.autor.name}")
    private String autorName;

    @Value("${configurations.autor.email}")
    private String autorEmail;

    public ItemController(@Qualifier("itemServiceFeign") ItemService itemService,
            CircuitBreakerFactory circuitBreakerFactory, Environment env) {
        this.itemService = itemService;
        this.circuitBreakerFactory = circuitBreakerFactory;
        this.env = env;
    }

    @GetMapping("/fetch-config")
    public ResponseEntity<?> fetchCofin() {

        Map<String, String> configs = Map.of("text", text, "port", port, "autorName", autorName, "autorEmail",
                autorEmail);
        if (env.getActiveProfiles().length > 0 && env.getActiveProfiles()[0].equals("dev")) {
            return ResponseEntity.ok(configs);
            
        }

        return ResponseEntity.ok(configs);
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

    @PostMapping()
    public ResponseEntity<?> save(@RequestBody Product product) {
        return ResponseEntity.status(HttpStatus.CREATED).body(itemService.save(product));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@RequestBody Product product, @PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.CREATED).body(itemService.update(product, id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        itemService.delete(id);
        return ResponseEntity.noContent().build();
    }
    

}
