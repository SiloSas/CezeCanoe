package Booking

import java.util.UUID

import Admin.VersionedStringScope
import Descentes.DescenteService
import DescentsClient.Price
import Lang.LangService
import com.greencatsoft.angularjs.core.{RouteParams, SceService, Timeout, Window}
import com.greencatsoft.angularjs.{AbstractController, injectable}
import materialDesign.MdToastService
import org.scalajs.dom.console
import upickle.default._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.scalajs.js
import scala.scalajs.js.JSConverters.JSRichGenTraversableOnce
import scala.scalajs.js.annotation.JSExportAll
import scala.scalajs.js.{Date, JSON}
import scala.util.Success

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
  var minDate = new Date()
  var isGroup = false
  var reduction = 0.0

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
  def computeReduction(): Unit = {
    timeout( () => {
      if (isGroup) reduction = (totalPrice * bookingScope.descente.groupReduction) / 100
      else reduction = 0.0
    })
  }

  def sendBooking(bookingForm: js.Any, creditCard: js.Any, message: String): Unit = {
    val prices = pricesDetails.toSeq map {price =>
      BookingDetail(priceId = price.id, number = price.numberToBook)
    }
    console.log(creditCard)
    val newCreditCard = read[CreditCard](JSON.stringify(creditCard))
    val email = read[BookingFormClient](JSON.stringify(bookingForm)).email + "--.," + message
    val newBooking = BookingFormBack(id = UUID.randomUUID().toString ,descentId = routeParams.get("id").toString,
      bookingFormClient = read[BookingFormClient](JSON.stringify(bookingForm)).copy(email = email),
      details = prices,
      isGroup = read[BookingFormClient](JSON.stringify(bookingForm)).isGroup)
    console.log(write(newBooking))
    console.log(write(newCreditCard))
    bookingService.post(newBooking, newCreditCard).onComplete {
      case Success(int) =>
        val a = mdToast.simple("Reservation validée")
        mdToast.show(a)
        console.log(int)
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
