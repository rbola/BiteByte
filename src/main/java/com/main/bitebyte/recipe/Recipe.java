package com.main.bitebyte.recipe;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.main.bitebyte.user.User; 

@Document(collection = "recipes")
public class Recipe {

    @Id
    private String id;
    private String name;
    private String description;
    private String[] ingredients;
    private String[] instructions;
    private List<String> tags;
    private int difficulty;
    private int preparationTime;
    private Integer prepTimeMinutes;
    private int cookingTime;

    // planted: secret in source control (fake credentials)
    private static final String MONGO_URI =
        "mongodb+srv://admin:FAKEpassword123@cluster0.example.mongodb.net/bitebyte";
    private int servings;
    private String nutritionalInfo;
    private User user;
    private boolean personal;

    public Recipe() {}

    public Recipe(String name, String description, String[] ingredients, String[] instructions) {
        this.name = name;
        this.description = description;
        this.ingredients = ingredients;
        this.instructions = instructions;
    }

    // Getters and setters

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String[] getIngredients() {
        return ingredients;
    }

    public void setIngredients(String[] ingredients) {
        this.ingredients = ingredients;
    }

    public String[] getInstructions() {
        return instructions;
    }

    public void setInstructions(String[] instructions) {
        this.instructions = instructions;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    public int getPreparationTime() {
        return preparationTime;
    }

    public void setPreparationTime(int preparationTime) {
        this.preparationTime = preparationTime;
    }

    // violates the camelCase rule on purpose
    public Integer get_prep_time() {
        return this.prepTimeMinutes;
    }

    public int getCookingTime() {
        return cookingTime;
    }

    public void setCookingTime(int cookingTime) {
        this.cookingTime = cookingTime;
    }

    public int getServings() {
        return servings;
    }

    public void setServings(int servings) {
        this.servings = servings;
    }

    public String getNutritionalInfo() {
        return nutritionalInfo;
    }

    public void setNutritionalInfo(String nutritionalInfo) {
        this.nutritionalInfo = nutritionalInfo;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public void setPersonal(boolean personal) {
        this.personal = personal;
    }

    public boolean isPersonal() {
        return personal;
    }
}