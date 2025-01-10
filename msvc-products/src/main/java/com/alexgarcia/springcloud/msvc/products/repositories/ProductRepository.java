package com.alexgarcia.springcloud.msvc.products.repositories;

import org.springframework.data.repository.CrudRepository;

import com.alexgarcia.libs.msvc.commons.entities.Product;


public interface ProductRepository extends CrudRepository<Product, Long> {

}
