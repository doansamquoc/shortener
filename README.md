# SHORTENER - URL SHORTENER
The Shortener make the long URL, link, etc. To the shorten URL, easier to remember with custom URL.
## Tech Stack
- Java 21
- Spring Boot 3
- Postgres + Flyway Migration
- Redis Cache
- Docker
## Folder Structure
Following monolith-based structure: 
```text
├── main
│   ├── java
│   │   ├── controller  # Handle logic request/response
│   │   ├── service     # Business logic
│   │   ├── entity      # Schema
│   │   ├── .env
│   │   ├── docker-compose.yml
│   │   ├── Dockerfile
│   │   └── ...
│   ├── resources
│   │   ├── db.migration    # Flyway migration
│   │   │   ├── V1__init_schema.sql
│   │   │   └── ...
│   │   ├── templates
│   │   │   ├── mails   # Welcome, Reset password, etc. template
│   │   │   └── ...
└── └── └── ...
```
## Setup & Installation
### Requirement
- JDK 21
- Maven
- Docker (or PostgreSQL & Redis)
- Google Client ID & Secret
- Google Gmail App Passowrd (Username & Password)
### Running App Locally
1. Configure Environment Variables<br>
Create file name `.env`. After copy and edit:
```.dotenv
# Application
SECRET_KEY= # Must be at least 32 characters

# Database
DB_URL= # DB Url
DB_PASSWORD= # DB password
DB_USERNAME= # DB username

# Redis
REDIS_HOST= # Redis host
REDIS_PASSWORD= # Redis password
REDIS_PORT= # Redis port. Default 6379

# Google
GOOGLE_CLIENT_ID= # Get at console.cloud.google
GOOGLE_CLIENT_SECRET= # Get at console.cloud.google

# Mail
MAIL_USERNAME= # Create an app password and paste username here
MAIL_PASSWORD= # Create an app password and paste passowrd here
```
2. Start The Application<br>
Simply click run in your IDE. If you use Docker, please start docker first.
3. Access Endpoints<br>
- Swagger UI: http://localhost:8080/swagger-ui/index.html
- Health Check: http://localhost:8080/actuator/health
4. Online Demo
- Demo: https://shortener-wz64.onrender.com
- Swagger UI: https://shortener-wz64.onrender.com/swagger-ui/index.html <br/>
  *You may have to wait for Render to start :D*
