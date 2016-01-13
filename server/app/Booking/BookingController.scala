package BookingBack

import javax.inject.Inject

import Descentes.ArticleForBack
import administration.Authenticated
import play.api.db.slick.DatabaseConfigProvider
import play.api.libs.json.JsObject
import play.api.mvc.{Action, _}
import upickle.default._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class BookingController @Inject()(protected val dbConfigProvider: DatabaseConfigProvider, val bookingMethods: BookingMethods)
  extends Controller {

  def findAll() = Authenticated.async {
    bookingMethods.findAll map { bookings =>
      println(bookings)
      Ok(write(bookings))
    }
  }

  def save() = Action.async(parse.json) { request =>
    val data = request.body.as[JsObject]
    println(data)
    val a = bookingMethods.save(read[BookingForm](data.toString()))
    a.map { result =>
      Ok(write(result))
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