-----
# SiLoker - Job Vacancy Information System

**A robust web application built with Spring Boot to connect job seekers with companies.**
-----

"SiLoker" is a comprehensive Job Vacancy Information System designed to streamline the process of hiring and job searching. This application serves three main user roles: Administrators, Job Seekers, and Companies, each with a dedicated dashboard and functionalities.

## ✨ Key Features

  - **👤 Multi-Role Access:**
      - **Administrator:** Manages all users, listings, and application data.
      - **Job Seeker:** Can browse, search, and apply for jobs.
      - **Company (HR):** Can post new job vacancies and manage applications for their listings.
  - **🔐 Secure Authentication:** Built with Spring Security for robust user authentication and authorization.
  - **🗂️ Full CRUD Functionality:** Complete Create, Read, Update, and Delete operations for job listings, user profiles, and applications.
  - **📄 DTO Mapping:** Uses MapStruct for efficient and clean mapping between DTOs and JPA entities.
  - **🗄️ Relational Database:** Powered by MySQL to ensure data integrity and persistence.

## 🛠️ Tech Stack

  - **Backend:** Java 17, Spring Boot, Spring Security, Spring Data JPA (Hibernate)
  - **Database:** MySQL
  - **Build Tool:** Apache Maven
  - **Utilities:** Lombok

## 🚀 Getting Started

Follow these instructions to get a local copy of the project up and running for development and testing purposes.

### Prerequisites

Make sure you have the following software installed on your machine:

  * JDK 17 or later
  * Apache Maven
  * MySQL Server

### Installation

1.  **Clone the repository:**

    ```sh
    git clone https://github.com/achmaddaffar/siloker-spring-boot.git
    cd siloker-spring-boot
    ```

2.  **Configure the database:**

      - Create a new database in MySQL for the project.
        ```sql
        CREATE DATABASE siloker_db;
        ```
      - Open the `src/main/resources/application.properties` file.
      - Update the `spring.datasource.url`, `spring.datasource.username`, and `spring.datasource.password` properties to match your MySQL configuration.
        ```properties
        spring.datasource.url=jdbc:mysql://localhost:3306/siloker_db
        spring.datasource.username=your_username
        spring.datasource.password=your_password
        ```

3.  **Run the application:**
    Use the Maven Spring Boot plugin to run the application.

    ```sh
    mvn spring-boot:run
    ```

4.  **Access the application:**
    Once the application is running, you can access it in your web browser at:
    [http://localhost:8080](https://www.google.com/search?q=http://localhost:8080)
