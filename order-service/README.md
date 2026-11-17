# Order Management Service (RescueBite)

**Assigned to:** Member 2  
**Port:** `8084`  
**Database:** `rescuebite_order_db` (MongoDB)  
**Swagger UI:** `http://localhost:8084/swagger-ui.html`

---

## 📋 Responsibilities
- Create, view, cancel, and track order lifecycles
- Coordinate inter-service calls with:
  - `food-listing-service` (Port 8083) to verify stock availability and deduct quantities atomically.
  - `notification-service` (Port 8086) to alert customers and providers on placement and status changes.
- Generate unique 6-character collection verification codes (`RB-XXXX`).
- Automatic inventory replenishment in `food-listing-service` upon order cancellation.

---

## 🚀 How to Run

### Standalone Maven
```bash
cd order-service
mvn spring-boot:run
```

---

## 📡 API Endpoints

| Method | Endpoint | Description |
|---|---|---|
| `POST` | `/api/orders` | Create an order (reserves listing stock) |
| `GET` | `/api/orders/{id}` | Get order by ID |
| `GET` | `/api/orders/customer/{customerId}` | List orders placed by a customer |
| `GET` | `/api/orders/provider/{providerId}` | List orders received by a provider |
| `GET` | `/api/orders?status={status}` | List orders with optional status filter |
| `PATCH` | `/api/orders/{id}/status` | Update status (used by Payment Service) |
| `POST` | `/api/orders/{id}/cancel` | Cancel order & restore inventory |
| `POST` | `/api/orders/{id}/collect?collectionCode={code}` | Verify code and mark collected |

---

## 🧪 Sample Request Payloads

### 1. Place Order for Surplus Food
`POST http://localhost:8084/api/orders`
```json
{
  "customerId": "660c1f2b3e4a5c6d7e8f9011",
  "customerName": "Alice Johnson",
  "customerEmail": "alice@example.com",
  "listingId": "660c1f2b3e4a5c6d7e8f9033",
  "quantity": 2
}
```

### 2. Verify and Collect Order
`POST http://localhost:8084/api/orders/{id}/collect?collectionCode=RB-4291`

