package com.main.bitebyte.recipe;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/recipes")
public class RecipeController {

    private static final Logger logger = LoggerFactory.getLogger(RecipeController.class);

    @Autowired
    private RecipeRepository recipeRepository;

    @GetMapping
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public List<Recipe> getAllRecipes() {
        logger.info("Fetching all recipes");
        return recipeRepository.findAll();
    }

    @GetMapping("/public")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public List<Recipe> getAllPublicRecipes() {
        logger.info("Fetching all public recipes");
        return recipeRepository.findAll(); // In a real-world scenario, you might want to filter only public recipes
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<Recipe> getRecipeById(@PathVariable String id) {
        logger.info("Fetching recipe with id: {}", id);
        Optional<Recipe> recipe = recipeRepository.findById(id);
        return recipe.map(ResponseEntity::ok).orElseGet(() -> {
            logger.warn("Recipe with id {} not found", id);
            return ResponseEntity.notFound().build();
        });
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Recipe createRecipe(@RequestBody Recipe recipe) {
       
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        logger.info("Creating new recipe. User: {}, Authorities: {}", auth.getName(), auth.getAuthorities());
        Recipe savedRecipe = recipeRepository.save(recipe);
        logger.info("Recipe created successfully with id: {}", savedRecipe.getId());
        return savedRecipe;
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Recipe> updateRecipe(@PathVariable String id, @RequestBody Recipe recipeDetails) {
        logger.info("Updating recipe with id: {}", id);
        Optional<Recipe> optionalRecipe = recipeRepository.findById(id);
        if (optionalRecipe.isPresent()) {
            Recipe recipe = optionalRecipe.get();
            recipe.setName(recipeDetails.getName());
            recipe.setDescription(recipeDetails.getDescription());
            recipe.setIngredients(recipeDetails.getIngredients());
            recipe.setInstructions(recipeDetails.getInstructions());
            Recipe updatedRecipe = recipeRepository.save(recipe);
            logger.info("Recipe updated successfully");
            return ResponseEntity.ok(updatedRecipe);
        } else {
            logger.warn("Recipe with id {} not found for update", id);
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> deleteRecipe(@PathVariable String id) {
        logger.info("Deleting recipe with id: {}", id);
        Optional<Recipe> recipe = recipeRepository.findById(id);
        if (recipe.isPresent()) {
            recipeRepository.deleteById(id);
            logger.info("Recipe deleted successfully");
            return ResponseEntity.ok().build();
        } else {
            logger.warn("Recipe with id {} not found for deletion", id);
            return ResponseEntity.notFound().build();
        }
    }
}