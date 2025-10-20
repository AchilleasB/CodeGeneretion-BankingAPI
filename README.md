# Banking API - Full Stack Application

A comprehensive banking application built with Spring Boot and Vue.js that provides secure banking operations including account management, transactions, and user authentication.

## 🏗️ System Architecture

This is a full-stack banking application consisting of:

### **Backend** (Java Spring Boot)
- **RESTful API** built with Spring Boot 3.2.4
- **Database**: H2 in-memory database (with PostgreSQL support)
- **Security**: JWT-based authentication with Spring Security
- **Testing**: JUnit, Mockito, and Cucumber BDD tests

### **Frontend** (Vue.js)
- **Framework**: Vue.js 3.4 with Composition API
- **State Management**: Pinia stores
- **Routing**: Vue Router
- **UI Framework**: Bootstrap 5
- **HTTP Client**: Axios

## ✨ Features

### Customer Features
- **User Registration & Authentication**
  - Secure JWT-based login system
  - Registration with approval workflow
  - Profile management

- **Account Management**
  - Multiple account types (Checking & Savings)
  - View account balances and details
  - IBAN-based account identification

- **Transactions**
  - Transfer funds between accounts
  - ATM operations (deposit/withdraw)
  - Transaction history with pagination
  - Daily transaction limits
  - Transaction amount limits

### Employee (Admin) Features
- **User Management**
  - Approve/decline new user registrations
  - View all users
  - Manage user status

- **Account Administration**
  - Create new accounts for users
  - Update account limits (absolute & transaction limits)
  - Toggle account active/inactive status
  - View all accounts

- **Transaction Monitoring**
  - View all transactions across the system
  - Transaction history for specific users


## 🛠️ Technology Stack

### Backend
| Technology | Version | Purpose |
|------------|---------|---------|
| Java | 17 | Programming Language |
| Spring Boot | 3.2.4 | Application Framework |
| Spring Security | 6.2.4 | Authentication & Authorization |
| Spring Data JPA | - | Data Persistence |
| H2 Database | - | In-memory Database |
| PostgreSQL | 42.7.3 | Production Database (optional) |
| JWT (jjwt) | 0.11.5 | Token-based Authentication |
| Lombok | - | Code Generation |
| ModelMapper | 3.2.0 | DTO Mapping |
| Cucumber | 7.11.2 | BDD Testing |
| JUnit 5 | - | Unit Testing |
| Maven | - | Build Tool |

### Frontend
| Technology | Version | Purpose |
|------------|---------|---------|
| Vue.js | 3.4.21 | Frontend Framework |
| Pinia | 2.1.7 | State Management |
| Vue Router | 4.3.0 | Routing |
| Axios | 1.6.8 | HTTP Client |
| Bootstrap | 5.3.3 | UI Framework |
| Vite | 5.2.8 | Build Tool |

## 🚀 Getting Started

### Prerequisites
- Java 17 or higher
- Node.js 16+ and npm
- Maven 3.6+

### Backend Setup

1. **Navigate to backend directory:**
   ```bash
   cd backend
   ```

2. **Build the project:**
   ```bash
   mvnw clean install
   ```

3. **Run the application:**
   ```bash
   mvnw spring-boot:run
   ```

   The backend API will start on `http://localhost:8080`

4. **Access H2 Console (optional):**
   - URL: `http://localhost:8080/h2-console`
   - JDBC URL: `jdbc:h2:mem:bank_db`
   - Username: `super`
   - Password: `bank`

### Frontend Setup

1. **Navigate to frontend directory:**
   ```bash
   cd frontend
   ```

2. **Install dependencies:**
   ```bash
   npm install
   ```

3. **Run development server:**
   ```bash
   npm run dev
   ```

   The frontend will start on `http://localhost:1573`

4. **Build for production:**
   ```bash
   npm run build
   ```

## 👥 User Roles

### Customer
- Can view their own accounts
- Can perform transactions (transfers, ATM operations)
- Can view their transaction history
- Must be approved by an employee to access the system

### Employee (Admin)
- All customer permissions
- Can approve/decline user registrations
- Can create accounts for users
- Can modify account and user limits
- Can view all users, accounts, and transactions
- Can activate/deactivate accounts


## 🧪 Testing

### Backend Testing

The application includes comprehensive test coverage:

#### Unit Tests
```bash
mvnw test
```

#### BDD Tests (Cucumber)
Located in `src/test/resources/features/`:

Run Cucumber tests:
```bash
mvnw test -Dtest=CucumberTestRunner
```

### Test Coverage
- Controller layer: JUnit + MockMvc
- Service layer: JUnit + Mockito
- Integration tests: Cucumber BDD
- Security tests: Spring Security Test

## 📁 Project Structure

```
CodeGeneretion-BankingAPI/
├── backend/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/restapi/banking/app/
│   │   │   │   ├── config/          # Security, CORS, Database init
│   │   │   │   ├── controller/      # REST controllers
│   │   │   │   ├── dto/             # Data Transfer Objects
│   │   │   │   ├── exception/       # Exception handling
│   │   │   │   ├── model/           # JPA entities
│   │   │   │   ├── repository/      # Data repositories
│   │   │   │   ├── service/         # Business logic
│   │   │   │   └── utilities/       # Helper classes
│   │   │   └── resources/
│   │   │       └── application.properties
│   │   └── test/
│   │       ├── java/                # Unit & integration tests
│   │       └── resources/features/  # Cucumber BDD scenarios
│   └── pom.xml
│
└── frontend/
    ├── src/
    │   ├── assets/              # Static assets
    │   ├── components/          # Vue components
    │   │   ├── account/        # Login, Registration
    │   │   ├── admin/          # Admin components
    │   │   └── customer/       # Customer components
    │   ├── router/             # Vue Router configuration
    │   ├── stores/             # Pinia stores
    │   ├── utils/              # Utility functions
    │   ├── views/              # Page views
    │   ├── App.vue             # Root component
    │   └── main.js             # Application entry
    └── package.json
```

## 🔒 Security Features

- **JWT Authentication**: Stateless authentication using JSON Web Tokens
- **Password Encryption**: BCrypt password hashing
- **Role-Based Access Control**: Method-level security with custom expressions
- **CORS Protection**: Configured allowed origins
- **Input Validation**: Jakarta Validation API
- **SQL Injection Prevention**: JPA/Hibernate parameterized queries
- **Transaction Limits**: Daily and per-transaction limits
- **Account Ownership Verification**: Custom security expressions

## 📄 License

This project is developed as part of InHolland Year 2 coursework.


**Note**: This application is for educational purposes. For production use, additional security measures, error handling, and testing would be required.

