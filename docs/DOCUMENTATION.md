# Study Room Booking System — REST API Documentation

**Assignment 4 — Project Phase 2**
**Student:** MUTONI Patience
**Repository:** https://github.com/mutonipatience877/study-room-booking-api
**Video walkthrough:** _add your Google Vid share link here_

This phase re-implements the Study Room Booking System as a **Spring Boot
REST API**, moving from the JSF server-rendered UI (Assignment 3) to a
stateless HTTP API that Postman is used to exercise directly. It builds on
the same domain identified previously — see the Assignment 3 documentation
for the Abstract, Problem Statement, Scope, AS-IS/TO-BE models and initial
class diagram, which are unchanged.

## 1. Entities chosen for full CRUD (3 of 3)

All three entities from the original design are implemented with full CRUD
in this phase (the assignment asks for 3; this project only models 3, so
all of them are covered):

| Entity | Relationship | Endpoint |
|---|---|---|
| `Student` | 1 student → many bookings | `/api/students` |
| `StudyRoom` | 1 room → many bookings | `/api/study-rooms` |
| `Booking` | promoted N-N between Student and StudyRoom | `/api/bookings` |

## 2. REST Endpoints

| Method | URL | Description |
|---|---|---|
| GET | `/api/students` | List all students |
| GET | `/api/students/{id}` | Get one student |
| POST | `/api/students` | Create a student |
| PUT | `/api/students/{id}` | Update a student |
| DELETE | `/api/students/{id}` | Delete a student |
| GET | `/api/study-rooms` | List all study rooms |
| GET | `/api/study-rooms/{id}` | Get one study room |
| POST | `/api/study-rooms` | Create a study room |
| PUT | `/api/study-rooms/{id}` | Update a study room |
| DELETE | `/api/study-rooms/{id}` | Delete a study room |
| GET | `/api/bookings` | List all bookings |
| GET | `/api/bookings/{id}` | Get one booking |
| POST | `/api/bookings` | Create a booking |
| PUT | `/api/bookings/{id}` | Update a booking |
| DELETE | `/api/bookings/{id}` | Delete a booking |

`Booking` requests/responses use a DTO (`BookingRequest`/`BookingResponse`)
that references students and rooms by id, avoiding circular JSON and
keeping the payload flat (`studentId`, `studentName`, `roomId`,
`roomNumber`, ...).

## 3. Business Rules Applied (from Assignment 3's Business Requirements)

Every rule is enforced in the **service layer** (`BookingService`,
`StudyRoomService`, `StudentService`), not just the UI, so it holds
regardless of which client calls the API.

| # | Business rule | Enforced by | HTTP result when violated |
|---|---|---|---|
| BR5 | A booking's date cannot be in the past | Bean Validation `@FutureOrPresent` on `BookingRequest.bookingDate` | `400 Bad Request` |
| BR6 | A booking's end time must be after its start time | `BookingService.applyBusinessRules` | `409 Conflict` |
| BR7 | A study room's number must follow the format `A101` / `LIB204` | Bean Validation `@Pattern` on `StudyRoom.roomNumber` | `400 Bad Request` |
| BR9 (new this phase) | A room cannot have two overlapping bookings on the same date (no double-booking) | `BookingRepository.findOverlapping` + `BookingService.applyBusinessRules` | `409 Conflict` |
| BR10 (new this phase) | A room that is `UNDER_MAINTENANCE` or `CLOSED` cannot be booked | `BookingService.applyBusinessRules` | `409 Conflict` |
| BR11 (new this phase) | Student numbers and room numbers must be unique | `StudentService.create` / `StudyRoomService.create` | `409 Conflict` |

All required-field and range/format checks (`@NotBlank`, `@NotNull`,
`@Size`, `@Min`/`@Max`, `@Email`, `@Pattern`) return a structured `400`
response listing every invalid field, produced by
`GlobalExceptionHandler.handleValidation`.

## 4. Error Response Shape

All errors share one JSON shape (`ApiError`):

```json
{
  "timestamp": "2026-09-18T16:15:52.469Z",
  "status": 409,
  "error": "Business Rule Violation",
  "message": "Room A101 is already booked for an overlapping time slot on 2026-09-25",
  "details": []
}
```

`404 Not Found` is used for missing resources, `400 Bad Request` for field
validation failures, `409 Conflict` for business-rule violations.

## 5. Verified with Postman / curl

The full CRUD + business-rule flow below was run end-to-end against a real
PostgreSQL database and the running Spring Boot app during development
(equivalent curl commands shown; the same requests are in
`postman/StudyRoomBookingAPI.postman_collection.json` — import it directly
into Postman):

| Test | Result |
|---|---|
| `POST /api/bookings` with a valid, non-overlapping slot | `201 Created` |
| `POST /api/bookings` overlapping an existing booking on the same room/date | `409 Conflict` |
| `POST /api/bookings` for a room `UNDER_MAINTENANCE` | `409 Conflict` |
| `POST /api/bookings` with `endTime` before `startTime` | `409 Conflict` |
| `POST /api/bookings` missing `purpose` | `400 Bad Request` with field detail |
| `PUT /api/bookings/{id}` to reschedule | `200 OK` |
| `DELETE /api/bookings/{id}` then `GET` the same id | `204 No Content` then `404 Not Found` |
| `POST /api/study-rooms` with an invalid room number (`office1`) | `400 Bad Request` |
| `POST /api/study-rooms` / `POST /api/students` with valid data | `201 Created` |

## 6. GitHub Repository

Public link: https://github.com/mutonipatience877/study-room-booking-api

## 7. Video Walkthrough

Google Vid link (5–10 min, screen + camera, explaining the implementation
and demoing the CRUD + Postman workflow for all three entities):
**_add link here_**
