# profile-management

### UserType Service

The UserType Service manages user types including Service Providers and Customers.

### API Endpoints

1. Get User Type by ID
```http request
GET /api/user-type/{id}
```
Response:
- ```type:``` 1 = Service Provider, 2 = Customer
```json
{
  "type": 1,
  "description": "Service Provider",
  "enabled": true
}
```
---
### MySQL Database schema
```sql
CREATE TABLE IF NOT EXISTS user_type (
    did INT AUTO_INCREMENT PRIMARY KEY,
    type INT NOT NULL, -- 1 = Service Provider, 2 = Customer
    description VARCHAR(50) NOT NULL,
    enabled BOOLEAN NOT NULL
);
```
### Build & Run Locally
1. Build
```
mvn clean install
```
2. Run
```
mvn spring-boot:run
```


