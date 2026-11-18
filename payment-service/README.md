# Payment Management Service (RescueBite)
**Port:** `8085`  
**Database:** `rescuebite_payment_db` (MongoDB)  
**Swagger UI:** `http://localhost:8085/swagger-ui.html`

---

## 📋 Responsibilities
- Simulate payment processing (Credit Card, Debit Card, Digital Wallet, Cash on Collection)
- Maintain non-sensitive transaction audits and reference IDs
- Inter-service REST coordination:
  - Fetches order details from `order-service` (Port 8084) to verify payable amount
  - Updates order status to `CONFIRMED` upon payment success
  - Emits payment notification to `notification-service` (Port 8086)
- Refund workflows with automated order cancellation

---

## 🚀 How to Run

### Standalone Maven
```bash
cd payment-service
mvn spring-boot:run
```

---

## 📡 API Endpoints

| Method | Endpoint | Description |
|---|---|---|
| `POST` | `/api/payments` | Process simulated payment |
| `GET` | `/api/payments/{id}` | Get payment by ID |
| `GET` | `/api/payments/order/{orderId}` | Get payment by Order ID |
| `GET` | `/api/payments/customer/{customerId}` | List payments by customer |
| `GET` | `/api/payments?status={status}` | List all payments with optional status filter |
| `POST` | `/api/payments/{id}/refund` | Refund payment |

---

## 🧪 Sample Request Payloads

### 1. Process Successful Payment
`POST http://localhost:8085/api/payments`
```json
{
  "orderId": "660c1f2b3e4a5c6d7e8f9055",
  "customerId": "660c1f2b3e4a5c6d7e8f9011",
  "amount": 900.00,
  "currency": "LKR",
  "paymentMethod": "CREDIT_CARD",
  "cardLastFour": "4242",
  "simulateFailure": false
}
```

### 2. Simulate Payment Failure (Card Decline)
`POST http://localhost:8085/api/payments`
```json
{
  "orderId": "660c1f2b3e4a5c6d7e8f9055",
  "customerId": "660c1f2b3e4a5c6d7e8f9011",
  "amount": 900.00,
  "currency": "LKR",
  "paymentMethod": "CREDIT_CARD",
  "cardLastFour": "0002",
  "simulateFailure": true
}
```
*(Throws HTTP 402 with decline description and logs FAILED transaction for university demo)*

