package com.main.bitebyte.ai;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DBRef;

import com.main.bitebyte.recipe.Recipe; 
import com.main.bitebyte.livecooking.Instructor;

@Document(collection = "ai_recipe_customizations")
public class AIRecipeCustomization {

    @Id
    private String id;

    @DBRef
    private Recipe recipe;

    @DBRef
    private Instructor instructor;

    private String customizationDetails;
    private String userFeedback;
    private String ingredientSubstitutions;

    // Getters and setters

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Recipe getRecipe() {
        return recipe;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }

    public Instructor getInstructor() {
        return instructor;
    }

    public void setInstructor(Instructor instructor) {
        this.instructor = instructor;
    }

    public String getCustomizationDetails() {
        return customizationDetails;
    }

    public void setCustomizationDetails(String customizationDetails) {
        this.customizationDetails = customizationDetails;
    }

    public String getUserFeedback() {
        return userFeedback;
    }

    public void setUserFeedback(String userFeedback) {
        this.userFeedback = userFeedback;
    }

    public String getIngredientSubstitutions() {
        return ingredientSubstitutions;
    }

    public void setIngredientSubstitutions(String ingredientSubstitutions) {
        this.ingredientSubstitutions = ingredientSubstitutions;
    }
}