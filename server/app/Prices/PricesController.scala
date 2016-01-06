package Prices

import java.io.{ByteArrayOutputStream, File}
import javax.imageio.ImageIO
import javax.inject.Inject

import Descentes.Price
import administration.Authenticated
import play.Play
import play.api.db.slick.DatabaseConfigProvider
import play.api.libs.json.JsObject
import play.api.mvc.{Action, _}
import upickle.default._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class PricesController @Inject()(protected val dbConfigProvider: DatabaseConfigProvider, val pricesMethods: PricesMethods)
  extends Controller {

  def findAll() = Action.async {
    pricesMethods.findAll map { prices =>
      Ok(write(prices))
    }
  }

  def find(id: String) = Action.async {
    pricesMethods.find(id) map {
      case Some(price) =>
        Ok(write(price))
      case _ =>
        NotFound
    }
  }

  def save() = process(pricesMethods.save)
  def update() = process(pricesMethods.update)
  def delete(id: String) = Authenticated.async { request =>
    pricesMethods.delete(id) map { result =>
      Ok(write(result))
    }
  }

  def process(updater: Price => Future[Int]) = Authenticated.async(parse.json) { request =>
    val data = request.body.as[JsObject]

    val a = updater(read[Price](data.toString()))
    a.map { result =>
      Ok(write(result))
    }
  }

}