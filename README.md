# ElectroStore - E-commerce Platform

![ElectroStore Banner](https://via.placeholder.com/1200x400?text=ElectroStore+Banner)

ElectroStore is a robust e-commerce platform tailored for electronic goods, built with **Spring Boot**, **PostgreSQL**, and **Keycloak**. This project provides a scalable backend for managing users, products, orders, and secure authentication.

## ✨ Features

- **🔒 Secure Authentication & Authorization**  
  Integrated with Keycloak for OAuth2/OpenID Connect user management, role-based access, and secure API endpoints.
  
- **📦 Product Management**  
  CRUD operations for electronic products, including categories, pricing, inventory, and search functionality.

- **🛒 Shopping Cart**  
  Users can add/remove items, adjust quantities, and save carts for later.

- **📝 Order Processing**  
  Track orders with status updates (Pending, Shipped, Delivered) and integrated payment gateway (e.g., Stripe/PayPal).

- **👥 User Management**  
  Self-registration, profile management, and admin-controlled user roles (Admin/Customer).

- **📊 Admin Dashboard**  
  Analytics dashboard for admins to monitor sales, inventory, and user activity.

## 🛠️ Technologies Used

**Backend**
- Spring Boot 3.x
- Spring Security
- Spring Data JPA
- Keycloak (Authentication)

**Database**
- PostgreSQL

**Tools**
- Maven
- Docker (for Keycloak & PostgreSQL)
- Postman (API Testing)
- Swagger/OpenAPI (Documentation)

## 🚀 Installation & Setup

### Prerequisites
- Java 17+
- Docker (for Keycloak & PostgreSQL)
- Maven 3.8+

### Steps
1. **Clone the Repository**
   ```bash
   git clone https://github.com/yourusername/electrostore.git
   cd electrostore
