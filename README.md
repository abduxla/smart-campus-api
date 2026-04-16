````markdown
# Smart Campus Sensor & Room Management API

## Module Information
- **Module:** 5COSC022W – Client-Server Architectures
- **Coursework Title:** Smart Campus Sensor & Room Management API
- **Student:** Abdullah

## Overview
This project is a RESTful API developed for the Client-Server Architectures coursework using **JAX-RS**, **Jersey**, and an embedded **Grizzly HTTP Server**.

The system simulates a Smart Campus environment where rooms, sensors, and sensor reading history can be managed through REST endpoints. The API supports resource discovery, room creation and deletion, sensor registration and filtering, nested historical readings, custom exception handling, and centralized logging.

The project uses **in-memory data structures only** such as `HashMap` and `ArrayList`. No external database is used.

## Features
The API supports the following features:

- Discovery endpoint
- Room management
- Sensor creation and linking to rooms
- Sensor filtering by type
- Nested sensor reading history
- Parent sensor `currentValue` updates after new readings
- Custom exception mapping
- Request and response logging

## Technology Stack
- Java
- Maven
- JAX-RS
- Jersey
- Grizzly HTTP Server
- Apache NetBeans

## Project Structure
Main parts of the project:

- `Room` model
- `Sensor` model
- `SensorReading` model
- `DataStore` for in-memory storage
- `ApplicationConfig`
- `DiscoveryResource`
- `RoomResource`
- `SensorResource`
- `SensorReadingResource`
- Custom exceptions
- Exception mappers
- Logging filter

## In-Memory Data Design
The API stores all runtime data in memory using Java collections.

Main collections:
- `Map<String, Room> rooms`
- `Map<String, Sensor> sensors`
- `Map<String, List<SensorReading>> readings`

This means that if the server is restarted, runtime-created sensors and readings are reset.

Seeded rooms:
- `ENG-102`
- `LIB-301`

## How to Build the Project
### Using Apache NetBeans
1. Open the project in Apache NetBeans.
2. Right-click the project.
3. Click **Clean and Build**.

### Using Maven
```bash
mvn clean install
````

## How to Run the Server

### Using Apache NetBeans

1. Open `Main.java`.
2. Right-click `Main.java`.
3. Click **Run File**.

### Local Base URL

The API currently runs locally at:

```text
http://localhost:9095/api
```

## Main Endpoints

### Discovery

* `GET /api`

### Rooms

* `GET /api/rooms`
* `POST /api/rooms`
* `GET /api/rooms/{roomId}`
* `DELETE /api/rooms/{roomId}`

### Sensors

* `GET /api/sensors`
* `POST /api/sensors`
* `GET /api/sensors?type=CO2`

### Sensor Reading History

* `GET /api/sensors/{sensorId}/readings`
* `POST /api/sensors/{sensorId}/readings`

## Sample curl Commands

### 1. Get API discovery info

```bash
curl http://localhost:9095/api
```

### 2. Get all rooms

```bash
curl http://localhost:9095/api/rooms
```

### 3. Create a new room

```bash
curl -X POST http://localhost:9095/api/rooms \
  -H "Content-Type: application/json" \
  -d "{\"id\":\"SCI-201\",\"name\":\"Science Lab\",\"capacity\":35,\"sensorIds\":[]}"
```

### 4. Get all sensors

```bash
curl http://localhost:9095/api/sensors
```

### 5. Create a new sensor

```bash
curl -X POST http://localhost:9095/api/sensors \
  -H "Content-Type: application/json" \
  -d "{\"id\":\"CO2-001\",\"type\":\"CO2\",\"status\":\"ACTIVE\",\"currentValue\":420.5,\"roomId\":\"ENG-102\"}"
```

### 6. Filter sensors by type

```bash
curl "http://localhost:9095/api/sensors?type=CO2"
```

### 7. Add a reading to a sensor

```bash
curl -X POST http://localhost:9095/api/sensors/CO2-001/readings \
  -H "Content-Type: application/json" \
  -d "{\"id\":\"READ-001\",\"timestamp\":1711980000000,\"value\":430.2}"
