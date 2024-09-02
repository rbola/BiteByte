package com.main.bitebyte.repository;

import com.main.bitebyte.model.AIRecipeCustomization;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AIRecipeCustomizationRepository extends MongoRepository<AIRecipeCustomization, String> {
    // Add custom query methods if needed
}