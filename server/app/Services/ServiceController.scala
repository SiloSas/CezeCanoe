package Services

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

class ServiceController @Inject()(protected val dbConfigProvider: DatabaseConfigProvider, val servicesMethods: ServicesMethods)
  extends Controller {

  def findAll() = Action.async {
    servicesMethods.findAll map { services =>
      Ok(write(services))
    }
  }

  def find(id: String) = Action.async {
    servicesMethods.find(id) map {
      case Some(service) =>
        Ok(write(service))
      case _ =>
        NotFound
    }
  }

  def save() = process(servicesMethods.save)
  def update() = process(servicesMethods.update)
  def delete(id: String) = Authenticated.async { request =>
    servicesMethods.delete(id) map { result =>
      Ok(write(result))
    }
  }

  def process(updater: ArticleWithSlider => Future[Int]) = Authenticated.async(parse.json) { request =>
    val data = request.body.as[JsObject]

    val a = updater(read[ArticleWithSlider](data.toString()))
    a.map { result =>
      Ok(write(result))
    }
  }

}