# User Management Service (RescueBite)

**Assigned to:** Member 1  
**Port:** `8081`  
**Database:** `rescuebite_user_db` (MongoDB)  
**Swagger UI:** `http://localhost:8081/swagger-ui.html`

---

## 📋 Responsibilities
- User registration with role selection (`CUSTOMER`, `CHARITY`, `PROVIDER`, `ADMIN`)
- Password hashing with BCrypt
- Stateless JWT authentication and token issuance
- Profile management (view, update, delete)
- Role-based access control and JWT bearer authorization

---

## 🚀 How to Run

### Option 1: Standalone Maven
```bash
cd user-service
mvn spring-boot:run
```

### Option 2: Run Jar
```bash
cd user-service
mvn clean package -DskipTests
java -jar target/user-service-1.0.0.jar
```

---

## 📡 API Endpoints

### Authentication (`/api/auth`)
| Method | Endpoint | Description | Public? |
|---|---|---|---|
| `POST` | `/api/auth/register` | Register new user and return JWT | Yes |
| `POST` | `/api/auth/login` | Authenticate and obtain JWT | Yes |

### User Management (`/api/users`)
| Method | Endpoint | Description | Auth Required |
|---|---|---|---|
| `GET` | `/api/users/{id}` | Get user by ID | Bearer JWT |
| `GET` | `/api/users/email/{email}` | Get user by email | Bearer JWT |
| `GET` | `/api/users?role={role}` | List users (optionally by role) | Bearer JWT |
| `PUT` | `/api/users/{id}` | Update profile | Bearer JWT |
| `DELETE` | `/api/users/{id}` | Delete user | Bearer JWT |

---

## 🧪 Sample Request Payloads

### 1. Register Customer
`POST http://localhost:8081/api/auth/register`
```json
{
  "name": "Alice Johnson",
  "email": "alice@example.com",
  "password": "Password123!",
  "phone": "+94771234567",
  "role": "CUSTOMER",
  "address": "45 Green Path, Colombo 03"
}
```

### 2. Register Food Provider
`POST http://localhost:8081/api/auth/register`
```json
{
  "name": "BakeHouse Admin",
  "email": "provider@bakehouse.com",
  "password": "ProviderPass123!",
  "phone": "+94777654321",
  "role": "PROVIDER",
  "address": "12 Flower Road, Colombo 07"
}
```

### 3. Login
`POST http://localhost:8081/api/auth/login`
```json
{
  "email": "alice@example.com",
  "password": "Password123!"
}
```
*Response contains the `token`. In Swagger UI or Postman, set the header: `Authorization: Bearer <token>`.*