```

### 8. Get sensor reading history

```bash
curl http://localhost:9095/api/sensors/CO2-001/readings
```

## Error Handling

The API includes custom exception handling for the following scenarios:

* **409 Conflict**
  Returned when attempting to delete a room that still has sensors assigned to it.

* **400 Bad Request**
  Returned when a sensor is posted with a `roomId` that does not exist.

* **403 Forbidden**
  Returned when a client attempts to add a reading to a sensor with status `MAINTENANCE`.

* **500 Internal Server Error**
  Returned by a global fallback exception mapper for unexpected runtime failures.

## Logging

The API includes a custom logging filter that logs:

* HTTP method and URI for incoming requests
* final HTTP status code for outgoing responses

This logging is displayed in the NetBeans output window during runtime.

## Testing Summary

The following features were tested successfully:

* discovery endpoint
* room creation
* room retrieval
* room deletion
* sensor creation
* sensor filtering by query parameter
* nested reading history
* updating sensor `currentValue` after reading creation
* 409 conflict for deleting a room with sensors
* 400 bad request for invalid linked room
* 403 forbidden for maintenance sensor reading attempts

## Design Decisions

### Why in-memory data structures were used

The coursework explicitly required the use of data structures like `HashMap` and `ArrayList` instead of database technologies. This keeps the project lightweight and aligned with the assignment requirements.

### Why a versioned API structure is important

A versioned API structure helps keep endpoints organized and makes future changes easier to manage without breaking existing clients.

### Why custom exception mappers were used

Custom exception mappers make the API safer and cleaner by preventing raw stack traces or default server error pages from being exposed to clients.

## Limitations

This implementation has some intentional limitations due to coursework scope:

* data is not persistent
* runtime-created data is lost when the server restarts
* authentication and authorization are not implemented
* the API is designed for local coursework demonstration rather than production deployment

---

# Coursework Question Answers

## Part 1 – Service Architecture & Setup

### 1. Resource lifecycle in JAX-RS

By default, JAX-RS resource classes are typically managed per request, meaning a new resource instance is commonly created for each incoming request. This helps avoid accidental data sharing inside instance variables. However, in this project, the actual application data is stored in shared in-memory collections inside `DataStore`, so the main concurrency concern is not the resource instance itself but the shared maps and lists. Because shared in-memory collections can be accessed by multiple requests, they must be handled carefully to avoid race conditions, inconsistent updates, or accidental data loss.

### 2. Why hypermedia is valuable in REST

Hypermedia is useful because responses can guide the client toward other valid actions and related resources. Instead of forcing the client developer to memorize or hard-code every endpoint path, the API can return discoverable links or resource maps. This makes the API easier to navigate, more self-descriptive, and more adaptable to future changes.

## Part 2 – Room Management

### 1. Returning IDs vs full room objects

Returning only IDs reduces response size and network usage, which can be useful when the client only needs identifiers. However, it shifts more work to the client because the client must then make extra requests to fetch details. Returning full room objects increases payload size but is often more convenient because the client immediately receives useful metadata such as room name, capacity, and assigned sensor IDs.

### 2. Is DELETE idempotent in this implementation?

Yes, the DELETE operation is idempotent in terms of system state. If a room is successfully deleted once, sending the same DELETE request again does not delete additional resources or change the final state further. The first request removes the room, and later repeated requests simply return that the room is not found. The response may change, but the final server state remains the same after the first successful deletion.

## Part 3 – Sensor Operations & Linking

### 1. What happens if a client sends the wrong content type?

The sensor creation method uses `@Consumes(MediaType.APPLICATION_JSON)`, which means the API expects JSON input. If a client sends data in a different format such as `text/plain` or `application/xml`, JAX-RS may fail to find a matching message body reader. In a correctly configured setup, this usually results in an HTTP `415 Unsupported Media Type` response because the server cannot process the request body in that format.

### 2. Why query parameters are better for filtering

Using a query parameter such as `/sensors?type=CO2` is generally better for filtering because it keeps the main resource collection path unchanged and treats the type as a search/filter condition rather than as a different resource. This is more flexible, easier to extend with multiple filters later, and more consistent with standard REST API design for collection filtering.

## Part 4 – Deep Nesting with Sub-Resources

### 1. Benefits of the sub-resource locator pattern

The sub-resource locator pattern keeps complex APIs cleaner by splitting nested logic into dedicated classes. Instead of placing every nested path and reading-related method inside one large `SensorResource` class, the API delegates reading history behavior to `SensorReadingResource`. This improves readability, makes the code easier to maintain, and keeps responsibilities separated. It also scales better when an API grows and gains more deeply nested features.

### 2. Historical data and consistency

In this implementation, `SensorReadingResource` handles both fetching the reading history and appending new readings. After a successful reading POST, the parent sensor’s `currentValue` is updated immediately. This is important because it keeps the historical record and the current sensor state synchronized, so clients do not see stale or inconsistent values across different endpoints.

## Part 5 – Advanced Error Handling, Exception Mapping & Logging

### 1. Why HTTP 422 is often more accurate than 404

HTTP `422 Unprocessable Entity` is often considered more accurate when the JSON body itself is structurally valid, but one of its internal references is invalid. In this case, the request body is understood, but the linked `roomId` does not exist. A `404 Not Found` usually refers to the URI resource itself being missing, while `422` focuses on a semantic problem inside the submitted payload. In this project, `400` was used as an acceptable alternative.

### 2. Cybersecurity risks of exposing stack traces

Exposing Java stack traces to external users is dangerous because it leaks internal implementation details. An attacker could learn class names, package names, library versions, method names, file structures, and the exact places where exceptions occurred. This information can help attackers identify weak points, guess framework behavior, target specific known vulnerabilities, and build more precise attacks against the system.

### 3. Why filters are better for cross-cutting concerns like logging

Filters are better for cross-cutting concerns because they centralize behavior that applies to many endpoints. Instead of manually writing logging statements inside every resource method, a request/response filter applies the same rule consistently to the whole API. This reduces duplication, improves maintainability, and ensures no endpoint is forgotten.

## Notes for Demonstration

For demonstration, the API can be tested through Postman using:

* discovery endpoint
* room management endpoints
* sensor creation and filtering
* nested reading history
* custom error responses

## Author

Abdullah
