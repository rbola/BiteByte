import React, { useState } from 'react';
import axios from 'axios';

const Auth = ({ setIsAuthenticated, setUserRole }) => {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [isLogin, setIsLogin] = useState(true);

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (isLogin) {
      handleSignIn();
    } else {
      handleSignUp();
    }
  };

  const handleSignIn = async () => {
    try {
      console.log('Attempting to sign in');
      const response = await axios.post('/api/auth/signin', {
        username: username,
        password: password
      });
      console.log('Sign in response:', response.data);
      if (response.data && response.data.token && response.data.roles) {
        localStorage.setItem('token', response.data.token);
        localStorage.setItem('userRole', response.data.roles[0]);
        setIsAuthenticated(true);
        setUserRole(response.data.roles[0]);
      } else {
        console.error('Invalid response format:', response.data);
      }
    } catch (error) {
      console.error('Error signing in:', error.response || error);
      if (error.response) {
        console.error('Error status:', error.response.status);
        console.error('Error data:', error.response.data);
      }
      // You might want to show an error message to the user here
    }
  };

  const handleSignUp = async () => {
    try {
      console.log('Attempting to sign up');
      const response = await axios.post('/api/auth/signup', {
        username: username,
        password: password
      });
      console.log('Sign up response:', response.data);
      // After successful signup, you might want to automatically sign in the user
      // or just show a message asking them to login
    } catch (error) {
      console.error('Error signing up:', error.response ? error.response.data : error.message);
    }
  };

  return (
    <div>
      <h2>{isLogin ? 'Login' : 'Sign Up'}</h2>
      <form onSubmit={handleSubmit}>
        <input
          type="text"
          placeholder="Username"
          value={username}
          onChange={(e) => setUsername(e.target.value)}
        />
        <input
          type="password"
          placeholder="Password"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
        />
        <button type="submit">{isLogin ? 'Login' : 'Sign Up'}</button>
      </form>
      <button onClick={() => setIsLogin(!isLogin)}>
        {isLogin ? 'Need an account? Sign Up' : 'Already have an account? Login'}
      </button>
    </div>
  );
};

export default Auth;