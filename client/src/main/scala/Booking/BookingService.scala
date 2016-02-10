package Booking

import com.greencatsoft.angularjs.core.{HttpService, SceService}
import com.greencatsoft.angularjs.{Factory, Service, injectable}
import org.scalajs.dom.console
import upickle.default._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.scalajs.js
import scala.scalajs.js.JSON
import scala.scalajs.js.annotation.JSExport

@injectable("bookingService")
class BookingService(http: HttpService, sce: SceService) extends Service {
  require(http != null, "Missing argument 'http'.")


  @JSExport
  def post(bookingForm: BookingFormBack, creditCard: CreditCard): Future[String] = {
    val formWithCreditCard = BookingFormWithCreditCard(bookingForm, creditCard)
    http.post[js.Any](s"/booking", write(formWithCreditCard))
      .map(JSON.stringify(_))
  }

  @JSExport
  def get(): Future[Seq[BookingForm]] = {
    http.get[js.Any](s"/booking")
      .map(JSON.stringify(_))
      .map { bookingForm =>
        console.log(read[Seq[BookingFormBack]](bookingForm))
        read[Seq[BookingForm]](bookingForm)
      }
  }


}


@injectable("bookingService")
class BookingServiceFactory(http: HttpService, sce: SceService) extends Factory[BookingService] {

  override def apply(): BookingService = new BookingService(http, sce: SceService)
}
