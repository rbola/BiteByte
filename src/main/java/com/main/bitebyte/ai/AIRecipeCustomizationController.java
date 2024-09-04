package com.main.bitebyte.ai;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.main.bitebyte.common.OpenAiProperties;
import com.main.bitebyte.recipe.Recipe;
import com.main.bitebyte.recipe.RecipeRepository;
import com.main.bitebyte.user.User;
import com.main.bitebyte.user.UserRepository;

@RestController
@RequestMapping("/api/ai-recipe-customization")
public class AIRecipeCustomizationController {

    private static final Logger logger = LoggerFactory.getLogger(AIRecipeCustomizationController.class);

    @Autowired
    private AIRecipeCustomizationRepository aiRecipeCustomizationRepository;

    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private UserRepository userRepository;

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

        return ResponseEntity.ok(Map.of(
            "id", savedCustomization.getId(),
            "customizedRecipe", savedCustomization.getCustomizationDetails()
        ));
    }

    @PostMapping("/save")
    public ResponseEntity<?> saveCustomizedRecipe(@RequestBody Map<String, String> request) {
        String customizationId = request.get("customizationId");
        String customizedRecipeText = request.get("customizedRecipe");
        String originalRecipeId = request.get("originalRecipeId");
        
        if (customizationId == null || customizedRecipeText == null || originalRecipeId == null) {
            return ResponseEntity.badRequest().body("Customization ID, customized recipe, and original recipe ID are required");
        }

        logger.info("Received save request for customization ID: {}", customizationId);
        logger.info("Customized recipe text: {}", customizedRecipeText);

        Optional<Recipe> originalRecipeOptional = recipeRepository.findById(originalRecipeId);
        if (!originalRecipeOptional.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        Recipe originalRecipe = originalRecipeOptional.get();

        // Create a new recipe based on the customization
        Recipe newRecipe = new Recipe();
        newRecipe.setName(originalRecipe.getName() + " (AI Customized)");
        newRecipe.setDescription("AI customized version of: " + originalRecipe.getDescription());
        
        // Parse the customization details to update ingredients and instructions
        String[] sections = customizedRecipeText.split("(?m)^(?=Ingredients:|Instructions:)");
        logger.info("Number of sections found: {}", sections.length);
        for (String section : sections) {
            if (section.trim().startsWith("Ingredients:")) {
                List<String> ingredients = new ArrayList<>(Arrays.asList(section.substring("Ingredients:".length()).trim().split("\n")));
                ingredients.removeIf(String::isEmpty);
                newRecipe.setIngredients(ingredients.toArray(new String[0]));
                logger.info("Parsed ingredients: {}", ingredients);
            } else if (section.trim().startsWith("Instructions:")) {
                List<String> instructions = new ArrayList<>(Arrays.asList(section.substring("Instructions:".length()).trim().split("\n")));
                instructions.removeIf(String::isEmpty);
                newRecipe.setInstructions(instructions.toArray(new String[0]));
                logger.info("Parsed instructions: {}", instructions);
            }
        }

        // Copy other properties from the original recipe
        newRecipe.setTags(originalRecipe.getTags());
        newRecipe.setDifficulty(originalRecipe.getDifficulty());
        newRecipe.setPreparationTime(originalRecipe.getPreparationTime());
        newRecipe.setCookingTime(originalRecipe.getCookingTime());
        newRecipe.setServings(originalRecipe.getServings());
        newRecipe.setNutritionalInfo(originalRecipe.getNutritionalInfo());

        // Associate the recipe with the current user
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        Optional<User> currentUserOptional = userRepository.findByUsername(username);
        if (!currentUserOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not found");
        }
        User currentUser = currentUserOptional.get();
        newRecipe.setUser(currentUser);
        newRecipe.setPersonal(true);

        // Ensure ingredients and instructions are set
        newRecipe.setIngredients(originalRecipe.getIngredients());
        newRecipe.setInstructions(originalRecipe.getInstructions());

        Recipe savedRecipe = recipeRepository.save(newRecipe);
        logger.info("Saved customized recipe: {}", savedRecipe);

        return ResponseEntity.ok(Map.of("savedRecipe", savedRecipe));
    }

    private String customizeRecipeWithAI(Recipe recipe, String customizationPrompt) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(openAiProperties.getApiKey());

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", openAiProperties.getModel());
        requestBody.put("messages", List.of(
            Map.of("role", "system", "content", "You are a culinary AI assistant. Your task is to improve and customize recipes based on user prompts. Always provide the full recipe with Ingredients and Instructions sections."),
            Map.of("role", "user", "content", "Please customize the following recipe: " + recipe.getName() + 
                "\n\nIngredients:\n" + String.join("\n", recipe.getIngredients()) + 
                "\n\nInstructions:\n" + String.join("\n", recipe.getInstructions()) + 
                "\n\nCustomization request: " + (customizationPrompt != null ? customizationPrompt : "Improve the recipe") + 
                "\n\nPlease provide the full customized recipe with Ingredients and Instructions sections.")
        ));

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(openAiProperties.getEndpoint(), entity, Map.class);
            List<Map<String, Object>> choices = (List<Map<String, Object>>) response.getBody().get("choices");
            Map<String, String> message = (Map<String, String>) choices.get(0).get("message");
            String content = message.get("content");
            logger.info("AI response: {}", content);
            return content;
        } catch (Exception e) {
            logger.error("Error occurred while customizing the recipe", e);
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

    @GetMapping("/recipes")
    public List<Recipe> getUserRecipes(@AuthenticationPrincipal User currentUser) {
        return recipeRepository.findPublicAndPersonal(currentUser);
    }
}