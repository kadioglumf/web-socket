# web-socket

Spring Boot WebSocket Example

---

## Table of Contents

1. [About](#about)
2. [Features](#features)
3. [Architecture](#architecture)
4. [Setup & Running](#setup--running)
5. [API & Message Model](#api--message-model)
6. [Using WebSocket](#using-websocket)
7. [Authentication](#authentication)
8. [Examples](#examples)

---

## About

This project demonstrates a **real-time communication** system using **Spring Boot WebSocket**.  
It supports JWT-based authentication, channel subscriptions/unsubscriptions, role-based messaging, and direct user messaging.

---

## Features

- Establish WebSocket connection
- Subscribe to a channel
- Unsubscribe from a channel
- JWT-based authentication
- Role-based messaging
- Send messages to specific users
- Broadcast messages to all users

---

## Architecture

- **Backend:** Java + Spring Boot
- **Security:** JWT Authentication
- **Message Format:** JSON
- Supports **channels**, **roles**, and **message types** (info, reply, failure...)

---

## Setup & Running

### Requirements

- Java 17+
- Maven
- Docker & Docker Compose (optional)

### Local Run

1. Clone the project:
   ```bash
   git clone https://github.com/kadioglumf/web-socket.git
   cd web-socket
   ```

2. Build with Maven:
   ```bash
   mvn clean install
   ```

3. Run the application:
   ```bash
   mvn spring-boot:run
   ```

### Run with Docker

From project root:

```bash
docker compose build
docker compose up -d
```

---

## API & Message Model

Messages follow this JSON structure:

```json
{
  "type": "info" | "reply" | "failure",
  "info_type": "test_info_type",
  "failure_type": "send_failure" | "auth_failure" | "illegal_format_failure" | "action_type_failure" | "auth_token_expired_failure" | "missing_field_failure" | "unknown_failure",
  "reply_type": "subscription_reply" | "authentication_success" | "authentication_refresh_success",
  "channel": "test-user-channel" | "test-admin-channel",
  "action": "subscribe" | "unsubscribe" | "send" | "refresh_connection",
  "payload": {},
  "category": "test_category_type",
  "sendingType": "ALL" | "SPECIFIED_USER" | "ROLE_BASED",
  "userId": 1,
  "role": "ROLE_USER"
}
```

---

## Using WebSocket

- Connection URL:
  ```
  ws://localhost:8081/websocket/connect?Authorization={YOUR_JWT_TOKEN}
  ```

- Can be tested with Postman or any WebSocket client.

- Supported actions:

  | Action               | Description                          |
    |----------------------|--------------------------------------|
  | `subscribe`          | Subscribe to a given channel         |
  | `subscribe_all`      | Subscribe to a all channels selected by the user |
  | `unsubscribe`        | Unsubscribe from a channel           |
  | `send`               | Send a message                       |
  | `refresh_connection` | Refresh JWT token for the connection |

---

## Authentication

1. Obtain JWT token via login endpoint:
   ```bash
   curl --location --request POST 'localhost:8081/websocket/auth/login'      --header 'Content-Type: application/json'      --data-raw '{ "email":"user@mail.com", "password": "user" }'
   ```

2. Use the returned token in WebSocket connection with `Authorization` parameter.

3. Expired or invalid tokens will trigger `failure` messages.

---

## Examples

- Broadcast message to all users
- Send message to a specific user
- Role-based messaging


### Subscribe and unsubscribe from a channel

- send request to subscribe a admin channel
    + ```json 
      {
        "channel":"test-admin-channel",
        "action":"subscribe"
      }
      ```


- send request to unsubscribe from the admin channel
    + ```json 
      {
      "channel":"test-admin-channel",
      "action":"unsubscribe"
      }
      ```
  
- send request to refresh connection the admin channel
    + ```json 
      {
      "action": "refresh_connection",
      "payload": "token"
      }
      ```

### Send message to subscribed channel (to everybody)

``` json 
    { 
      "payload": "This is test message admin",
      "action": "send",
      "infoType": "test_info_type",
      "category": "test_category_type",
      "channel": "test-admin-channel",
      "sendingType": "ALL" //optional
    }
```

### Send Message to subscribed channel (specified user)

```json 
    {
      "payload": "Test Message",
      "infoType": "test_info_type",
      "category":"test_category_type",
      "channel":"test-user-channel",
      "sendingType": "SPECIFIED_USER",
      "userId": 1
    }
```

### Send Message to subscribed channel (role based)

```json 
  {
    "payload": "Test Message2",
    "infoType": "test_info_type",
    "category":"test_category_type",
    "channel":"test-user-channel",
    "sendingType": "ROLE_BASED",
    "role":"ROLE_USER"
  }
```
