package com.main.bitebyte.controller;

import com.main.bitebyte.model.EcommerceProduct;
import com.main.bitebyte.repository.EcommerceProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/ecommerce-products")
public class EcommerceProductController {

    @Autowired
    private EcommerceProductRepository ecommerceProductRepository;

    @GetMapping("/api/ecommerce-products")
    public List<EcommerceProduct> getAllProducts() {
        return ecommerceProductRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<EcommerceProduct> getProductById(@PathVariable Long id) {
        Optional<EcommerceProduct> product = ecommerceProductRepository.findById(String.valueOf(id));
        return product.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public EcommerceProduct createProduct(@RequestBody EcommerceProduct product) {
        return ecommerceProductRepository.save(product);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EcommerceProduct> updateProduct(@PathVariable Long id, @RequestBody EcommerceProduct productDetails) {
        Optional<EcommerceProduct> product = ecommerceProductRepository.findById(String.valueOf(id));
        if (product.isPresent()) {
            EcommerceProduct updatedProduct = product.get();
            updatedProduct.setName(productDetails.getName());
            updatedProduct.setDescription(productDetails.getDescription());
            updatedProduct.setPrice(productDetails.getPrice());
            updatedProduct.setCategory(productDetails.getCategory());
            updatedProduct.setBrand(productDetails.getBrand());
            updatedProduct.setStockQuantity(productDetails.getStockQuantity());
            updatedProduct.setImageUrl(productDetails.getImageUrl());
            return ResponseEntity.ok(ecommerceProductRepository.save(updatedProduct));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        Optional<EcommerceProduct> product = ecommerceProductRepository.findById(String.valueOf(id));
        if (product.isPresent()) {
            ecommerceProductRepository.delete(product.get());
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}