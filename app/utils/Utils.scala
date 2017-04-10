package utils

import org.joda.time.format.DateTimeFormat

/**
  * Created by callumcooper on 07/04/2017.
  */
trait Utils {

  val dateTimeFormatter = DateTimeFormat.forPattern("dd/MM/yyyy")
}
