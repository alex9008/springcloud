package com.alexgarcia.springcloud.msvc.items.services;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import com.alexgarcia.springcloud.msvc.items.models.Item;
import com.alexgarcia.springcloud.msvc.items.models.Product;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ItemSerciceWebClient implements ItemService {

    private final WebClient.Builder webClientBuilder;

    @Override
    public List<Item> findAll() {
        return this.webClientBuilder.build()
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
        try {
            return Optional.of(this.webClientBuilder.build()
                    .get()
                    .uri("/{id}", params)
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .bodyToMono(Product.class)
                    .map(item -> new Item(item, new Random().nextInt(10) + 1))
                    .block());
        } catch (WebClientResponseException e) {
            return Optional.empty();
        }

    }

}
