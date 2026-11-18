# Food Provider Management Service (RescueBite)

**Assigned to:** Member 1  
**Port:** `8082`  
**Database:** `rescuebite_provider_db` (MongoDB)  
**Swagger UI:** `http://localhost:8082/swagger-ui.html`

---

## 📋 Responsibilities
- Food provider business registration (Restaurants, Bakeries, Supermarkets, Cafes)
- Management of business contact information, operating hours, and location/city
- Verification workflow (`PENDING` -> `VERIFIED` or `REJECTED`)
- Inter-service verification lookup (`GET /api/providers/{id}/is-verified`) consumed by `food-listing-service`

---

## 🚀 How to Run

### Standalone Maven
```bash
cd food-provider-service
mvn spring-boot:run
```

---

## 📡 API Endpoints

| Method | Endpoint | Description |
|---|---|---|
| `POST` | `/api/providers` | Create food provider profile |
| `GET` | `/api/providers/{id}` | Get provider by ID |
| `GET` | `/api/providers/user/{userId}` | Get provider by User ID |
| `GET` | `/api/providers` | List providers (filters: status, city, businessType) |
| `PUT` | `/api/providers/{id}` | Update provider details |
| `PATCH` | `/api/providers/{id}/verify` | Approve or reject provider |
| `GET` | `/api/providers/{id}/is-verified` | Lightweight verification check (inter-service) |
| `DELETE` | `/api/providers/{id}` | Delete provider profile |

---

## 🧪 Sample Request Payloads

### 1. Register Provider Business Profile
`POST http://localhost:8082/api/providers`
```json
{
  "userId": "660c1f2b3e4a5c6d7e8f9012",
  "businessName": "Fresh Crust Bakery",
  "businessType": "BAKERY",
  "registrationNumber": "BR-2024-9841",
  "contactEmail": "info@freshcrust.com",
  "contactPhone": "+94112345678",
  "address": "78 Galle Road, Kollupitiya",
  "city": "Colombo",
  "operatingHours": "07:00 AM - 09:00 PM"
}
```

### 2. Verify Provider (Admin Action)
`PATCH http://localhost:8082/api/providers/{id}/verify`
```json
{
  "status": "VERIFIED",
  "rejectionReason": null
}
```

