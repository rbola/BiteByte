import React, { useEffect, useState } from 'react';
import axios from 'axios';

const LiveCookingClasses = () => {
  const [classes, setClasses] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    axios.get('/api/live-cooking-classes')
      .then(response => {
        setClasses(response.data);
        setError(null);
      })
      .catch(error => {
        console.error('There was an error fetching the live cooking classes!', error);
        if (error.response && error.response.status === 401) {
          setError('Your session has expired. Please log in again.');
        } else {
          setError('An error occurred while fetching live cooking classes. Please try again later.');
        }
      })
      .finally(() => setLoading(false));
  }, []);

  return (
    <div>
      <h2>Live Cooking Classes</h2>
      {loading && <p>Loading classes...</p>}
      {error && <div className="error-message">{error}</div>}
      {!loading && !error && classes.length === 0 && (
        <p>No live cooking classes available.</p>
      )}
      {!loading && !error && classes.length > 0 && (
        <ul>
          {classes.map(liveClass => (
            <li key={liveClass.id}>
              <h3>{liveClass.title}</h3>
              <p>{liveClass.description}</p>
              <p>Start Time: {new Date(liveClass.startTime).toLocaleString()}</p>
              <p>End Time: {new Date(liveClass.endTime).toLocaleString()}</p>
              <p>Instructor: {liveClass.instructor.name}</p>
              <p>Zoom Link: <a href={liveClass.zoomLink} target="_blank" rel="noopener noreferrer">{liveClass.zoomLink}</a></p>
              <p>Max Participants: {liveClass.maxParticipants}</p>
              <p>Attendee Count: {liveClass.attendeeCount}</p>
            </li>
          ))}
        </ul>
      )}
    </div>
  );
};

export default LiveCookingClasses;