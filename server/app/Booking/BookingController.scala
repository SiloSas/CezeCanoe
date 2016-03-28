package BookingBack

import javax.inject.Inject

import Descentes.DescentesMethods
import Prices.PricesMethods
import administration.Authenticated
import play.api.db.slick.DatabaseConfigProvider
import play.api.libs.json.{JsObject, _}
import play.api.libs.ws.{WSAuthScheme, WSClient}
import play.api.mvc.{Action, _}
import upickle.default._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class BookingController @Inject()(protected val dbConfigProvider: DatabaseConfigProvider,
                                  val bookingMethods: BookingMethods,
                                   val descentesMethods: DescentesMethods,
                                   val pricesMethods: PricesMethods,
                                   val ws: WSClient)
  extends Controller {

  def preparJsPostForPaypal(creditCard: CreditCard, amount: Double): JsObject = {
    Json.parse(s"""{"intent": "sale", "payer": {"payment_method": "credit_card", "funding_instruments": [{"credit_card": {"number": "${creditCard.number}", "type": "${creditCard.card_type}", "expire_month": ${creditCard.expire_month}, "expire_year": ${creditCard.expire_year}, "cvv2": ${creditCard.cvv2.toString}, "first_name": "${creditCard.first_name}", "last_name": "${creditCard.last_name}"}}]}, "transactions": [{"amount": {"total": $amount, "currency": "EUR"}, "description": "Thisisthepaymenttransactiondescription."}]}""").as[JsObject]
  }

  def getToken: Future[String] = {
    ws.url("https://api.sandbox.paypal.com/v1/oauth2/token")
      .withAuth("AZjqcHR8CIY7Y83XtHp5yDeE8pmkgyQqqYmUZ-iH3lL2uE7IUU_IsrVJABDRQx8QS0BG6KfNEwIA8cV-",
      "EHNMRJ_5eOFOOqseJ-Oxx7WcxBzGgqaia-yfvRS4Qhh_Umfz4dYGfcZuCxxAFN_JSWHsUSMZqZ2TQ4V_", WSAuthScheme.BASIC)
      .post(Map("grant_type" -> Seq("client_credentials"))) map { a =>
      println((a.json \ "access_token").get.toString())
      (a.json \ "access_token").get.toString()
    } recover { case e: Throwable =>
      println("e = " + e)
      throw e
    }
  }


  def getPaypalPaiement(creditCard: CreditCard, amount: Double, token: String): Future[Int] = {
    println(creditCard)
    println(preparJsPostForPaypal(creditCard, amount))
    ws.url("https://api.sandbox.paypal.com/v1/payments/payment")
      .withHeaders("Content-Type" -> "application/json", "Authorization" -> ("Bearer " + token.replace("\"", "")))
      .post(preparJsPostForPaypal(creditCard, amount))
      .map { wsResponse =>
        println(wsResponse.json)
        val a = wsResponse.json \ "state"
        println(a.get.toString())
        val b = a.get
        if (b.toString().replace("\"", "") == "approved") 1
        else 0
    } recover { case t: Throwable =>
      println(t)
        throw new Exception()
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

    val eventuallyTotal = for {
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

    eventuallyTotal flatMap { total =>
      println(total)
      getToken flatMap { token =>
        getPaypalPaiement(creditCard, total, token) flatMap {
          case isValidate if isValidate == 1 =>
            bookingMethods.save(bookingForm) map {
              case saved if saved == 1 =>
                Ok(write(total))
              case _ =>
                InternalServerError(write("save error, please contact us"))
            }
          case _ =>
            Future(InternalServerError(write("payment error")))
        }
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