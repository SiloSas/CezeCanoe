package Booking

import java.util.UUID

import Admin.{VersionedStringScope, VersionedString}
import Descentes.DescenteService
import Lang.LangService
import com.greencatsoft.angularjs.core.{Window, RouteParams, SceService, Timeout}
import com.greencatsoft.angularjs.{AbstractController, injectable}
import materialDesign.{MdToastService, MdToast}
import shared.Price
import org.scalajs.dom.console
import scala.concurrent.ExecutionContext.Implicits.global
import scala.scalajs.js
import scala.scalajs.js.{JSON, Object}
import scala.scalajs.js.annotation.JSExportAll
import scala.scalajs.js.JSConverters.JSRichGenTraversableOnce
import scala.util.Success
import upickle.default._

@JSExportAll
@injectable("bookingController")
class BookingController(bookingScope: BookingScope, routeParams: RouteParams, descenteService: DescenteService,
timeout: Timeout, $sce: SceService, langService: LangService, bookingService: BookingService, mdToast: MdToastService, window: Window)
  extends AbstractController[BookingScope](bookingScope) {

  case class BookingPrice(id: String, name: js.Array[VersionedStringScope], price: Double, numberToBook: Int, isSupplement: Boolean)
  var totalPrice: Double = 0
  var pricesDetails: List[BookingPrice] = List.empty
  var lang = langService.lang
  langService.get(bookingScope, () => lang = langService.lang)

  val id = routeParams.get("id").toString
  descenteService.findById(id).onComplete {
    case Success(descente) =>
          timeout( () => {
            bookingScope.descente = descente
          }, 0)
    case _ =>
      print("find descente by id error")
  }

  descenteService.findTariffs().onComplete {
    case Success(tariffs) =>
      bookingScope.tariffs = tariffs.toJSArray
    case _ =>
    print("find descente by id error")
  }

  bookingScope.computeTotalPrice = (price: Price, numberToBook: Int) => {
    println("yoy")
    pricesDetails = doComputeTotalPrice(price, numberToBook)
    totalPrice = 0
    pricesDetails foreach { detail =>
      if (detail.isSupplement) {
        val basePrice = bookingScope.descente.prices.head.price
        totalPrice = totalPrice + (detail.price * detail.numberToBook + basePrice * detail.numberToBook)
      } else totalPrice = totalPrice + detail.price * detail.numberToBook
    }
    bookingScope.total = totalPrice
  }

  def sendBooking(bookingForm: js.Any): Unit = {
    val prices = pricesDetails.toSeq map {price =>
      BookingDetail(priceId = price.id, number = price.numberToBook)
    }
    val newBooking = BookingFormBack(id = UUID.randomUUID().toString ,descentId = routeParams.get("id").toString,
      bookingFormClient = read[BookingFormClient](JSON.stringify(bookingForm)), details = prices)
    console.log(write(newBooking))
    bookingService.post(newBooking).onComplete {
      case Success(int) =>
        val a = mdToast.simple("Reservation validée")
        mdToast.show(a)
        console.log("youpi")
      case _ =>
        print("post booking error")
    }
  }

  def doComputeTotalPrice(price: Price, numberToBook: Int): List[BookingPrice] = {
    pricesDetails.find(_.name == price.name) match {
      case Some(bookingPrice) =>
        pricesDetails map {
          case alreadyExistingPrice if alreadyExistingPrice.name == price.name =>
            bookingPrice.copy(numberToBook = numberToBook)
          case other =>
            other
        }
      case _ =>
        pricesDetails :+ BookingPrice(price.id, price.name, price.price, numberToBook, price.isSupplement)
    }
  }
}
