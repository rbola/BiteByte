# BiteByte

BiteByte is a full-stack recipe and live cooking platform built with React, Spring Boot, Spring AI and MongoDB.

![Demo of the app](/demobitebyte.gif)

## Table of Contents

- [Features](#features)
- [Architecture](#architecture)
- [Prerequisites](#prerequisites)
- [Setup](#setup)
- [Running the App](#running-the-app)
- [API Endpoints](#api-endpoints)
- [Contributing](#contributing)
- [License](#license)

## Features

- AI Recipe Customization powered by OpenAI
- Browse and add recipes with nutritional info
- Participate in live cooking classes
- User authentication and authorization (JWT)
- Dark and light theme toggle

## Architecture

The project is split into two parts:

### Backend — Spring Boot (port 8080)

A modular monolithic Spring Boot application with the following modules:

| Module | Responsibility |
|---|---|
| `ai` | OpenAI-powered recipe customization |
| `common` | Shared utilities, web config, DB initializer |
| `livecooking` | Live cooking class management |
| `recipe` | Recipe CRUD |
| `security` | JWT authentication filter |
| `user` | User registration and login |

### Frontend — React + Vite (port 5173)

A React SPA that proxies all `/api` and `/auth` requests to the backend.

### Technologies

- Java 21 (Amazon Corretto), Spring Boot 3, Spring AI, MongoDB
- React 18, Vite 4, React Router 6, Axios

## Prerequisites

- [Node.js](https://nodejs.org/) (v18+) and npm
- [Amazon Corretto 21](https://aws.amazon.com/corretto/) or any JDK 21
- [MongoDB](https://www.mongodb.com/) running locally on port 27017
- An [OpenAI API key](https://platform.openai.com/api-keys)

## Setup

### 1. Clone the repository

```sh
git clone https://github.com/yourusername/BiteByte.git
cd BiteByte
```

### 2. Configure environment variables

Create the local env file (already gitignored):

```sh
mkdir -p .vscode
echo "OPENAI_API_KEY=your-key-here" > .vscode/.env
```

This file is read automatically by `./gradlew bootRun` and by the VS Code / Cursor debugger launch config.

### 3. Enable the pre-commit hook (optional but recommended)

Prevents local config files from being committed accidentally:

```sh
git config core.hooksPath .githooks
```

### 4. Install frontend dependencies

```sh
cd client && npm install && cd ..
```

## Running the App

Open two terminals from the project root:

**Terminal 1 — Backend**

```sh
./gradlew bootRun
```

Spring Boot starts on [http://localhost:8080](http://localhost:8080). On first run the database is seeded with sample recipes and live cooking classes.

**Terminal 2 — Frontend**

```sh
cd client && npm start
```

Vite starts on [http://localhost:5173](http://localhost:5173) by default (increments automatically if the port is in use — check the terminal output for the exact URL). API calls are proxied automatically to the backend.

## API Endpoints

### Authentication

| Method | Path | Description |
|---|---|---|
| POST | `/api/auth/signup` | Register a new user |
| POST | `/api/auth/signin` | Sign in and receive a JWT |

### Recipes

| Method | Path | Description |
|---|---|---|
| GET | `/api/recipes/all` | List all recipes (public + personal) |
| GET | `/api/recipes/{id}` | Get a recipe by ID |
| POST | `/api/recipes` | Add a recipe _(admin only)_ |
| PUT | `/api/recipes/{id}` | Update a recipe _(admin only)_ |
| DELETE | `/api/recipes/{id}` | Delete a recipe _(admin only)_ |

### Live Cooking Classes

| Method | Path | Description |
|---|---|---|
| GET | `/api/live-cooking-classes` | List all classes |
| POST | `/api/live-cooking-classes` | Add a class _(admin only)_ |
| POST | `/api/live-cooking-classes/{id}/attend` | Attend a class |

### AI Recipe Customization

| Method | Path | Description |
|---|---|---|
| POST | `/api/ai-recipe-customization` | Customise a recipe using AI |
| POST | `/api/ai-recipe-customization/save` | Save a customised recipe to your account |
| GET | `/api/ai-recipe-customization` | List all customisations |
| GET | `/api/ai-recipe-customization/{id}` | Get a customisation by ID |

## Contributing

1. Fork the repository.
2. Create a new branch: `git checkout -b feature/your-feature`
3. Commit your changes: `git commit -m 'Add some feature'`
4. Push to the branch: `git push origin feature/your-feature`
5. Open a pull request.

## License

This project is licensed under the MIT License — see the [LICENSE](LICENSE) file for details.
