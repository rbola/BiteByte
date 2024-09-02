package com.main.bitebyte.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.main.bitebyte.model.Recipe;
import org.springframework.stereotype.Repository;

@Repository
public interface RecipeRepository extends MongoRepository<Recipe, String> {
    // Custom query methods if any
}