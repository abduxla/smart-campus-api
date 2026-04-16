# Smart Campus Sensor & Room Management API

## Overview
This project is a RESTful API developed for the 5COSC022W Client-Server Architectures coursework using **JAX-RS** and **Jersey**.  
It manages rooms, sensors, and sensor reading history for a Smart Campus system.

The API supports:
- Discovery endpoint
- Room management
- Sensor registration and filtering
- Nested sensor reading history
- Custom exception handling
- Request and response logging

This project uses **in-memory data structures** (`HashMap`, `ArrayList`) and does **not** use a database.

---

## Technology Stack
- Java
- Maven
- JAX-RS
- Jersey
- Grizzly HTTP Server

---

## Project Structure
- `Room` model
- `Sensor` model
- `SensorReading` model
- `RoomResource`
- `SensorResource`
- `SensorReadingResource`
- Custom exceptions and exception mappers
- Logging filter

---

## How to Build the Project
1. Open the project in Apache NetBeans
2. Right-click the project
3. Click **Clean and Build**

Alternatively with Maven:
```bash
mvn clean install
