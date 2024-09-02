import React, { useEffect, useState } from 'react';
import axios from 'axios';

const LiveCookingClasses = () => {
  const [classes, setClasses] = useState([]);

  useEffect(() => {
    axios.get('/api/live-classes')
      .then(response => {
        setClasses(response.data);
      })
      .catch(error => {
        console.error('There was an error fetching the live cooking classes!', error);
      });
  }, []);

  return (
    <div>
      <h2>Live Cooking Classes</h2>
      {classes.length === 0 ? (
        <p>No live cooking classes available.</p>
      ) : (
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