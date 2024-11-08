## You can use Run Configurations in Intellij IDEA 

## Login and get user token

- `curl --location --request POST 'localhost:8081/websocket/auth/login' \
  --header 'Content-Type: application/json' \
  --data-raw '{
  "email":"asd@mail.com",
  "password": "user"
  }'`

## Login and get admin token

- `curl --location --request POST 'localhost:8081/websocket/auth/login' \
  --header 'Content-Type: application/json' \
  --data-raw '{
  "email":"admin@mail.com",
  "password": "admin"
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
    +  `localhost:8081/websocket/connect?Authorization={YOUR_JWT_TOKEN}`


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

## Send message to subscribed channel (to everybody)

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

## Send Message to subscribed channel (specified user)

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

## Send Message to subscribed channel (role based)

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
