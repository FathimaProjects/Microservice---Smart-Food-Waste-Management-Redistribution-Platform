# Notification Service (RescueBite)

 
**Port:** `8086`  
**Database:** `rescuebite_notification_db` (MongoDB)  
**Swagger UI:** `http://localhost:8086/swagger-ui.html`

---

## 📋 Responsibilities
- Create and manage notifications for orders, payments, donations, and platform activity
- Track notification statuses (`UNREAD`, `READ`)
- Filter user inbox notifications and unread counters
- Inter-service consumer accepting asynchronous alerts from `order-service` and `payment-service`

---

## 🚀 How to Run

### Standalone Maven
```bash
cd notification-service
mvn spring-boot:run
```

---

## 📡 API Endpoints

| Method | Endpoint | Description |
|---|---|---|
| `POST` | `/api/notifications` | Create / dispatch notification |
| `GET` | `/api/notifications/{id}` | Get notification by ID |
| `GET` | `/api/notifications/user/{userId}` | Get all notifications for user |
| `GET` | `/api/notifications/user/{userId}/unread` | Get unread notifications |
| `GET` | `/api/notifications/user/{userId}/unread-count` | Get unread badge count |
| `PATCH` | `/api/notifications/{id}/read` | Mark notification as read |
| `PATCH` | `/api/notifications/user/{userId}/read-all` | Mark all user notifications as read |
| `DELETE` | `/api/notifications/{id}` | Delete notification |

---

## 🧪 Sample Request Payloads

### 1. Dispatch Notification
`POST http://localhost:8086/api/notifications`
```json
{
  "userId": "660c1f2b3e4a5c6d7e8f9011",
  "title": "Donation Claim Approved",
  "message": "Your claim for 15 bread loaves from Fresh Crust Bakery has been approved. Pickup code: RB-4291",
  "type": "DONATION"
}
```

### 2. Mark Notification as Read
`PATCH http://localhost:8086/api/notifications/{id}/read`

