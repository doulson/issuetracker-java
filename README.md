# Issue Tracker

## Table of Contents

- [Overview](#overview)
- [Features](#features)
- [Technologies Used](#technologies-used)
    - [Backend (issue tracker)](#Backend)
    - Frontend (issue-tracker-ui)(coming soon)
- [Learning Objectives](#learning-objectives)
- [License](#license)


## Overview

Issue tracker is a full-stack application that enables users to raise issue and discuss solution in the application. It offers features such as user registration, secure email validation, issue tracking (including creation, updating, comment and archiving). The application ensures security using JWT tokens and adheres to best practices in REST API design. The backend is built with Spring Boot 3 and Spring Security 6, while the frontend is developed using React and nextjs for styling and routering.

## Features

- User Registration: Users can register for a new account.
- Email Validation: Accounts are activated using secure email validation codes.
- User Authentication: Existing users can log in to their accounts securely.
- Issue Track: Users can create, update, comment, and archive their issues.

#### Class diagram
![Class diagram](screenshots/class-diagram.png)

#### Spring security diagram
![Security diagram](screenshots/security.png)


## Technologies Used

### Backend ()

- Spring Boot 3
- Spring Security 6
- JWT Token Authentication
- Spring Data JPA
- JSR-303 and Spring Validation
- Docker

### Frontend (book-network-ui)

- Reactjs
- Nextjs
- Framer-motion
- shadcn/ui component customize
- dark mode/ light mode


## Learning Objectives

By following this project, what I learnt:

- Designing a class diagram from business requirements
- Implementing a mono repo approach
- Securing an application using JWT tokens with Spring Security
- Registering users and validating accounts via email
- Utilizing inheritance with Spring Data JPA
- Implementing the service layer and handling application exceptions
- Object validation using JSR-303 and Spring Validation
- Handling custom exceptions
- Implementing pagination and REST API best practices
- Using Spring Profiles for environment-specific configurations
- Implementing business requirements and handling business exceptions
- Dockerizing the infrastructure
- CI/CD pipeline & deployment

## License

This project is licensed under the Apache License 2.0. See the [LICENSE](LICENSE) file for details.
