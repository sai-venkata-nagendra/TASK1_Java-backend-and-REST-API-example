# Task Manager REST API

A robust Java Spring Boot REST API for managing and executing shell command tasks with built-in security validation and MongoDB integration. This application provides complete CRUD operations for task management with secure command execution capabilities.

## 📋 Project Overview

This application implements a RESTful API that allows users to create, manage, and execute shell command tasks. Each task represents a shell command that can be safely executed, with built-in security measures to prevent malicious commands. The system maintains execution history and provides comprehensive task management capabilities.

## 🚀 Features

- **🔐 Secure Command Validation** - Blocks unsafe/malicious commands
- **📊 Task Execution History** - Tracks command execution with timestamps
- **🔍 Advanced Search** - Find tasks by name with partial matching
- **💾 MongoDB Integration** - Persistent data storage with Spring Data MongoDB
- **🛡️ Input Validation** - Comprehensive request validation and error handling
- **🌐 RESTful API** - Clean, standardized API endpoints
- **⚡ Spring Boot 3.2** - Modern Java framework with latest features

## 🛠️ Technology Stack

- **Java 17** - Programming language
- **Spring Boot 3.2.0** - Application framework
- **Spring Data MongoDB** - Database integration
- **Maven** - Dependency management
- **MongoDB** - NoSQL database
- **Jakarta Validation** - Input validation
- **Tomcat** - Embedded web server

## 📁 Project Structure

<img width="438" height="394" alt="image" src="https://github.com/user-attachments/assets/d08469a4-344f-44ac-a9bd-f700f9c6ac58" />


curl -X GET "http://localhost:8080/api/tasks?id=1"

Search Tasks by Name
bash

curl -X GET "http://localhost:8080/api/tasks/search?name=Hello"

Create New Task
bash

curl -X PUT "http://localhost:8080/api/tasks" \
  -H "Content-Type: application/json" \
  -d '{
    "id": "4",
    "name": "Show Date",
    "owner": "Alice Brown",
    "command": "date"
  }'

Execute Task
bash

curl -X PUT "http://localhost:8080/api/tasks/execute?id=1"

Delete Task
bash

curl -X DELETE "http://localhost:8080/api/tasks?id=4"

🔒 Security Features

The application includes comprehensive command validation to prevent execution of unsafe commands:

Blocked Commands:

    File system operations (rm, del, format)

    System commands (shutdown, reboot)

    Network operations (wget, curl, ssh)

    Privilege escalation (sudo, su)

    And many more dangerous operations

Example Security Response:
json

"Unsafe command detected: rm -rf /"

🗄️ Data Models
Task Object
json

{
  "id": "123",
  "name": "Print Hello",
  "owner": "John Smith",
  "command": "echo Hello World!",
  "taskExecutions": []
}

TaskExecution Object
json

{
  "startTime": "2025-10-15 13:02:54.216Z",
  "endTime": "2025-10-15 13:02:54.217Z",
  "output": "Hello World!"
}

⚙️ Installation & Setup
Prerequisites

    Java 17 or higher

    Maven 3.6+

    MongoDB (optional - application includes in-memory fallback)

Running the Application

    Clone the repository

bash

git clone https://github.com/yourusername/task-manager.git
cd task-manager

    Build and run with Maven

bash

mvn clean compile
mvn spring-boot:run

    Access the API

text

http://localhost:8080/api/tasks

Configuration

Update src/main/resources/application.properties for custom settings:
properties

spring.data.mongodb.uri=mongodb://localhost:27017/taskdb
server.port=8080

🧪 Testing

Run the comprehensive test script:
bash

chmod +x test-api.sh
./test-api.sh

📊 Sample Data

The application initializes with sample tasks:

    Print Hello - echo Hello World!

    List Files - ls -la

    Show Current Directory - pwd

🐛 Error Handling

The API provides meaningful error responses for:

    Task not found (404)

    Unsafe commands (400)

    Validation errors (400)

    Server errors (500)

## 📸 Screenshots
Application Running Successfully

<img width="1850" height="1077" alt="Screenshot from 2025-10-15 18-37-24" src="https://github.com/user-attachments/assets/2c80333b-d905-4991-a4ba-51c80b15d4d7" />

Spring Boot application starting up with MongoDB connection - showing system date/time and username
Complete API Test Results

<img width="1850" height="1077" alt="Screenshot from 2025-10-15 18-35-54" src="https://github.com/user-attachments/assets/1575452d-e8fd-49a1-97d0-b60c2221cc4a" />
<img width="1850" height="1077" alt="Screenshot from 2025-10-15 18-36-09" src="https://github.com/user-attachments/assets/441751b4-9980-4151-a6d9-5cfd11bb0a3e" />


Comprehensive testing of all API endpoints showing successful responses
Security Validation Working

<img width="843" height="147" alt="image" src="https://github.com/user-attachments/assets/93e204cd-9dae-464e-8bc2-92dcffd70d24" />

Unsafe command 'rm -rf /' being properly blocked by security validation
Task Execution with Timestamps


## 👨‍💻 Developer

ssvnagendra

*Kaiburr Assessment Task 1 - REST API Implementation*

## 📄 License

This project is part of the Kaiburr technical assessment.
