package connectors

import javax.inject.Inject
import config.GlobalConfig
import models.UsersCollection
import play.api.libs.json.{JsValue, Json}
import play.api.libs.ws.WSClient
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
  * Created by callumcooper on 06/04/2017.
  */
class PushButtonConnector @Inject() (implicit val ws: WSClient, globalConfig: GlobalConfig) {

  // Pushbullet API - push endpoint
  val pushBulletApiPushEndpoint: String = s"${globalConfig.pushBulletAPIGlobalProtocol}://${globalConfig.pushbulletAPIGlobalHost}/v2/pushes"

  // Pushbullet API - user endpoint
  val pushBulletApiUserEndpoint: String = s"${globalConfig.pushBulletAPIGlobalProtocol}://${globalConfig.pushbulletAPIGlobalHost}/v2/users/me"

  // Data to push to users notification, this can be sourced from anywhere.
  val data = Json.obj(
    "body" -> "This has been posted from a push-notification-service",
    "title" -> "You have a new notification!",
    "type" -> "note"
  )

  /**
    * Push notification to stored user data in simulated data-store
    *
    * @param username
    * @return
    */
  def pushToUser(username: String): Future[Seq[JsValue]] = {

    // Find users access-token
    val accessToken = UsersCollection.findAccessTokenByUsername(username = username)

    ws.url(url = pushBulletApiPushEndpoint).withHeaders("Content-Type" -> "application/json", "Access-Token" -> s"$accessToken").post(body = data).map {
      response =>
        // Increment incrementNumOfNotificationsPushedByUsername
        UsersCollection.incrementNumOfNotificationsPushedByUsername(username = username)

        (Json.parse(response.body) \ "Push-bullet HTTP response").as[Seq[JsValue]]
    }
  }
}
