import React, { useEffect, useState } from 'react';
import axios from 'axios';
import './AIRecipeCustomization.css';

function AIRecipeCustomization() {
  const [recipes, setRecipes] = useState([]);
  const [selectedRecipeId, setSelectedRecipeId] = useState('');
  const [customizationPrompt, setCustomizationPrompt] = useState('');
  const [customizedRecipe, setCustomizedRecipe] = useState('');
  const [customizationId, setCustomizationId] = useState('');
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const [saveSuccess, setSaveSuccess] = useState(false);

  useEffect(() => {
    fetchRecipes();
  }, []);

  const fetchRecipes = async () => {
    try {
      const response = await axios.get('/api/recipes');
      setRecipes(response.data);
    } catch (error) {
      console.error('Error fetching recipes:', error);
      setError('Failed to fetch recipes. Please try again.');
    }
  };

  const handleCustomization = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError('');
    setCustomizedRecipe('');
    setSaveSuccess(false);
    try {
      const response = await axios.post('/api/ai-recipe-customization', { 
        recipeId: selectedRecipeId,
        customizationPrompt: customizationPrompt
      });
      if (response.data && response.data.customizedRecipe) {
        setCustomizedRecipe(response.data.customizedRecipe);
        setCustomizationId(response.data.id);
      } else {
        throw new Error('Customized recipe not received from server');
      }
    } catch (error) {
      console.error('Error customizing recipe:', error.response || error);
      setError('Error customizing recipe. Please try again.');
    } finally {
      setLoading(false);
    }
  };

  const handleSaveCustomizedRecipe = async () => {
    setLoading(true);
    setError('');
    setSaveSuccess(false);
    try {
      const response = await axios.post('/api/ai-recipe-customization/save', {
        customizationId: customizationId,
        customizedRecipe: customizedRecipe,
        originalRecipeId: selectedRecipeId
      });
      if (response.data && response.data.savedRecipe) {
        setSaveSuccess(true);
        // Refresh the recipe list after saving
        fetchRecipes();
      } else {
        throw new Error('Failed to save customized recipe');
      }
    } catch (error) {
      console.error('Error saving customized recipe:', error.response || error);
      setError('Error saving customized recipe. Please try again.');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="ai-customization-container">
      <h2>AI Recipe Customization</h2>
      {error && <div className="error-message">{error}</div>}
      {saveSuccess && <div className="success-message">Customized recipe saved successfully!</div>}
      <form onSubmit={handleCustomization} className="ai-customization-form">
        <div className="form-group">
          <label htmlFor="recipeSelect">Select a recipe:</label>
          <select
            id="recipeSelect"
            value={selectedRecipeId}
            onChange={(e) => setSelectedRecipeId(e.target.value)}
            required
          >
            <option value="">--Select a recipe--</option>
            {recipes.map(recipe => (
              <option key={recipe.id} value={recipe.id}>
                {recipe.name}
              </option>
            ))}
          </select>
        </div>
        <div className="form-group">
          <label htmlFor="customizationPrompt">Customization Prompt:</label>
          <input
            type="text"
            id="customizationPrompt"
            value={customizationPrompt}
            onChange={(e) => setCustomizationPrompt(e.target.value)}
            placeholder="e.g., Make it vegan, reduce calories, etc."
          />
        </div>
        <button type="submit" className="submit-button" disabled={loading || !selectedRecipeId}>
          {loading ? 'Customizing...' : 'Customize Recipe'}
        </button>
      </form>
      {customizedRecipe && (
        <div className="customized-recipe">
          <h3>Customized Recipe:</h3>
          <pre>{customizedRecipe}</pre>
          <button 
            onClick={handleSaveCustomizedRecipe} 
            className="save-button" 
            disabled={loading}
          >
            {loading ? 'Saving...' : 'Save Customized Recipe'}
          </button>
        </div>
      )}
    </div>
  );
}

export default AIRecipeCustomization;