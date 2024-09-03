# BiteByte

BiteByte is a react web app powered by a Spring AI and MongoDB.

![Demo of the app](/demobitebyte.gif)

## Table of Contents

- [Features](#features)
- [Architecture](#architecture)
- [Installation](#installation)
- [Usage](#usage)
- [API Endpoints](#api-endpoints)
- [Contributing](#contributing)
- [License](#license)

## Features

- AI Recipe Customization using OpenAI
- Add and View Recipes
- Participate in Live Cooking Classes
- User Authentication and Authorization
- Dark and Light Theme Toggle

## Architecture

The project is divided into two main parts:

1. **Backend**: A modular monolithic Spring Boot application that handles the API and business logic. The backend is divided into the following modules:
    - **ai**: Handles AI-related functionalities using OpenAI.
    - **common**: Contains common utilities and configurations used across other modules.
    - **livecooking**: Manages live cooking classes.
    - **recipe**: Manages recipe-related functionalities.
    - **security**: Handles user authentication and authorization.
    - **user**: Manages user-related functionalities.
    - **util**: Contains utility classes and methods.

2. **Frontend**: A React application that provides the user interface.

### Technologies Used

- **Backend**:
  - Java (Amazon Corretto)
  - Spring Boot
  - Spring AI
  - MongoDB
  - OpenAI API

- **Frontend**:
  - React
  - Vite

## Installation

### Prerequisites

- [Node.js](https://nodejs.org/)
- [npm](https://www.npmjs.com/)
- [Amazon Corretto](https://aws.amazon.com/corretto/)
- [Gradle](https://gradle.org/)
- [MongoDB](https://www.mongodb.com/)

### Backend Setup

1. Clone the repository:
    ```sh
    git clone https://github.com/yourusername/BiteByte.git
    cd BiteByte
    ```

2. Navigate to the backend directory:
    ```sh
    cd src/main/java/com/main/bitebyte
    ```

3. Build the project using Gradle:
    ```sh
    ./gradlew build
    ```

4. Run the Spring Boot application:
    ```sh
    ./gradlew bootRun
    ```

5. Configure the application properties file:
    ```properties
    spring.data.mongodb.uri=mongodb+srv://<username>:<password>@<cluster>.umrcw.mongodb.net/?retryWrites=true&w=majority&appName=bitebyte

    spring.data.mongodb.database=bitebyte
    spring.data.mongodb.auto-index-creation=false

    spring.ai.openai.api-key= <OpenAI Key>
    spring.ai.openai.model=gpt-4o
    spring.ai.openai.urls.base=https://api.openai.com
    spring.ai.openai.urls.chat-completion=/v1/chat/completions

    # JWT Configuration
    jwt.secret= <JWT>
    jwt.expiration=86400000
    jwt.expiration.ms=3600000

    # Add this line
    app.jwtSecret=${jwt.secret}

    # Server configuration
    server.port=8080

    # Disable context path
    server.servlet.context-path=

    # Vector store configuration
    spring.ai.vectorstore.name=vector_store
    ```

### Frontend Setup

1. Navigate to the client directory:
    ```sh
    cd client
    ```

2. Install the dependencies:
    ```sh
    npm install
    ```

3. Start the development server:
    ```sh
    npm start
    ```

## Usage

1. Open your browser and navigate to `http://localhost:3000`.
2. Sign up or sign in to your account.
3. Use the navigation bar to access different features like adding recipes, viewing recipes, and participating in live cooking classes.
4. Customize recipes using the AI Recipe Customization feature.

## API Endpoints

### Authentication

- `POST /api/auth/signup` - Sign up a new user
- `POST /api/auth/signin` - Sign in an existing user

### Recipes

- `GET /api/recipes` - Get all recipes
- `GET /api/recipes/{id}` - Get a specific recipe by ID
- `POST /api/recipes` - Add a new recipe (Admin only)
- `PUT /api/recipes/{id}` - Update a recipe (Admin only)
- `DELETE /api/recipes/{id}` - Delete a recipe (Admin only)

### Live Cooking Classes

- `GET /api/live-classes` - Get all live cooking classes
- `POST /api/live-classes` - Add a new live cooking class (Admin only)

### AI Recipe Customization

- `POST /api/ai-customization` - Customize a recipe using AI

## Contributing

Contributions are welcome! Please follow these steps to contribute:

1. Fork the repository.
2. Create a new branch (`git checkout -b feature/your-feature`).
3. Make your changes.
4. Commit your changes (`git commit -m 'Add some feature'`).
5. Push to the branch (`git push origin feature/your-feature`).
6. Open a pull request.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
