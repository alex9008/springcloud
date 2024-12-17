package com.alexgarcia.springcloud.msvc.items.services;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.alexgarcia.springcloud.msvc.items.clients.ProductFeignClient;
import com.alexgarcia.springcloud.msvc.items.models.Item;
import com.alexgarcia.springcloud.msvc.items.models.Product;

import feign.FeignException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ItemServiceFeign implements ItemService {

    private final ProductFeignClient productClient;

    @Override
    public List<Item> findAll() {

        return productClient.findAll()
                .stream()
                .map(product -> new Item(product, new Random().nextInt(10) + 1))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Item> findById(Long id) {

        try {
            Product product = productClient.findById(id);
            return Optional.ofNullable(new Item(product, new Random().nextInt(10) + 1));
        } catch (FeignException e) {
            return Optional.empty();
        }

    }

}
