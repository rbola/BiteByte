import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import axios from 'axios';
import './RecipeList.css';

function RecipeList() {
  const [recipes, setRecipes] = useState([]);
  const [error, setError] = useState(null);

  useEffect(() => {
    fetchRecipes();
  }, []);

  const fetchRecipes = async () => {
    try {
      const response = await axios.get('/api/recipes/public');
      console.log('Recipes response:', response.data); // For debugging
      setRecipes(response.data);
      setError(null);
    } catch (error) {
      console.error('Error fetching recipes:', error.response || error);
      if (error.response && error.response.status === 401) {
        setError('Your session has expired. Please log in again.');
      } else if (error.response && error.response.status === 404) {
        setError('Recipe endpoint not found. Please check the API URL.');
      } else {
        setError('An error occurred while fetching recipes. Please try again later.');
      }
    }
  };

  return (
    <div className="recipe-list-container">
      <h2>Public Recipes</h2>
      {error && <div className="error-message">{error}</div>}
      {recipes.length === 0 && !error && <p>No public recipes found.</p>}
      <div className="recipe-grid">
        {recipes.map((recipe) => (
          <Link to={`/recipes/${recipe.id}`} key={recipe.id} className="recipe-card">
            <h3>{recipe.name}</h3>
            <p>{recipe.description ? recipe.description.substring(0, 100) + '...' : 'No description available'}</p>
          </Link>
        ))}
      </div>
    </div>
  );
}

export default RecipeList;