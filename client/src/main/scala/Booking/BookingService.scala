package Booking

import Admin.{VersionedStringScope, VersionedStringToBind, VersionedStringToBindScope, VersionedString}
import com.greencatsoft.angularjs.core.{SceService, HttpService}
import com.greencatsoft.angularjs.{Factory, Service, injectable}
import shared.{ArticleForBack, Article}
import upickle.default._
import scala.concurrent.Future
import scala.scalajs.js
import org.scalajs.dom.console
import scala.scalajs.js.{Object, JSON}
import scala.scalajs.js.annotation.JSExport
import scala.scalajs.js.JSConverters.JSRichGenTraversableOnce
import scala.concurrent.ExecutionContext.Implicits.global

@injectable("bookingService")
class BookingService(http: HttpService, sce: SceService) extends Service {
  require(http != null, "Missing argument 'http'.")


  @JSExport
  def post(bookingForm: BookingFormBack): Future[String] = {
    http.post[js.Any](s"/booking", write(bookingForm))
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
