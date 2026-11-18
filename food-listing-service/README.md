# Food Listing Management Service (RescueBite)

**Assigned to:** Member 3  
**Port:** `8083`  
**Database:** `rescuebite_listing_db` (MongoDB)  
**Swagger UI:** `http://localhost:8083/swagger-ui.html`

---

## 📋 Responsibilities
- Create, view, update, and delete surplus food listings
- Manage discounted prices and free charitable donation flags (`donation: true`)
- Track quantities, units, expiration timestamps, and collection deadlines
- Expose atomic stock reduction (`PATCH /api/listings/{id}/reduce-stock`) and restoration (`PATCH /api/listings/{id}/restore-stock`) for `order-service`

---

## 🚀 How to Run

### Standalone Maven
```bash
cd food-listing-service
mvn spring-boot:run
```

---

## 📡 API Endpoints

| Method | Endpoint | Description |
|---|---|---|
| `POST` | `/api/listings` | Create food listing |
| `GET` | `/api/listings/{id}` | Get listing by ID |
| `GET` | `/api/listings` | List all listings (filters: category, donationOnly, status) |
| `GET` | `/api/listings/provider/{providerId}` | List listings by provider ID |
| `PUT` | `/api/listings/{id}` | Update listing details |
| `PATCH` | `/api/listings/{id}/reduce-stock` | Reduce available quantity (called by Order Service) |
| `PATCH` | `/api/listings/{id}/restore-stock` | Restore quantity on order cancellation |
| `DELETE` | `/api/listings/{id}` | Delete listing |

---

## 🧪 Sample Request Payloads

### 1. Create Paid Surplus Listing
`POST http://localhost:8083/api/listings`
```json
{
  "providerId": "660c1f2b3e4a5c6d7e8f9012",
  "providerName": "Fresh Crust Bakery",
  "title": "Surplus Sourdough & Croissants",
  "description": "Freshly baked today at 7 AM, remaining evening batch in excellent condition.",
  "category": "BAKED_GOODS",
  "quantity": 10,
  "unit": "boxes",
  "originalPrice": 1200.00,
  "discountedPrice": 450.00,
  "donation": false,
  "expiryDate": "2026-11-09T22:00:00",
  "collectionDeadline": "2026-11-09T20:30:00",
  "pickupAddress": "78 Galle Road, Kollupitiya, Colombo"
}
```

### 2. Create Free Donation for Charities
`POST http://localhost:8083/api/listings`
```json
{
  "providerId": "660c1f2b3e4a5c6d7e8f9012",
  "providerName": "Fresh Crust Bakery",
  "title": "Bread Loaves Donation",
  "description": "15 whole grain loaves available for registered charitable shelters.",
  "category": "BAKED_GOODS",
  "quantity": 15,
  "unit": "loaves",
  "originalPrice": 800.00,
  "discountedPrice": 0.00,
  "donation": true,
  "expiryDate": "2026-11-09T22:00:00",
  "collectionDeadline": "2026-11-09T21:00:00",
  "pickupAddress": "78 Galle Road, Kollupitiya, Colombo"
}
```

