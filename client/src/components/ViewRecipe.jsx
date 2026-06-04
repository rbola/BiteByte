import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { useParams, useNavigate } from 'react-router-dom';
import './ViewRecipe.css';

function ViewRecipe() {
  const [recipe, setRecipe] = useState(null);
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

  return (
    <div className="view-recipe-container">
      {error && <div className="error-message">{error}</div>}
      {recipe ? (
        <>
          <h2>{recipe.name}</h2>
          <p>{recipe.description}</p>
          {recipe.tags && recipe.tags.length > 0 && (
            <p><strong>Tags:</strong> {recipe.tags.join(', ')}</p>
          )}
          <p><strong>Difficulty:</strong> {recipe.difficulty}/5</p>
          <p><strong>Total time:</strong> {recipe.preparationTime}h &nbsp;|&nbsp; <strong>Cook time:</strong> {recipe.cookingTime} min &nbsp;|&nbsp; <strong>Serves:</strong> {recipe.servings}</p>
          <h3>Ingredients</h3>
          <ul>
            {recipe.ingredients && recipe.ingredients.map((ing, i) => <li key={i}>{ing}</li>)}
          </ul>
          <h3>Instructions</h3>
          <ol>
            {recipe.instructions && recipe.instructions.map((step, i) => <li key={i}>{step}</li>)}
          </ol>
          {recipe.nutritionalInfo && <p><strong>Nutrition:</strong> {recipe.nutritionalInfo}</p>}
        </>
      ) : (
        !error && <p>Loading...</p>
      )}
      <button onClick={() => navigate('/recipes')}>Back to Recipes</button>
    </div>
  );
}

export default ViewRecipe;