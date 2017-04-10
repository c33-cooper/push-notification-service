package services

import javax.inject.Inject
import connectors.PushButtonConnector

/**
  * Created by callumcooper on 06/04/2017.
  * TODO: Service left blank for now as requested features have been
  * implemented with basic connector. To scale app with further
  * enhancements it would make sense to modularise business logic
  * in PushButtonService class and utilize external data access
  * layer from connectors.
  */
class PushButtonService @Inject() (val pushButtonConnector: PushButtonConnector){
}
