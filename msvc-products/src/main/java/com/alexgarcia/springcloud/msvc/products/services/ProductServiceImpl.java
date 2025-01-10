package com.alexgarcia.springcloud.msvc.products.services;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alexgarcia.libs.msvc.commons.entities.Product;
import com.alexgarcia.springcloud.msvc.products.repositories.ProductRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final Environment environment;

    @Override
    @Transactional(readOnly = true)
    public List<Product> findAll() {
        return ((List<Product>) productRepository.findAll()).stream().map(product -> {
            product.setPort(Integer.parseInt(environment.getProperty("local.server.port")));
            return product;
        }).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Product> findById(Long id) {
        return productRepository.findById(id).map(producto -> {
            producto.setPort(Integer.parseInt(environment.getProperty("local.server.port")));
            return producto;
        });
    }

    @Override
    @Transactional
    public Product save(Product product) {

        product.setCreatedAt(LocalDate.now());
        return productRepository.save(product);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        productRepository.deleteById(id);
    }

}
