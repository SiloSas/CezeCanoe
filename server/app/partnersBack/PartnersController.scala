package partnersBack

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

class PartnersController @Inject()(protected val dbConfigProvider: DatabaseConfigProvider, val partnersMethods: PartnersMethods)
  extends Controller {

  def findAll() = Action.async {
    partnersMethods.findAll map { partners =>
      Ok(write(partners))
    }
  }

  def find(id: String) = Action.async {
    partnersMethods.find(id) map {
      case Some(partner) =>
        Ok(write(partner))
      case _ =>
        NotFound
    }
  }

  def save() = process(partnersMethods.save)
  def update() = process(partnersMethods.update)
  def delete(id: String) = Authenticated.async { request =>
    partnersMethods.delete(id) map { result =>
      Ok(write(result))
    }
  }

  def process(updater: Partner => Future[Int]) = Authenticated.async(parse.json) { request =>
    val data = request.body.as[JsObject]

    val a = updater(read[Partner](data.toString()))
    a.map { result =>
      Ok(write(result))
    }
  }

}