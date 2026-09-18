# Study Room Booking API

Assignment 4 — Project Phase 2. A Spring Boot REST API with full CRUD for
all 3 entities (**Student**, **StudyRoom**, **Booking**), enforcing the
business rules identified for the Study Room Booking System, tested with
Postman.

See [`docs/DOCUMENTATION.md`](docs/DOCUMENTATION.md) for the full
documentation (entities, endpoints, business rules, error format, and
verified test results).

## Tech stack

- Java 21, Spring Boot 3.3.5
- Spring Web (REST controllers)
- Spring Data JPA + Hibernate — persistence
- Spring Boot Validation (Bean Validation / Hibernate Validator)
- PostgreSQL
- Maven

## Prerequisites

- **Java 21+** (`java -version`)
- **Maven** (`mvn -v`)
- **PostgreSQL** running locally
- **Postman** (https://www.postman.com/downloads/)

## Step-by-step setup

### Step 1 — Create the database

```sql
CREATE DATABASE studyroom_api_db;
```

### Step 2 — Configure your database credentials

Edit `src/main/resources/application.properties` if your Postgres
username/password differ from the defaults:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/studyroom_api_db
spring.datasource.username=postgres
spring.datasource.password=postgres
```

`spring.jpa.hibernate.ddl-auto=update` creates/updates the `student`,
`study_room` and `booking` tables automatically on startup.

### Step 3 — Build and run

```bash
mvn clean spring-boot:run
```

The API starts on **http://localhost:8081**. On first startup it seeds 3
students and 3 study rooms (one of them `UNDER_MAINTENANCE`, useful for
testing the "can't book an unavailable room" rule).

### Step 4 — Test with Postman

Import [`postman/StudyRoomBookingAPI.postman_collection.json`](postman/StudyRoomBookingAPI.postman_collection.json)
into Postman (**Import → File**). It includes:
- Full CRUD requests for Students, Study Rooms and Bookings.
- Negative test requests that intentionally violate each business rule, so
  you can see the `400`/`409` responses:
  - Overlapping booking on the same room/date → `409`
  - Booking a room that's `UNDER_MAINTENANCE` → `409`
  - `endTime` before `startTime` → `409`
  - Missing a required field (`purpose`) → `400`
  - Study room number in the wrong format (e.g. `office1` instead of `A101`) → `400`

The collection variable `baseUrl` defaults to `http://localhost:8081` —
change it in the collection's Variables tab if you run the app on a
different port.

### Step 5 — Try it with curl (optional, no Postman needed)

```bash
curl http://localhost:8081/api/students

curl -X POST http://localhost:8081/api/bookings \
  -H "Content-Type: application/json" \
  -d '{"studentId":1,"roomId":1,"bookingDate":"2026-09-25","startTime":"10:00:00","endTime":"11:00:00","purpose":"Group study session","status":"CONFIRMED"}'
```

### Step 6 — Verify the generated tables

```bash
psql -U postgres -d studyroom_api_db -c "\dt"
psql -U postgres -d studyroom_api_db -c "\d booking"
```

### Step 7 — Push to GitHub and add the link to the documentation

```bash
git init
git add .
git commit -m "Study Room Booking API - CRUD for Student, StudyRoom and Booking"
git branch -M main
git remote add origin https://github.com/<your-username>/study-room-booking-api.git
git push -u origin main
```

Then paste the repository URL into `docs/DOCUMENTATION.md` (Section 6).

### Step 8 — Record the video and add the link

Record a 5–10 minute screen + camera walkthrough (Google Vid) covering:
1. The implementation/workflow of the CRUD + business rules built here.
2. A live Postman demo of at least one success case and one business-rule
   failure (400/409) per entity.

Paste the share link into `docs/DOCUMENTATION.md` (Section 7).

## Project structure

```
study-room-booking-api/
├── pom.xml
├── README.md
├── docs/DOCUMENTATION.md
├── postman/StudyRoomBookingAPI.postman_collection.json
└── src/main
    ├── java/com/studyroom/api
    │   ├── model         (Student, StudyRoom, Booking, RoomStatus, BookingStatus)
    │   ├── repository     (StudentRepository, StudyRoomRepository, BookingRepository)
    │   ├── dto            (BookingRequest, BookingResponse)
    │   ├── service        (StudentService, StudyRoomService, BookingService - business rules)
    │   ├── controller     (StudentController, StudyRoomController, BookingController)
    │   ├── exception       (ResourceNotFoundException, BusinessRuleException, ApiError, GlobalExceptionHandler)
    │   └── StudyRoomApiApplication.java
    └── resources/application.properties
```
