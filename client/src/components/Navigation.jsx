import React from 'react';
import './Navigation.css';

const Navigation = ({ onLogout, onToggleTheme }) => {
  const isDevEnvironment = import.meta.env.VITE_APP_ENV === 'development';

  return (
    <nav className="navigation">
      <ul className="navigation-menu">
        <li className="navigation-item"><a href="/">Home</a></li>
        <li className="navigation-item"><a href="/recipes">Recipes</a></li>
        {isDevEnvironment && (
          <li className="navigation-item"><a href="/live-classes">Live Classes</a></li>
        )}
        <li className="navigation-item"><a href="/ai-customization">AI Customization</a></li>
        {/* Add more navigation items here */}
      </ul>
      <div className="navigation-actions">
        <button onClick={onToggleTheme} className="navigation-button">Switch Theme</button>
        <button onClick={onLogout} className="navigation-button">Logout</button>
      </div>
    </nav>
  );
};

export default Navigation;
