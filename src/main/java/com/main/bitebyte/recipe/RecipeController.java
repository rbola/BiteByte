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

import com.main.bitebyte.user.Role;
import com.main.bitebyte.user.User;
import com.main.bitebyte.user.UserRepository; // Ensure this import is present

@RestController
@RequestMapping("/api/recipes")
public class RecipeController {

    private static final Logger logger = LoggerFactory.getLogger(RecipeController.class);

    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/all")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public List<Recipe> getAllRecipes() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Optional<User> user = userRepository.findByUsername(authentication.getName());

        if (user.isPresent() && user.get().getRoles().stream().anyMatch(role -> role == Role.ADMIN)) {
            logger.info("Fetching all recipes for admin");
            return recipeRepository.findAll();
        } else {
            logger.info("Fetching public recipes and personal recipes for the current user");
            return recipeRepository.findPublicAndPersonal(user.get());
        }
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
    @PreAuthorize("hasRole('ROLE_ADMIN')")
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