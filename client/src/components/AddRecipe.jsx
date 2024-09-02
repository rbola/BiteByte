import React, { useState } from 'react';
import axios from 'axios';
import './AddRecipe.css'; // Make sure to create this CSS file

function AddRecipe() {
  const [recipeForm, setRecipeForm] = useState({
    name: '',
    description: '',
    ingredients: '',
    instructions: '',
    tags: '',
    difficulty: 1,
    preparationTime: 0,
    cookingTime: 0,
    servings: 1,
    nutritionalInfo: ''
  });

  const handleRecipeSubmit = async (e) => {
    e.preventDefault();
    try {
      const token = localStorage.getItem('token');
      await axios.post('/api/recipes', {
        ...recipeForm,
        ingredients: recipeForm.ingredients.split(','),
        instructions: recipeForm.instructions.split(','),
        tags: recipeForm.tags.split(',')
      }, {
        headers: {
          'Authorization': `Bearer ${token}`
        }
      });
      alert('Recipe added successfully!');
      setRecipeForm({
        name: '',
        description: '',
        ingredients: '',
        instructions: '',
        tags: '',
        difficulty: 1,
        preparationTime: 0,
        cookingTime: 0,
        servings: 1,
        nutritionalInfo: ''
      });
    } catch (error) {
      console.error('Error adding recipe:', error.response || error);
      if (error.response && error.response.status === 401) {
        alert('You are not authorized to add recipes. Please log in again.');
      } else if (error.response && error.response.status === 403) {
        alert('You do not have permission to add recipes.');
      } else {
        alert('Error adding recipe. Please try again.');
      }
    }
  };

  return (
    <div className="add-recipe-container">
      <h2>Add New Recipe</h2>
      <form onSubmit={handleRecipeSubmit} className="add-recipe-form">
        <div className="form-group">
          <label htmlFor="name">Recipe Name:</label>
          <input
            id="name"
            type="text"
            value={recipeForm.name}
            onChange={(e) => setRecipeForm({ ...recipeForm, name: e.target.value })}
            required
          />
        </div>

        <div className="form-group">
          <label htmlFor="description">Description:</label>
          <textarea
            id="description"
            value={recipeForm.description}
            onChange={(e) => setRecipeForm({ ...recipeForm, description: e.target.value })}
            required
          />
        </div>

        <div className="form-group">
          <label htmlFor="ingredients">Ingredients (comma-separated):</label>
          <textarea
            id="ingredients"
            value={recipeForm.ingredients}
            onChange={(e) => setRecipeForm({ ...recipeForm, ingredients: e.target.value })}
            required
          />
        </div>

        <div className="form-group">
          <label htmlFor="instructions">Instructions (comma-separated):</label>
          <textarea
            id="instructions"
            value={recipeForm.instructions}
            onChange={(e) => setRecipeForm({ ...recipeForm, instructions: e.target.value })}
            required
          />
        </div>

        <div className="form-group">
          <label htmlFor="tags">Tags (comma-separated):</label>
          <input
            id="tags"
            type="text"
            value={recipeForm.tags}
            onChange={(e) => setRecipeForm({ ...recipeForm, tags: e.target.value })}
          />
        </div>

        <div className="form-group">
          <label htmlFor="difficulty">Difficulty (1-5):</label>
          <input
            id="difficulty"
            type="number"
            value={recipeForm.difficulty}
            onChange={(e) => setRecipeForm({ ...recipeForm, difficulty: parseInt(e.target.value) })}
            min="1"
            max="5"
            required
          />
        </div>

        <div className="form-group">
          <label htmlFor="preparationTime">Preparation Time (minutes):</label>
          <input
            id="preparationTime"
            type="number"
            value={recipeForm.preparationTime}
            onChange={(e) => setRecipeForm({ ...recipeForm, preparationTime: parseInt(e.target.value) })}
            min="0"
            required
          />
        </div>

        <div className="form-group">
          <label htmlFor="cookingTime">Cooking Time (minutes):</label>
          <input
            id="cookingTime"
            type="number"
            value={recipeForm.cookingTime}
            onChange={(e) => setRecipeForm({ ...recipeForm, cookingTime: parseInt(e.target.value) })}
            min="0"
            required
          />
        </div>

        <div className="form-group">
          <label htmlFor="servings">Servings:</label>
          <input
            id="servings"
            type="number"
            value={recipeForm.servings}
            onChange={(e) => setRecipeForm({ ...recipeForm, servings: parseInt(e.target.value) })}
            min="1"
            required
          />
        </div>

        <div className="form-group">
          <label htmlFor="nutritionalInfo">Nutritional Info:</label>
          <textarea
            id="nutritionalInfo"
            value={recipeForm.nutritionalInfo}
            onChange={(e) => setRecipeForm({ ...recipeForm, nutritionalInfo: e.target.value })}
          />
        </div>

        <button type="submit" className="submit-button">Add Recipe</button>
      </form>
    </div>
  );
}

export default AddRecipe;