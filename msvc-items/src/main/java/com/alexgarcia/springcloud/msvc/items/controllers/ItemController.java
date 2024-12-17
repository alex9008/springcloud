package com.alexgarcia.springcloud.msvc.items.controllers;

import org.springframework.web.bind.annotation.RestController;

import com.alexgarcia.springcloud.msvc.items.models.Item;
import com.alexgarcia.springcloud.msvc.items.services.ItemService;


import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
public class ItemController {

    
    private final ItemService itemService;

    public ItemController(@Qualifier("itemSerciceWebClient") ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping
    public List<Item> list() {
        return itemService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> detail(@PathVariable Long id) {

        Optional<Item> item = itemService.findById(id);

        if (item.isEmpty()) {
            return ResponseEntity.status(404).body(Collections.singletonMap("message", "Item not found"));
        }

        return ResponseEntity.ok(item.get());
    }

}
