# profile-management

The Profile Management module provides the following features:

1. Waitlist requests raised by customers
2. Waitlist requests raised by investors
3. Waitlist requests raised by service providers


## API Endpoints

Refer to Swagger UI for more information

```
/swagger-ui/index.html
```

## MySQL Database schema

The Flyway plugin will create all required database tables when the application starts. <br/><br/>
**Note:** For local development, please ensure that a schema named GlerProfile exists.
If you prefer to use a different schema name, update the reference in the application-dev.properties file.
However, **DO NOT CHANGE** the existing schema name in the **application-aws.properties** file.

## Flyway migration errors
- For a **FlywayValidateException** or a **Migration checksum mismatch**, either:
    1. Create a new Flyway migration file in  
       ```src/main/resources/scripts/```
    2. Drop the **local** database: ⚠️ **Dangerous:** This **deletes all data**
       ```sql
       DROP DATABASE your_database_name;
        ```
## Build & Run Locally
1. Build
```
mvn clean install
```
2. Run
```
mvn spring-boot:run
```


