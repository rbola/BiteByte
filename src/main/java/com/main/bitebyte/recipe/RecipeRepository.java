package com.main.bitebyte.recipe;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.main.bitebyte.user.User;

@Repository
public interface RecipeRepository extends MongoRepository<Recipe, String> {
    // Custom query method using @Query annotation
    @Query("{ '$or': [ { 'user': null }, { 'user': ?0 } ] }")
    List<Recipe> findPublicAndPersonal(User user);
}