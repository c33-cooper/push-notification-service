package models

import org.joda.time.{DateTime, DateTimeZone}
import play.api.libs.json._

/**
  * Created by callumcooper on 06/04/2017.
  */

/**
  * UserRequest model
  * @param username
  * @param accessToken
  */
case class UserRegisterRequest(username: String,
                       accessToken: String)

/**
  * UserRegisterRequest data-access-object (DAO)
  */
object UserRegisterRequest {
  implicit val userRequestFormat: Format[UserRegisterRequest] = Json.format[UserRegisterRequest]
}

/**
  * UserPushNotificationRequest model
  * @param username
  */
case class UserPushNotificationRequest(username: String)

/**
  * UserPushNotificationRequest data-access-object (DAO)
  */
object UserPushNotificationRequest {
  implicit val userPushNotificationRequestFormat: Format[UserPushNotificationRequest] = Json.format[UserPushNotificationRequest]
}

/**
  * User model
  * @param username
  * @param accessToken
  * @param creationTime
  * @param numOfNotificationsPushed
  */
case class User(username: String,
                accessToken: String,
                creationTime: DateTime,
                var numOfNotificationsPushed: Int)

object User {
  val datePattern = "yyyy-MM-dd'T'HH:mm:ss"
  implicit val dateFormat = Format[DateTime]( Reads.jodaDateReads(datePattern), Writes.jodaDateWrites(datePattern) )

  implicit val userResponseFormat: Format[User] = Json.format[User]
}

/**
  * User database collection with dummy data, this would usually
  * sit in an external data-store microservice where data would
  * be pulled via HTTP requests.
  */
object UsersCollection {

  // All users data
  var users = Set(
    User(username = "Joe Bloggs",
                 accessToken = "o.40I0GTlfT8r5sOalsefgTnA58Lvg8fP6",
                 creationTime = DateTime.now.withZone(DateTimeZone.UTC),
                 numOfNotificationsPushed = 12),
    User(username = "Jack Smith",
                 accessToken = "o.40I0GTlbT8r5eOalHejgTnA66Lvg8fP2",
                 creationTime = DateTime.now.withZone(DateTimeZone.UTC),
                 numOfNotificationsPushed = 40),
    User(username = "John Jones",
                 accessToken = "o.40I0GTlbT8r7eOalHejgTnA66Lvg8fP3",
                 creationTime = DateTime.now.withZone(DateTimeZone.UTC),
                 numOfNotificationsPushed = 99
    )
  )

  // Find all users sorted by username
  def findAll = this.users.toList.sortBy(_.username)

  // Find users by username
  def findUsersByUsername(username: String) = this.users.find(_.username == username)

  // Find access token by Username
  def findAccessTokenByUsername(username: String): String = findUsersByUsername(username = username).map { user =>
      user.accessToken
    }.getOrElse("")

  // Increment numOfNotificationsPushed by Username
  def incrementNumOfNotificationsPushedByUsername(username: String) = findUsersByUsername(username = username).get.numOfNotificationsPushed += 1

  // PUT users to data store.
  def putUser(user: User) = {
    this.users += user
  }
}
