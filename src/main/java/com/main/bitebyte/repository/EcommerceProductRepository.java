package com.main.bitebyte.repository;

import com.main.bitebyte.model.EcommerceProduct;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EcommerceProductRepository extends MongoRepository<EcommerceProduct, String> {
    // Add custom query methods if needed
}