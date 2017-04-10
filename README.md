# push-notification-service
API to distribute notifications to users via the Pushbullet API

## Production Endpoints

### GET /push-notification-service/users/user/all

Request all users records from simulated server-side database collection.

Responds test-data records that live in on the server for the life span of the application.

```json
  [
  {
    "username": "Jack Smith",
    "accessToken": "o.40I0GTlbT8r5eOalHejgTnA66Lvg8fP2",
    "creationTime": "2017-04-10T21:57:26",
    "numOfNotificationsPushed": 40
  },
  {
    "username": "Joe Bloggs",
    "accessToken": "o.40I0GTlfT8r5sOalsefgTnA58Lvg8fP6",
    "creationTime": "2017-04-10T21:57:26",
    "numOfNotificationsPushed": 12
  },
  {
    "username": "John Jones",
    "accessToken": "o.40I0GTlbT8r7eOalHejgTnA66Lvg8fP3",
    "creationTime": "2017-04-10T21:57:26",
    "numOfNotificationsPushed": 99
  }
] 
```

Status codes 
- 200 Ok(Success)

### POST /push-notification-service/register/user

Register a new user with the API. See example request body below:

```json
  {
	"username": "Callum Cooper",
	"accessToken": "o.40I0GTlbT8r5eOalHefgTnA58Lvg8fP6"
  }
```

Responds with status code:
- 200 if record has been succesfully created along with response body below:


```json
  {
	  "username": "Callum Cooper",
	  "accessToken": "o.40I0GTlbT8r5eOalHefgTnA58Lvg8fP6",
	  "creationTime": "2017-04-10T21:53:08",
	  "numOfNotificationsPushed": 0
  }  
```
