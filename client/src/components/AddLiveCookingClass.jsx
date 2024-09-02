import React, { useState } from 'react';
import axios from 'axios';
import './AddLiveCookingClass.css';

function AddLiveCookingClass() {
  const [classForm, setClassForm] = useState({
    title: '',
    description: '',
    date: '',
    time: '',
    duration: 60,
    maxParticipants: 10,
    price: 0
  });

  const handleClassSubmit = async (e) => {
    e.preventDefault();
    try {
      await axios.post('/api/live-cooking-classes', classForm);
      alert('Live cooking class added successfully!');
      setClassForm({
        title: '',
        description: '',
        date: '',
        time: '',
        duration: 60,
        maxParticipants: 10,
        price: 0
      });
    } catch (error) {
      console.error('Error adding live cooking class:', error.response || error);
      alert('Error adding live cooking class. Please try again.');
    }
  };

  return (
    <div className="add-class-container">
      <h2>Add Live Cooking Class</h2>
      <form onSubmit={handleClassSubmit} className="add-class-form">
        <div className="form-group">
          <label htmlFor="title">Class Title:</label>
          <input
            id="title"
            type="text"
            value={classForm.title}
            onChange={(e) => setClassForm({ ...classForm, title: e.target.value })}
            required
          />
        </div>

        <div className="form-group">
          <label htmlFor="description">Class Description:</label>
          <textarea
            id="description"
            value={classForm.description}
            onChange={(e) => setClassForm({ ...classForm, description: e.target.value })}
            required
          />
        </div>

        <div className="form-group">
          <label htmlFor="date">Date:</label>
          <input
            id="date"
            type="date"
            value={classForm.date}
            onChange={(e) => setClassForm({ ...classForm, date: e.target.value })}
            required
          />
        </div>

        <div className="form-group">
          <label htmlFor="time">Time:</label>
          <input
            id="time"
            type="time"
            value={classForm.time}
            onChange={(e) => setClassForm({ ...classForm, time: e.target.value })}
            required
          />
        </div>

        <div className="form-group">
          <label htmlFor="duration">Duration (minutes):</label>
          <input
            id="duration"
            type="number"
            value={classForm.duration}
            onChange={(e) => setClassForm({ ...classForm, duration: parseInt(e.target.value) })}
            min="1"
            required
          />
        </div>

        <div className="form-group">
          <label htmlFor="maxParticipants">Max Participants:</label>
          <input
            id="maxParticipants"
            type="number"
            value={classForm.maxParticipants}
            onChange={(e) => setClassForm({ ...classForm, maxParticipants: parseInt(e.target.value) })}
            min="1"
            required
          />
        </div>

        <div className="form-group">
          <label htmlFor="price">Price:</label>
          <input
            id="price"
            type="number"
            value={classForm.price}
            onChange={(e) => setClassForm({ ...classForm, price: parseFloat(e.target.value) })}
            min="0"
            step="0.01"
            required
          />
        </div>

        <button type="submit" className="submit-button">Add Live Cooking Class</button>
      </form>
    </div>
  );
}

export default AddLiveCookingClass;