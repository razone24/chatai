# Spring AI Chat App for Game Rewards

[![Build Status](https://img.shields.io/badge/build-passing-brightgreen.svg)]()
[![License](https://img.shields.io/badge/license-MIT-blue.svg)]()

A Spring Boot–based AI chat service specialized in gaming-related queries and rewards distribution. Integrates vector-search over game-specific reward data, chat memory, and a simulated “web-search” tool for board games. Powered by Ollama’s Llama 3.2 model with nomic-embed-text embeddings, all running in Docker.

---

## 🔍 Table of Contents

- [Features](#-features)
- [Tech Stack](#-tech-stack)
- [Prerequisites](#-prerequisites)
- [Getting Started](#-getting-started)
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
- **Board-Game Info Tool**  
  − “Web search” simulator for Monopoly, Chess, and Scrabble rewards.
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
  − Ollama server with Llama 3.2 (“lama-3.2”)
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
- **API key** (if using any external AI providers)

---

## 🏁 Getting Started

1. **Clone & Build**
   ```bash
   git clone https://github.com/razone24/chatai.git
   cd chatai
   ./mvnw clean package
   sh db/run.sh
   sh db/init.sh
   run app
   ```
2. **Ask questions**
    ```
   curl -G "http://localhost:8080/chat/myuser/ask" \
     --data-urlencode "message=Hello, how are rewards distributed in poker?""
   ```
