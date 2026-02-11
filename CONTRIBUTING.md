# Contributing to Smart Clinic Management System

Thank you for your interest in contributing to the Smart Clinic Management System! This document provides guidelines for contributing to this project.

## Getting Started

1. **Fork the repository** on GitHub
2. **Clone your fork** locally:
   ```bash
   git clone https://github.com/YOUR-USERNAME/java-database-capstone.git
   cd java-database-capstone
   ```
3. **Set up the development environment** (see below)

## Development Setup

### Prerequisites

- Java 17 or higher
- Maven 3.6 or higher
- MySQL 8.0 or higher
- MongoDB 4.4 or higher

### Setting up the database

1. Create a MySQL database named `cms`
2. Create a MongoDB database named `clinic`
3. Update `app/src/main/resources/application.properties` with your database credentials

### Running the application

```bash
cd app
./mvnw spring-boot:run
```

The application will start on `http://localhost:8080`

## Making Changes

1. **Create a new branch** for your feature or bugfix:
   ```bash
   git checkout -b feature/your-feature-name
   ```

2. **Make your changes** following the coding standards below

3. **Test your changes** thoroughly

4. **Commit your changes** with clear, descriptive commit messages:
   ```bash
   git commit -m "Add feature: description of what you added"
   ```

5. **Push to your fork**:
   ```bash
   git push origin feature/your-feature-name
   ```

6. **Create a Pull Request** on GitHub

## Coding Standards

- Follow standard Java coding conventions
- Use meaningful variable and method names
- Add comments for complex logic
- Ensure code is properly formatted
- Write unit tests for new features

## Code Review Process

All submissions require review before being merged. We'll review your pull request and may suggest changes or improvements.

## Reporting Issues

If you find a bug or have a feature request, please create an issue on GitHub with:
- Clear title and description
- Steps to reproduce (for bugs)
- Expected vs actual behavior
- Any relevant error messages or screenshots

## Questions?

Feel free to reach out by creating an issue with the "question" label.

Thank you for contributing!
