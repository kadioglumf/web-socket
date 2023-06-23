## Run the project 
- `mvn clean compile`
- `mvn spring-boot:run`

## Login and get user token

- `curl --location 'localhost:8080/api/auth/login' \
  --header 'Content-Type: application/json' \
  --data-raw '{
  "email":"asd@mail.com",
  "password": "password"
  }'`

## Login and get admin token

- `curl --location 'localhost:8080/api/auth/login' \
  --header 'Content-Type: application/json' \
  --data-raw '{
  "email":"admin@mail.com",
  "password": "password"
  }'`

## Connect to WebSocket Url (Consumer)

- open the new Websocket tab on postman

- connect to `localhost:8080/api/web-socket?Authorization=(YOUR_TOKEN)`

- send request to subscribe a channel
  + `{
  "channel":"test-user-channel",
  "action":"subscribe"
  }`

- send request to subscribe a admin channel
`{
    "channel":"test-admin-channel",
    "action":"subscribe"
    }`


## Send Message to test-user-channel (to everybody)

-   `curl --location 'localhost:8080/api/web-socket/publish' \
    --header 'Content-Type: application/json' \
    --data '{
    "payload": "Test Message",
    "infoType": "test_info_type",
    "category":"test_category_type",
    "channel":"test-user-channel"
    }'`

## Send Message to test-user-channel (specified user)

- `curl --location 'localhost:8080/api/web-socket/publish' \
  --header 'Content-Type: application/json' \
  --data '{
  "payload": "Test Message",
  "infoType": "test_info_type",
  "category":"test_category_type",
  "channel":"test-user-channel",
  "sendingType": "SPECIFIED_USER",
  "userId": 2
  }'`

## Send Message to test-user-channel (role based)

- `curl --location 'localhost:8080/api/web-socket/publish' \
  --header 'Content-Type: application/json' \
  --data '{
  "payload": "Test Message2",
  "infoType": "test_info_type",
  "category":"test_category_type",
  "channel":"test-user-channel",
  "sendingType": "ROLE_BASED",
  "role":"ROLE_USER"
  }'`
