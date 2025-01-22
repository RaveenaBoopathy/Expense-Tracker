# Expenses Tracker WebApp

## Description

The **Expenses Tracker WebApp** is a comprehensive financial management application built using **Spring Boot** and **MySQL**. It helps users manage their expenses effectively by providing features like secure login, expense tracking, and filtering capabilities. With **Spring Security** for user authentication, **Thymeleaf** and **Bootstrap** for a responsive user interface, this app offers a robust solution to track and analyze financial data.

## Features

- **User Authentication & Authorization**: Users can securely sign up, log in, and manage their accounts with authentication and role-based authorization.
- **Expense Management**: Track your expenses by adding, updating, viewing, and deleting financial entries.
- **Filtering**: Sort and filter expenses by categories, dates, or amounts for a personalized financial overview.
- **Responsive Design**: An intuitive and mobile-friendly interface, thanks to **Bootstrap** and **Thymeleaf**.

## Technologies Used

- **Java** - The core programming language for backend development.
- **Spring Boot** - Framework for building the backend.
- **Spring MVC** - Used for the model-view-controller architecture.
- **Spring Security** - Provides user authentication and authorization.
- **Spring Data JPA** - For database interaction and ORM.
- **MySQL** - Database used for storing user and expense data.
- **Thymeleaf** - Template engine for rendering dynamic content.
- **Bootstrap** - Frontend framework for responsive and mobile-friendly UI.

## How to Run the Project

### Step 1: Clone the Repository

Clone the repository to your local machine using the following command:

```bash
git clone https://github.com/RaveenaBoopathy/Expense-Tracker.git

Step 2: Configure Database
Install MySQL if not already installed.
Create a new database in MySQL.
Update the application.properties file in the project with your MySQL database credentials, including:
spring.datasource.url
spring.datasource.username
spring.datasource.password
spring.datasource.driver-class-name
