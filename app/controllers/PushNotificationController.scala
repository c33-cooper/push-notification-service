package controllers

import javax.inject.Inject
import models.{User, UserPushNotificationRequest, UserRegisterRequest, UsersCollection}
import org.joda.time.{DateTime, DateTimeZone}
import play.api.mvc.{Action, Controller, Request, Result}
import play.api.libs.json._
import utils.Utils
import scala.concurrent.Future
import scala.util.{Failure, Success, Try}
import connectors.PushButtonConnector
import play.api.Logger

/**
  * Created by callumcooper on 06/04/2017.
  */
class PushNotificationController @Inject() (pushButtonConnector: PushButtonConnector) extends Controller
      with Utils {

  /**
    * Register user with service
    *
    * @return
    */
  def registerUser() = Action.async(parse.json) { implicit request =>
    withJsonBody[UserRegisterRequest] { userReq =>

      // PUT new user in simulated server side data-store
      UsersCollection.putUser(user = User(
        username = userReq.username,
        accessToken = userReq.accessToken,
        creationTime = DateTime.now().withZone(DateTimeZone.UTC),
        numOfNotificationsPushed = 0
      ))

      Future.successful(Ok(Json.toJson(UsersCollection.findUsersByUsername(username = userReq.username))))
    }
  }

  /**
    * Find all sorted users
    *
    * @return
    */
  def sortedUsers() = Action { implicit request =>
    val sortedUsers = UsersCollection.findAll
    Ok(Json.toJson(sortedUsers))
  }

  /**
    * User specific details by username
    *
    * @param username
    * @return
    */
  def userByUsername(username: String) = Action { implicit request =>
    UsersCollection.findUsersByUsername(username = username).map { user =>
      Ok(Json.toJson(user))
    }.getOrElse(NotFound)
  }

  /**
    * Push notification to push bullet from supplied username
    *
    * @return
    */
  def pushBulletNotification() = Action.async(parse.json) { implicit request =>
    withJsonBody[UserPushNotificationRequest] { userPushNotificationReq =>

      try {
        // Push to user
        pushButtonConnector.pushToUser(username = userPushNotificationReq.username)

        Future.successful(Ok(Json.toJson(s"200 - Push bullet notification sent successfully for ${userPushNotificationReq.username}")))
      }
      catch {
        case e: IllegalArgumentException => Logger.debug("Exception occured: " format e)
          Future.successful(BadRequest(s"Push bullet notification for ${userPushNotificationReq.username} failed!"))
      }
    }
  }

  /**
    * Utility function to allow controller to accept JSON body from payload
    * and read to specified model.
    *
    * @param f
    * @param request
    * @param m
    * @param reads
    * @tparam T
    * @return
    */
  private def withJsonBody[T](f: (T) => Future[Result])(implicit request: Request[JsValue], m: Manifest[T], reads: Reads[T]) =
    Try(request.body.validate[T]) match {
      case Success(JsSuccess(payload, _)) => f(payload)
      case Success(JsError(errs)) => Future.successful(BadRequest(s"Invalid ${m.runtimeClass.getSimpleName} payload: $errs"))
      case Failure(e) => Future.successful(BadRequest(s"could not parse body due to ${e.getMessage}"))
    }
}
