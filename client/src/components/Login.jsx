import React, { useState } from 'react';
import axios from 'axios';
import { useHistory } from 'react-router-dom';

const Login = ({ setIsAuthenticated, setUserRole }) => {
  const [isSignUp, setIsSignUp] = useState(false);
  const [form, setForm] = useState({ username: '', password: '', email: '' });
  const history = useHistory();

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const endpoint = isSignUp ? '/api/auth/signup' : '/api/auth/signin';
      const response = await axios.post(endpoint, form);
      
      if (isSignUp) {
        alert('Sign up successful! Please sign in.');
        setIsSignUp(false);
      } else {
        const { token, roles } = response.data;
        localStorage.setItem('token', token);
        localStorage.setItem('userRole', roles[0]);
        axios.defaults.headers.common['Authorization'] = `Bearer ${token}`;
        setIsAuthenticated(true);
        setUserRole(roles[0]);
        history.push('/');
      }
    } catch (error) {
      console.error('Error:', error.response || error);
      alert(isSignUp ? 'Error signing up. Please try again.' : 'Error signing in. Please check your credentials.');
    }
  };

  return (
    <div>
      <h2>{isSignUp ? 'Sign Up' : 'Sign In'}</h2>
      <form onSubmit={handleSubmit}>
        <input
          type="text"
          value={form.username}
          onChange={(e) => setForm({ ...form, username: e.target.value })}
          placeholder="Username"
          required
        />
        <input
          type="password"
          value={form.password}
          onChange={(e) => setForm({ ...form, password: e.target.value })}
          placeholder="Password"
          required
        />
        {isSignUp && (
          <input
            type="email"
            value={form.email}
            onChange={(e) => setForm({ ...form, email: e.target.value })}
            placeholder="Email"
            required
          />
        )}
        <button type="submit">{isSignUp ? 'Sign Up' : 'Sign In'}</button>
      </form>
      <button onClick={() => setIsSignUp(!isSignUp)}>
        {isSignUp ? 'Switch to Sign In' : 'Switch to Sign Up'}
      </button>
    </div>
  );
};

export default Login;