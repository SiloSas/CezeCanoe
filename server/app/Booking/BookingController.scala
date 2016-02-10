package BookingBack

import javax.inject.Inject

import Descentes.{DescentesMethods, ArticleForBack}
import Prices.PricesMethods
import administration.Authenticated
import play.api.db.slick.DatabaseConfigProvider
import play.api.libs.json
import play.api.libs.json.JsObject
import play.api.mvc.{Action, _}
import upickle.default._
import play.api.libs.ws.WSClient
import play.api.libs.json._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Await, Future}
import scala.concurrent.duration._

class BookingController @Inject()(protected val dbConfigProvider: DatabaseConfigProvider,
                                  val bookingMethods: BookingMethods,
                                   val descentesMethods: DescentesMethods,
                                   val pricesMethods: PricesMethods,
                                   val ws: WSClient)
  extends Controller {

  def preparJsPostForPaypal(creditCard: CreditCard, amount: Double): JsValue = {

    Json.parse(s"""{
        "intent":"sale",
        "payer":{
          "payment_method":"credit_card",
          "funding_instruments":[
        {
          "credit_card":{
          "number":${creditCard.number},
          "type":${creditCard.card_type},
          "expire_month":${creditCard.expire_month},
          "expire_year":${creditCard.expire_year},
          "cvv2":${creditCard.cvv2.toString},
          "first_name":${creditCard.first_name},
          "last_name":${creditCard.last_name},
          "billing_address":{
          "line1":${creditCard.billing_address.line1},
          "city":${creditCard.billing_address.city},
          "state":${creditCard.billing_address.state},
          "postal_code":${creditCard.billing_address.postal_code},
          "country_code":${creditCard.billing_address.country_code}
        }
        }
        }
          ]
        },
        "transactions":[
        {
          "amount":{
            "total":$amount,
            "currency":"EUR",
            "details":{
            "subtotal":"$amount"
          }
          },
          "description":"This is the payment transaction description."
        }
        ]
      }""")
  }

  def getPaypalPaiement(creditCard: CreditCard, amount: Double): Future[Int] = {
    ws.url("https://api.sandbox.paypal.com/v1/payments/payment")
      .withHeaders("Content-Type" -> "application/json", "Authorization" -> "Bearer <Access-Token>")
      .post(preparJsPostForPaypal(creditCard, amount))
      .map { wsResponse =>
        val a = wsResponse.json \ "state"
        val b = a.get
        if (b.toString() == "approved") 1
        else 0
    }
  }

  def findAll() = Authenticated.async {
    bookingMethods.findAll map { bookings =>
      println(bookings)
      Ok(write(bookings))
    }
  }

  def save() = Action.async(parse.json) { request =>
    val bookingFormWithCreditCard = read[BookingFormWithCreditCard](request.body.as[JsObject].toString())
    val bookingForm = bookingFormWithCreditCard.bookingForm
    val creditCard = bookingFormWithCreditCard.creditCard

    val eventuallyDescent = descentesMethods.find(bookingForm.descentId) map {
      case Some(descent) =>
        descent
      case _ =>
        throw new Exception("no descent")
    }

    val eventuallyGroupPrice = eventuallyDescent map { descente =>
      descente.groupReduction
    }

    val eventuallyDescenteReferencePrice = eventuallyDescent map { descente =>
      descente.prices.headOption match {
        case Some(price) =>
          price.price
        case _ =>
          throw new Exception("descent price error")
      }
    }

    val eventuallyPrices = Future.sequence(bookingForm.details map { detail =>
      eventuallyDescent map { descente => descente.prices.find(_.id == detail.priceId) }
    }).map { aa => aa.flatten }

    val eventuallyPrices2 = Future.sequence(bookingForm.details map { detail => pricesMethods.find(detail.priceId)}) map {
      a => a.collect { case Some(price) => price }
    }

    val total = for {
      prices <- eventuallyPrices
      prices2 <- eventuallyPrices2
      refPrice <- eventuallyDescenteReferencePrice
      groupPrice <- eventuallyGroupPrice
    } yield {
        val allPrices = prices ++ prices2
        val numberOfPrices = allPrices.length

        if (numberOfPrices != bookingForm.details.length) throw new Exception("Bad prices!")

        val allPricesPlusSupplement = allPrices.map { price =>
          val bookingDetail = bookingForm.details.find(_.priceId == price.id).getOrElse( throw new Exception("Bad prices!"))
          if(price.isSupplement) (price.price + refPrice)* bookingDetail.number
          else price.price * bookingDetail.number
        }

        val subTotal = allPricesPlusSupplement.foldLeft(0.0)((r, c) => r + c)
        if(bookingForm.bookingFormClient.isGroup) subTotal - ((subTotal * groupPrice) / 100)
        else subTotal
    }

    total flatMap { t =>
      getPaypalPaiement(creditCard, t) map {
        case isValidate if isValidate == 1 =>
          Ok(write(t))
        case _ =>
          InternalServerError(write("payment error"))
      }
    }
  }

  /*def update() = process(bookingsMethods.update)
  def delete(id: String) = Authenticated.async { request =>
    bookingsMethods.delete(id) map { result =>
      Ok(write(result))
    }
  }*/

  /*def process(updater: BookingForBack => Future[Int]) = Authenticated.async(parse.json) { request =>
    val data = request.body.as[JsObject]

    val a = updater(read[BookingForBack](data.toString()))
    a.map { result =>
      Ok(write(result))
    }
  }*/

}