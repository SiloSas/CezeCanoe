package Booking

import Descentes.DescenteService
import com.greencatsoft.angularjs.core.{RouteParams, SceService, Timeout}
import com.greencatsoft.angularjs.{AbstractController, injectable}
import shared.Price

import scala.concurrent.ExecutionContext.Implicits.global
import scala.scalajs.js.annotation.JSExportAll
import scala.scalajs.js.JSConverters.JSRichGenTraversableOnce
import scala.util.Success

@JSExportAll
@injectable("bookingController")
class BookingController(bookingScope: BookingScope, routeParams: RouteParams, descenteService: DescenteService,
timeout: Timeout, $sce: SceService)
  extends AbstractController[BookingScope](bookingScope) {

  case class BookingPrice(name: String, price: Double, numberToBook: Int, isSupplement: Boolean)
  var totalPrice: Double = 0
  var pricesDetails: List[BookingPrice] = List.empty

  val id = routeParams.get("id").toString
  descenteService.findById(id).onComplete {
    case Success(maybeDescente) =>
      maybeDescente match {
        case Some(descente) =>
          timeout( () => {
            bookingScope.descente = descente.copy(presentation = $sce.trustAsHtml(descente.presentation))
          }, 0)
        case _ =>
          print("no descente for this id")
      }
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
        pricesDetails :+ BookingPrice(price.name, price.price, numberToBook, price.isSupplement)
    }
  }
}
