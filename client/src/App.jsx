import React, { useState, useEffect } from 'react';
import { BrowserRouter as Router, Route, Routes, Navigate } from 'react-router-dom';
import axios from 'axios';
import './App.css';

import Navigation from './components/Navigation';
import UserHome from './components/UserHome';
import AddRecipe from './components/AddRecipe';
import AddLiveCookingClass from './components/AddLiveCookingClass';
import AIRecipeCustomization from './components/AIRecipeCustomization';
import RecipeList from './components/RecipeList';
import LiveCookingClasses from './components/LiveCookingClasses';
import ViewRecipe from './components/ViewRecipe';

axios.defaults.baseURL = 'http://localhost:8080';

function App() {
  const [isAuthenticated, setIsAuthenticated] = useState(false);
  const [userRole, setUserRole] = useState('');
  const [isSignUp, setIsSignUp] = useState(false);
  const [signUpForm, setSignUpForm] = useState({ username: '', password: '', email: '' });
  const [signInForm, setSignInForm] = useState({ username: '', password: '' });
  const [isDarkTheme, setIsDarkTheme] = useState(false);

  useEffect(() => {
    const token = localStorage.getItem('token');
    const role = localStorage.getItem('userRole');
    if (token && role) {
      setIsAuthenticated(true);
      setUserRole(role);
      axios.defaults.headers.common['Authorization'] = `Bearer ${token}`;
    }
    const savedTheme = localStorage.getItem('theme');
    setIsDarkTheme(savedTheme === 'dark');
  }, []);

  const handleSignInSubmit = async (e) => {
    e.preventDefault();
    try {
      const response = await axios.post('/api/auth/signin', signInForm);
      const token = response.data.token;
      const role = response.data.roles[0]; // Assuming the first role is the main role
      localStorage.setItem('token', token);
      localStorage.setItem('userRole', role);
      axios.defaults.headers.common['Authorization'] = `Bearer ${token}`;
      setIsAuthenticated(true);
      setUserRole(role);
    } catch (error) {
      console.error('Error signing in:', error.response || error);
      alert('Error signing in. Please check your credentials and try again.');
    }
  };

  const handleSignUpSubmit = async (e) => {
    e.preventDefault();
    try {
      await axios.post('/api/auth/signup', signUpForm);
      alert('Sign up successful! Please sign in.');
      setIsSignUp(false);
    } catch (error) {
      console.error('Error signing up:', error.response || error);
      alert('Error signing up. Please try again.');
    }
  };

  const handleLogout = () => {
    localStorage.removeItem('token');
    localStorage.removeItem('userRole');
    delete axios.defaults.headers.common['Authorization'];
    setIsAuthenticated(false);
    setUserRole('');
  };

  const toggleTheme = () => {
    const newTheme = !isDarkTheme;
    setIsDarkTheme(newTheme);
    localStorage.setItem('theme', newTheme ? 'dark' : 'light');
  };

  useEffect(() => {
    document.body.className = isDarkTheme ? 'dark-theme' : 'light-theme';
  }, [isDarkTheme]);

  if (!isAuthenticated) {
    return (
      <>
        <div className="login-container">
          <img src="/logo.png" alt="BiteByte Logo" className="logo" />
        </div>
        <div className={isDarkTheme ? 'dark-theme' : 'light-theme'}>
          {isSignUp ? (
            <form onSubmit={handleSignUpSubmit}>
              <div className="form-group">
                <label htmlFor="username">Username</label>
                <input
                  type="text"
                  id="username"
                  value={signUpForm.username}
                  onChange={(e) => setSignUpForm({ ...signUpForm, username: e.target.value })}
                  placeholder="Username"
                  required
                />
              </div>
              <div className="form-group">
                <label htmlFor="password">Password</label>
                <input
                  type="password"
                  id="password"
                  value={signUpForm.password}
                  onChange={(e) => setSignUpForm({ ...signUpForm, password: e.target.value })}
                  placeholder="Password"
                  required
                />
              </div>
              <div className="form-group">
                <label htmlFor="email">Email</label>
                <input
                  type="email"
                  id="email"
                  value={signUpForm.email}
                  onChange={(e) => setSignUpForm({ ...signUpForm, email: e.target.value })}
                  placeholder="Email"
                  required
                />
              </div>
              <button type="submit">Sign Up</button>
            </form>
          ) : (
            <form onSubmit={handleSignInSubmit}>
              <div className="form-group">
                <label htmlFor="username">Username</label>
                <input
                  type="text"
                  id="username"
                  value={signInForm.username}
                  onChange={(e) => setSignInForm({ ...signInForm, username: e.target.value })}
                  placeholder="Username"
                  required
                />
              </div>
              <div className="form-group">
                <label htmlFor="password">Password</label>
                <input
                  type="password"
                  id="password"
                  value={signInForm.password}
                  onChange={(e) => setSignInForm({ ...signInForm, password: e.target.value })}
                  placeholder="Password"
                  required
                />
              </div>
              <button type="submit">Sign In</button>
            </form>
          )}
          <div className="switch-links">
            <a className="switch-link" onClick={() => setIsSignUp(!isSignUp)}>
              {isSignUp ? "Switch to Sign In" : "Switch to Sign Up"}
            </a>
            <a className="switch-link" onClick={toggleTheme}>
              Switch to {isDarkTheme ? 'Light' : 'Dark'} Theme
            </a>
          </div>
        </div>
      </>
    );
  }

  return (
    <Router>
      <div className={isDarkTheme ? 'dark-theme' : 'light-theme'}>
        <Navigation onLogout={handleLogout} onToggleTheme={toggleTheme} />
        <Routes>
          <Route path="/" element={<UserHome userRole={userRole} />} />
          <Route path="/recipes" element={<RecipeList />} />
          <Route path="/recipes/:id" element={<ViewRecipe />} />
          <Route path="/live-classes" element={<LiveCookingClasses />} />
          <Route path="/ai-customization" element={<AIRecipeCustomization />} />
          {userRole.includes('ADMIN') && (
            <>
              <Route path="/add-recipe" element={<AddRecipe />} />
              <Route path="/add-live-class" element={<AddLiveCookingClass />} />
            </>
          )}
          <Route path="*" element={<Navigate to="/" replace />} />
        </Routes>
      </div>
    </Router>
  );
}

export default App;