import React from 'react';
import { Link } from 'react-router-dom';
import './UserHome.css';

const UserHome = ({ userRole }) => {
  return (
    <div>
      <div className="hero">
        <h1>Welcome to BiteByte</h1>
        <p>Your ultimate recipe and cooking class platform</p>
      </div>
      <div className="options">
        <Link to="/recipes">View Recipes</Link>
        <Link to="/live-classes">Live Cooking Classes</Link>
        <Link to="/ai-customization">AI Recipe Customization</Link>
        {userRole.includes('ADMIN') && (
          <>
            <Link to="/add-recipe">Add Recipe</Link>
            <Link to="/add-live-class">Add Live Cooking Class</Link>
          </>
        )}
      </div>
    </div>
  );
};

export default UserHome;