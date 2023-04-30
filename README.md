# Quiz Me!

Quiz Me! is a simple, easy-to-use web application that allows users to test their knowledge on various topics through multiple-choice quizzes.

## Features

- User registration and authentication
- Randomly generated quizzes with multiple-choice questions
- Time-limited quizzes
- Quiz results with  percentage scores
- Leaderboard displaying top 10 

## Technologies

- Java
- Spring Boot 3.0.5
- Thymeleaf 3.1.1
- Spring Security
- JPA (Java Persistence API)
- Hibernate
- PostgreSQL 
- Bootstrap
- Junit testing
- Lombok

## Getting Started

### Prerequisites

- Java JDK (version 17 or higher recommended)
- Maven
- PostgreSQL (or your choice of database)

### Installation

1. Clone the repository:
```
  git clone https://github.com/yourusername/quiz-web-app.git
```

2. Configure the database connection settings in the `application.properties` file located in `src/main/resources/`. Replace the placeholders with your database's credentials and settings:
```    
    spring.datasource.url=jdbc:postgresql://localhost:5432/your_database_name
    spring.datasource.username=your_username
    spring.datasource.password=your_password
```

3. Build the project:
```
  mvn clean install
```  

4. Run the application:
```
  mvn spring-boot:run
```  

5. Access the web app in your browser by navigating to:
```
    http://localhost:8080
```  
