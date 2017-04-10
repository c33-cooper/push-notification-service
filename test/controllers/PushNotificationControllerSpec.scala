package controllers

import akka.stream.Materializer
import connectors.PushButtonConnector
import org.joda.time.{DateTime, DateTimeZone}
import org.scalatest.mock._
import org.scalatestplus.play.OneAppPerSuite
import play.api.libs.json.{JsValue, Json}
import play.api.test.FakeRequest

import scala.concurrent.ExecutionContext

/**
  * Created by callumcooper on 10/04/2017.
  */
class PushNotificationControllerSpec extends UnitSpec with MockitoSugar with OneAppPerSuite {

  implicit lazy val materializer: Materializer = app.materializer
  implicit val executionContext = ExecutionContext.Implicits.global

  "registerUser" should "return some valid body JSON" in new TestCase {
    val result = TestController.registerUser().apply(registerUserFakeRequest)

    result map { res => res.body shouldBe registerUserJsonPayloadResponseBody }
  }

  "sortedUsers" should "return all dummy data-store users" in new TestCase {
    val result = TestController.sortedUsers().apply(sortedUserFakeRequest)

    result map { res => res.body shouldBe sortedUsersJsonPayloadResponseBody }
  }

  "userByUsername" should "return user metadata for provided username" in new TestCase {
    val result = TestController.userByUsername(username = username).apply(userByUsernameFakeRequest)

    result map { res => res.body shouldBe userByUsernameJsonPayloadResponseBody }
  }

  "pushBulletNotification" should "respond with success message" in new TestCase {
    val result = TestController.pushBulletNotification().apply(pushBulletNotificationFakeRequest)

    result map { res => res.body shouldBe pushBulletNotificationJsonPayloadResponseBody }
  }
}

class TestCase {

  // Register user HTTP post request payload body test data
  val registerUserJsonPayloadPostRequestBody: JsValue = Json.parse(
    s"""
       |{
       |  "username": "Callum Cooper",
       |  "accessToken": "o.40I0GTlbT8r5eOalHefgTnA58Lvg8fP6"
       |}
       """.stripMargin)

  // Register user HTTP post response payload body test data
  val registerUserJsonPayloadResponseBody: JsValue = Json.parse(
    s"""
       |{
       |  "username": "Callum Cooper",
       |  "accessToken": "o.40I0GTlbT8r5eOalHefgTnA58Lvg8fP6",
       |  "creationTime": "${DateTime.now().withZone(DateTimeZone.UTC)}",
       |  "numOfNotificationsPushed": 0
       |}
     """.stripMargin
  )

  // Sorted users response body
  val sortedUsersJsonPayloadResponseBody: JsValue = Json.parse(
    """
      |[
      |  {
      |    "username": "Jack Smith",
      |    "accessToken": "o.40I0GTlbT8r5eOalHejgTnA66Lvg8fP2",
      |    "creationTime": "2017-04-10T19:19:15",
      |    "numOfNotificationsPushed": 40
      |  },
      |  {
      |    "username": "Joe Bloggs",
      |    "accessToken": "o.40I0GTlfT8r5sOalsefgTnA58Lvg8fP6",
      |    "creationTime": "2017-04-10T19:19:15",
      |    "numOfNotificationsPushed": 12
      |  },
      |  {
      |    "username": "John Jones",
      |    "accessToken": "o.40I0GTlbT8r7eOalHejgTnA66Lvg8fP3",
      |    "creationTime": "2017-04-10T19:19:15",
      |    "numOfNotificationsPushed": 99
      |  }
      |]
    """.stripMargin
  )

  // UserByUsername response body
  val userByUsernameJsonPayloadResponseBody: JsValue = Json.parse(
    """
      |[
      |  {
      |    "username": "Jack Smith",
      |    "accessToken": "o.40I0GTlbT8r5eOalHejgTnA66Lvg8fP2",
      |    "creationTime": "2017-04-10T19:19:15",
      |    "numOfNotificationsPushed": 40
      |  }
      |]
    """.stripMargin
  )

  // PushBulletNotification request body
  val pushBulletNotificationJsonPayloadRequestBody: JsValue = Json.parse(
    """
      |{
      |	 "username": "Jack Smith"
      |}
    """.stripMargin
  )

  // PushBulletNotification response body
  val pushBulletNotificationJsonPayloadResponseBody: JsValue = Json.parse(
    """
      | "200 - Push bullet notification sent successfully for Jack Smith"
    """.stripMargin
  )

  // userByUsername
  val username: String = "Jack Smith"

  // Spec HTTP fake requests
  val registerUserFakeRequest = FakeRequest().withJsonBody(registerUserJsonPayloadPostRequestBody)
  val sortedUserFakeRequest = FakeRequest()
  val userByUsernameFakeRequest = FakeRequest()
  val pushBulletNotificationFakeRequest = FakeRequest().withJsonBody(pushBulletNotificationJsonPayloadRequestBody)

  // Spec mocks
  val pushButtonConnectorMock = MockitoSugar.mock[PushButtonConnector]

  // Spec instances
  object TestController extends PushNotificationController(pushButtonConnector = pushButtonConnectorMock) {
  }
}
