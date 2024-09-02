package com.main.bitebyte.controller;

import com.main.bitebyte.model.AIRecipeCustomization;
import com.main.bitebyte.model.Recipe;
import com.main.bitebyte.repository.AIRecipeCustomizationRepository;
import com.main.bitebyte.repository.RecipeRepository;
import com.main.bitebyte.config.OpenAiProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.HashMap;

@RestController
@RequestMapping("/api/ai-recipe-customization")
public class AIRecipeCustomizationController {

    @Autowired
    private AIRecipeCustomizationRepository aiRecipeCustomizationRepository;

    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private OpenAiProperties openAiProperties;

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping
    public List<AIRecipeCustomization> getAllCustomizations() {
        return aiRecipeCustomizationRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<AIRecipeCustomization> getCustomizationById(@PathVariable String id) {
        Optional<AIRecipeCustomization> customization = aiRecipeCustomizationRepository.findById(id);
        return customization.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> createCustomization(@RequestBody Map<String, String> request) {
        String recipeId = request.get("recipeId");
        String customizationPrompt = request.get("customizationPrompt");
        
        if (recipeId == null) {
            return ResponseEntity.badRequest().body("Recipe ID is required");
        }

        Optional<Recipe> recipeOptional = recipeRepository.findById(recipeId);
        if (!recipeOptional.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        Recipe recipe = recipeOptional.get();
        
        String customizedRecipe = customizeRecipeWithAI(recipe, customizationPrompt);

        AIRecipeCustomization customization = new AIRecipeCustomization();
        customization.setRecipe(recipe);
        customization.setCustomizationDetails(customizedRecipe);
        
        AIRecipeCustomization savedCustomization = aiRecipeCustomizationRepository.save(customization);

        return ResponseEntity.ok(Map.of("customizedRecipe", savedCustomization.getCustomizationDetails()));
    }

    private String customizeRecipeWithAI(Recipe recipe, String customizationPrompt) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(openAiProperties.getApiKey());

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", openAiProperties.getModel());
        requestBody.put("messages", List.of(
            Map.of("role", "system", "content", "You are a culinary AI assistant. Your task is to improve and customize recipes based on user prompts."),
            Map.of("role", "user", "content", "Please customize the following recipe: " + recipe.getName() + 
                "\n\nIngredients:\n" + recipe.getIngredients() + 
                "\n\nInstructions:\n" + recipe.getInstructions() + 
                "\n\nCustomization request: " + (customizationPrompt != null ? customizationPrompt : "Improve the recipe"))
        ));

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(openAiProperties.getEndpoint(), entity, Map.class);
            List<Map<String, Object>> choices = (List<Map<String, Object>>) response.getBody().get("choices");
            Map<String, String> message = (Map<String, String>) choices.get(0).get("message");
            return message.get("content");
        } catch (Exception e) {
            e.printStackTrace();
            return "Error occurred while customizing the recipe: " + e.getMessage();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<AIRecipeCustomization> updateCustomization(@PathVariable String id, @RequestBody AIRecipeCustomization customizationDetails) {
        Optional<AIRecipeCustomization> customization = aiRecipeCustomizationRepository.findById(id);
        if (customization.isPresent()) {
            AIRecipeCustomization updatedCustomization = customization.get();
            updatedCustomization.setRecipe(customizationDetails.getRecipe());
            updatedCustomization.setCustomizationDetails(customizationDetails.getCustomizationDetails());
            updatedCustomization.setUserFeedback(customizationDetails.getUserFeedback());
            updatedCustomization.setIngredientSubstitutions(customizationDetails.getIngredientSubstitutions());
            return ResponseEntity.ok(aiRecipeCustomizationRepository.save(updatedCustomization));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomization(@PathVariable String id) {
        Optional<AIRecipeCustomization> customization = aiRecipeCustomizationRepository.findById(id);
        if (customization.isPresent()) {
            aiRecipeCustomizationRepository.delete(customization.get());
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}