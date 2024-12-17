package com.alexgarcia.springcloud.msvc.items.services;

import java.util.List;
import java.util.Optional;

import com.alexgarcia.springcloud.msvc.items.models.Item;

public interface ItemService {

    public List<Item> findAll();

    public Optional<Item> findById(Long id);
}
