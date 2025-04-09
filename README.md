# 💳 Bank Management System

A full-stack **Bank Management System** developed as a group project. This application enables users to sign up, log in, and securely perform everyday banking operations such as deposits, withdrawals, and transfers. It was built using **Spring Boot**, **Maven**, **Java**, **HTML**, **CSS**, and **PostgreSQL**, and deployed via **Render**.

## 🚀 Features

- ✅ User Authentication (Sign Up & Login)
- 🏦 Account Dashboard
- 💰 Deposit Funds
- 🔁 Transfer Money
- 🧾 Transaction History
- 🔐 Secure Password Encryption
- 🌐 Hosted on Render
- 🗃️ PostgreSQL database

## 🛠️ Tech Stack

| Layer             | Technology                         |
|------------------|-------------------------------------|
| Backend          | Java, Spring Boot, Maven            |
| Frontend         | HTML, CSS (Thymeleaf Templates)     |
| Database         | PostgreSQL (hosted on Render)       |
| Deployment       | Render                              |
| Build Tool       | Maven                               |
| Version Control  | Git + GitHub                        |


## 🔐 Authentication Flow

- On visiting the app, users can either sign up or log in.
- Passwords are encrypted before being stored in the database.
- Authenticated users are redirected to a dashboard where they can manage their finances.

## 🧪 How to Run Locally

1. **Clone the Repository**

   ```bash
   git clone https://github.com/404Found-CapaCITI/Bank-Management-System.git
   cd bank-management-system
   ```
2. **Configure the Database**

    - Set up a local PostgreSQL database or use the provided Render database.
  
    - Update the application.properties file:
    
     ```properties
    spring.datasource.url=jdbc:postgresql://<your-db-url>:5432/bankdb
    spring.datasource.username=<your-username>
    spring.datasource.password=<your-password>
    ```
3. **Run the App**

    ```bash
    ./mvnw spring-boot:run
    ```
4. **Visit the App**

    Open your browser and go to http://localhost:8080

## 🌐 Deployed Version

You can access the live app here:

👉 _Coming Soon!_

## 👨🏽‍💻 Contributing Team

* Pelma M. - @1632Pelma
* Isabella M. - @Isamonyeseala
* Mpho I. – @MphoItumeleng
* Nosipho M. - @Nosipho9

## 📝 Future Improvements

1. Add admin panel to manage user accounts.
2. Implement password changing.
3. Implement email notifications
4. Improve design.

## 

**Thank you for checking out our project! 💖**
