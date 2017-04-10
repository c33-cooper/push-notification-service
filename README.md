# push-notification-service
API to distribute notifications to users via the Pushbullet API. The push-notification-service is built in Scala on top of the Play Framework. - https://www.playframework.com/

## Pre-requisites for running the API on a local machine
Scala Build Tool (SBT) - Homebrew or binary installations can be found here - http://www.scala-sbt.org/download.html

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
- 400 if invalid JSON has been supplied
- 200 if record has been succesfully created along with example response body below:


```json
  {
	  "username": "Callum Cooper",
	  "accessToken": "o.40I0GTlbT8r5eOalHefgTnA58Lvg8fP6",
	  "creationTime": "2017-04-10T21:53:08",
	  "numOfNotificationsPushed": 0
  }  
```

### POST /push-notification-service/pushbullet/notification

Push notification to user for provided 'username'.

```json
  {
	"username": "Callum Cooper"
  }
```

Status codes
- 200 ("200 - Push bullet notification sent successfully for $username")
- 400 if invalid JSON has been supplied

NOTE - Access token must have been provided and stored as a record along with username during registration phase mentioned above for PushBullet note notification to be sent.

### GET /push-notification-service/users/user/:username

Request user metadata on a per user basis from the API

###### :username
- Description - Is the username of the store user data collection in the simulated dummy database.
- Type - (String literal)
- Example - 'Joe Bloggs'

200 Json response looks like below - 

```json
{
  "username": "Joe Bloggs",
  "accessToken": "o.40I0GTlfT8r5sOalsefgTnA58Lvg8fP6",
  "creationTime": "2017-04-10T22:23:27",
  "numOfNotificationsPushed": 12
}
```

## Start API locally with SBT 

```
sbt clean "run 9998"
```

App will boot localhost NettyServer listening on port 9998

## Execute unit tests with ScalaTest

```
sbt clean test
```

## Example cURL request locally

- Show all users 

```
curl -X GET -H "Cache-Control: no-cache" -H "Postman-Token: 01a5a568-5f28-be64-4220-6d1a14b2a466" "http://localhost:9998/push-notification-service/users/user/all"
```

- Register user

```
curl -X POST -H "Content-Type: application/json" -H "Cache-Control: no-cache" -H "Postman-Token: 616ed0f4-446f-40d9-d442-50cd574d72c1" -d '{
	"username": "Callum Cooper",
	"accessToken": "o.40I0GTlbT8r5eOalHefgTnA58Lvg8fP6"
}' "http://localhost:9998/push-notification-service/register/user"
```

- Push notification via PushBullet API to user

```
curl -X POST -H "Content-Type: application/json" -H "Cache-Control: no-cache" -H "Postman-Token: c2766c2a-972b-d87d-23c4-5d294700b16c" -d '{
	"username": "Callum Cooper"
}' "http://localhost:9998/push-notification-service/pushbullet/notification"
```

- Request per user metadata

```
curl -X GET -H "Content-Type: application/json" -H "Cache-Control: no-cache" -H "Postman-Token: 3ca16e1f-d29d-5d8d-c235-63cb156ef29b" "http://localhost:9998/push-notification-service/users/user/Joe%20Bloggs"
```


