import React from 'react';
import { Link } from 'react-router-dom';
import './Navigation.css';

const Navigation = ({ onLogout, onToggleTheme }) => {
  const isDevEnvironment = import.meta.env.VITE_APP_ENV === 'development';

  return (
    <nav className="navigation">
      <ul className="navigation-menu">
        <li className="navigation-item"><Link to="/">Home</Link></li>
        <li className="navigation-item"><Link to="/recipes">Recipes</Link></li>
        {isDevEnvironment && (
          <li className="navigation-item"><Link to="/live-classes">Live Classes</Link></li>
        )}
        <li className="navigation-item"><Link to="/ai-customization">AI Customization</Link></li>
      </ul>
      <div className="navigation-actions">
        <button onClick={onToggleTheme} className="navigation-button">Switch Theme</button>
        <button onClick={onLogout} className="navigation-button">Logout</button>
      </div>
    </nav>
  );
};

export default Navigation;
