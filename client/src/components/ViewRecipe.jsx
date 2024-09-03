import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { useParams, useNavigate } from 'react-router-dom';
import './ViewRecipe.css';

function ViewRecipe() {
  const [recipe, setRecipe] = useState(null);
  const [customizationPrompt, setCustomizationPrompt] = useState('');
  const [customizedRecipe, setCustomizedRecipe] = useState('');
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const { id } = useParams();
  const navigate = useNavigate();

  useEffect(() => {
    fetchRecipe();
  }, [id]);

  const fetchRecipe = async () => {
    try {
      const response = await axios.get(`/api/recipes/${id}`);
      setRecipe(response.data);
    } catch (error) {
      console.error('Error fetching recipe:', error);
      setError('Failed to fetch recipe. Please try again.');
    }
  };

  const handleCustomization = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError('');
    setCustomizedRecipe('');
    try {
      const response = await axios.post('/api/ai-recipe-customization', {
        recipeId: id,
        customizationPrompt: customizationPrompt
      });
      if (response.data && response.data.customizedRecipe) {
        setCustomizedRecipe(response.data.customizedRecipe);
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

  if (!recipe) {
    return <div>Loading...</div>;
  }

  const renderInstructions = (instructions) => {
    if (!instructions) {
      return 'No instructions available.';
    }
    if (Array.isArray(instructions)) {
      return instructions.join('\n');
    }
    if (typeof instructions === 'string') {
      return instructions;
    }
    return JSON.stringify(instructions);
  };

  return (
    <div className="view-recipe-container">
      <h2>{recipe.name}</h2>
      <p><strong>Ingredients:</strong></p>
      <pre className="recipe-text">{recipe.ingredients}</pre>
      <p><strong>Instructions:</strong></p>
      <pre className="recipe-text">{renderInstructions(recipe.instructions)}</pre>

      <div className="ai-customization-section">
        <h3>AI Recipe Customization</h3>
        <form onSubmit={handleCustomization}>
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
          <button type="submit" disabled={loading || !customizationPrompt}>
            {loading ? 'Customizing...' : 'Customize Recipe'}
          </button>
        </form>
        {error && <div className="error-message">{error}</div>}
        {customizedRecipe && (
          <div className="customized-recipe">
            <h4>Customized Recipe:</h4>
            <pre className="recipe-text">{renderInstructions(customizedRecipe)}</pre>
          </div>
        )}
      </div>

      <button onClick={() => navigate('/recipes')}>Back to Recipes</button>
    </div>
  );
}

export default ViewRecipe;