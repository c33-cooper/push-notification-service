package config

import javax.inject.Inject
import play.api.{Configuration}

/**
  * Created by callumcooper on 07/04/2017.
  */
class GlobalConfig @Inject() (playConfig: Configuration) {

  // Environment variable
  val env = playConfig.getString("run.mode").getOrElse("Dev")

  // Pushbullet-api global host
  val pushBulletAPIGlobalProtocol = playConfig.getString(s"$env.services.pushbullet-api.protocol").getOrElse("")
  val pushbulletAPIGlobalHost = playConfig.getString(s"$env.services.pushbullet-api.host").getOrElse("")

}
