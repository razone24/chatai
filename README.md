# Spring AI Chat App for Game Rewards

[![Build Status](https://img.shields.io/badge/build-passing-brightgreen.svg)]()
[![License](https://img.shields.io/badge/license-MIT-blue.svg)]()

A Spring Boot–based AI chat service specialized in gaming-related queries and rewards distribution. Integrates vector-search over game-specific reward data, chat memory, internet search via Brave API, and a simulated "web-search" tool for board games. Powered by Ollama's Llama 3.2 model with nomic-embed-text embeddings, all running in Docker.

---

## 🔍 Table of Contents

- [Features](#-features)
- [Tech Stack](#-tech-stack)
- [Prerequisites](#-prerequisites)
- [Getting Started](#-getting-started)
- [Configuration](#-configuration)
- [API Reference](#-api-reference)
- [Testing](#-testing)
- [Docker](#-docker)
- [Contributing](#-contributing)
- [License](#-license)

---

## 🚀 Features

- **Rewards Distribution Chat**  
  − Ask about rewards split for social-casino features (Poker, Slots, Bingo, Blackjack, Roulette) and get answers pulled from a vectorized PostgreSQL ML database.
- **Chat Memory**  
  − Retains context across turns for multi-step conversations.
- **Internet Search**  
  − Real-time web search using Brave Search API for current information, news, and recent updates.
- **Board-Game Info Tool**  
  − "Web search" simulator for Monopoly, Chess, and Scrabble rewards.
- **Extensible AI Layer**  
  − Swap in additional tools or embedding/index backends as needed.

---

## 🛠️ Tech Stack

- **Language & Framework**  
  − Java 24 & Spring Boot 3.x
- **Embedding & Vector Search**  
  − Postgres ML (vector extension) running in Docker  
  − nomic-embed-text for text embeddings
- **Model Inference**  
  − Ollama server with Llama 3.2 ("lama-3.2")
- **Internet Search**  
  − Brave Search API for real-time web search
- **Persistence**  
  − PostgreSQL for vector DB, chat history, and metadata
- **Build & Packaging**  
  − Maven wrapper (`mvnw`)
- **Monitoring & Metrics**  
  − Spring Actuator

---

## 📋 Prerequisites

- **JDK 24**
- **Docker**
- **Maven 3.6+** 
- **Ollama CLI** (configured to serve Llama 3.2 and nomic-embed-text for embeddings)
- **Brave Search API Key** (optional, for internet search functionality)

---

## 🏁 Getting Started

1. **Clone & Build**
   ```bash
   git clone https://github.com/razone24/chatai.git
   cd chatai
   ./mvnw clean package
   ```

2. **Setup Database**
   ```bash
   sh db/run.sh
   sh db/init.sh
   ```

3. **Configure API Keys** (Optional)
   - Get a Brave Search API key from [Brave Search API](https://api-dashboard.search.brave.com/)
   - Set the environment variable:
     ```bash
     export BRAVE_API_KEY=your_brave_api_key_here
     ```

4. **Run Application**
   ```bash
   java -jar target/chat-0.0.1-SNAPSHOT.jar
   ```

---

## ⚙️ Configuration

### Brave Search API Setup

To enable internet search functionality:

1. **Sign up** for Brave Search API at https://api-dashboard.search.brave.com/
2. **Get your API key** from the API Keys section
3. **Configure the application** by setting the environment variable:
   ```bash
   export BRAVE_API_KEY=your_brave_api_key_here
   ```
   
   Or update `application.properties`:
   ```properties
   brave.search.api.key=your_brave_api_key_here
   ```

**Note**: The application will work without the Brave API key, but internet search functionality will not be available.

---

## 🔗 API Reference

### Chat Endpoint

**Ask a question:**
```bash
curl -G "http://localhost:8080/chat/myuser/ask" \
  --data-urlencode "message=Hello, how are rewards distributed in poker?"
```

**Ask for current information (uses internet search):**
```bash
curl -G "http://localhost:8080/chat/myuser/ask" \
  --data-urlencode "message=What are the latest updates in cryptocurrency?"
```

**Ask about board games:**
```bash
curl -G "http://localhost:8080/chat/myuser/ask" \
  --data-urlencode "message=Tell me about Chess rewards"
```

### Available Tools

The AI assistant has access to the following tools:

1. **Game Rewards Database** - Information about casino game rewards (Poker, Slots, Bingo, Blackjack, Roulette)
2. **Internet Search** - Real-time search via Brave API for current information
3. **Board Game Info** - Predefined information about Monopoly, Chess, and Scrabble

---

## 🧪 Testing

### Health Check
```bash
curl http://localhost:8080/actuator/health
```

### Test Game Rewards
```bash
curl -G "http://localhost:8080/chat/testuser/ask" \
  --data-urlencode "message=How are poker rewards distributed?"
```

### Test Internet Search
```bash
curl -G "http://localhost:8080/chat/testuser/ask" \
  --data-urlencode "message=What's happening in the tech world today?"
```

---

## 🐳 Docker

For containerized deployment, make sure to pass the Brave API key as an environment variable:

```bash
docker run -p 8080:8080 -e BRAVE_API_KEY=your_api_key_here your-app-image
```

---

## 🤝 Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

---

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---

## 🔗 External APIs

- **Brave Search API**: https://api-dashboard.search.brave.com/app/documentation/web-search/get-started
- **Ollama**: https://ollama.ai/
- **PostgresML**: https://postgresml.org/
