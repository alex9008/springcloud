package com.alexgarcia.springcloud.msvc.items.services;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.alexgarcia.libs.msvc.commons.entities.Product;
import com.alexgarcia.springcloud.msvc.items.models.Item;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ItemSerciceWebClient implements ItemService {

    private final WebClient webClientBuilder;

    @Override
    public List<Item> findAll() {
        return this.webClientBuilder
                .get()
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToFlux(Product.class)
                .map(item -> new Item(item, new Random().nextInt(10) + 1))
                .collectList()
                .block();
    }

    @Override
    public Optional<Item> findById(Long id) {
        Map<String, Object> params = Map.of("id", id);
        // try {
        return Optional.of(this.webClientBuilder
                .get()
                .uri("/{id}", params)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(Product.class)
                .map(item -> new Item(item, new Random().nextInt(10) + 1))
                .block());
        // } catch (WebClientResponseException e) {
        // return Optional.empty();
        // }

    }

    @Override
    public Product save(Product product) {

        return this.webClientBuilder
                .post()
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(product)
                .retrieve()
                .bodyToMono(Product.class)
                .block();

    }

    @Override
    public Product update(Product product, Long id) {

        return this.webClientBuilder
                .put()
                .uri("/{id}", Map.of("id", id))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(product)
                .retrieve()
                .bodyToMono(Product.class)
                .block();

    }

    @Override
    public void delete(Long id) {

        this.webClientBuilder
                .delete()
                .uri("/{id}", Map.of("id", id))
                .retrieve()
                .bodyToMono(Void.class)
                .block();

    }

}
