## You can use Run Configurations in Intellij IDEA 

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


## WS Message Model

```json
{
    "type": enum["info", "reply", "failure"],

    "info_type": enum[ "test_info_type"],

    "failure_type": enum["send_failure", "auth_failure", "illegal_format_failure", "action_type_failure", "auth_token_expired_failure", "missing_field_failure", "unknown_failure"],

    "reply_type": enum["subscription_reply", "authentication_success", "authentication_refresh_success"],

    "channel": const string ["test-user-channel","test-admin-channel"],

    "action": enum["subscribe", "unsubscribe", "send", "refresh_connection"],

    "payload": any object,

    "category": enum[ "test_category_type"]
}
```

## Connect to WebSocket Url (Consumer)

- open the new Websocket tab on postman

- connect to 
    +  `localhost:8080/api/web-socket?Authorization=(YOUR_TOKEN)`

- send request to subscribe a user channel
  + `{
  "channel":"test-user-channel",
  "action":"subscribe"
  }`

- send message to subscribed user channel
    + `{ "payload": "This is test message", "action": "send","infoType": "test_info_type", "category":"test_category_type", "channel":"test-user-channel" }`

- send request to unsubscribe from the user channel
  + `{
    "channel":"test-user-channel",
    "action":"unsubscribe"
    }`

- send request to refresh connection the user channel
  + `{
    "action":"refresh_connection",
    "payload": "token"
    }
    `

- send request to subscribe a admin channel
    + `{
    "channel":"test-admin-channel",
    "action":"subscribe"
    }`

- send message to subscribed admin channel
    + `{ "payload": "This is test message", "action": "send","infoType": "test_info_type", "category":"test_category_type", "channel":"test-admin-channel" }`

- send request to unsubscribe from the admin channel
  + `{
    "channel":"test-admin-channel",
    "action":"unsubscribe"
    }`

- send request to refresh connection the admin channel
  + `{
    "action":"refresh_connection",
    "payload": "token"
    }
    `

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
