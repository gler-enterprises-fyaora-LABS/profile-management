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
    type VARCHAR(50) NOT NULL, -- TYPE 1 = Registration Service Provider, TYPE 2 = Customer Provider
    description VARCHAR(50) NOT NULL,
    enabled BOOLEAN NOT NULL
);      
```
### Flyway migration errors
- For a **FlywayValidateException** or a **Migration checksum mismatch**, either:
    1. Create a new Flyway migration file in  
       ```src/main/resources/scripts/```
    2. Drop the **local** database: ⚠️ **Dangerous:** This **deletes all data**
       ```sql
       DROP DATABASE your_database_name;
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


