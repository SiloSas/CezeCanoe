package Information

import java.io.{ByteArrayOutputStream, File}
import javax.imageio.ImageIO
import javax.inject.Inject

import Descentes.{Information, Price}
import administration.Authenticated
import play.Play
import play.api.db.slick.DatabaseConfigProvider
import play.api.libs.json.{JsArray, JsObject}
import play.api.mvc.{Action, _}
import upickle.default._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class InformationController @Inject()(protected val dbConfigProvider: DatabaseConfigProvider, val informationsMethods: InformationsMethods)
  extends Controller {

  def findAll() = Action.async {
    informationsMethods.findAll map { informations =>
      println(informations)
      Ok(write(informations))
    }
  }

  def find(id: String) = Action.async {
    informationsMethods.find(id) map {
      case Some(information) =>
        Ok(write(information))
      case _ =>
        NotFound
    }
  }

  def findHomeImages() = Action.async {
    informationsMethods.findHomeImages() map { images =>
      println(images.head.images)
        Ok(images.head.images)
    }
  }
  def updateHomeImages() = Authenticated.async(parse.json) { request =>
    val data = request.body.as[JsArray]
    println("yoyoyo" + data.toString())
    informationsMethods.updateHomeImages(data.toString()) map { images =>
        Ok(write(images))
    }
  }

  def save() = process(informationsMethods.save)
  def update() = process(informationsMethods.update)
  def delete(id: String) = Authenticated.async { request =>
    informationsMethods.delete(id) map { result =>
      Ok(write(result))
    }
  }

  def process(updater: Information => Future[Int]) = Authenticated.async(parse.json) { request =>
    val data = request.body.as[JsObject]

    val a = updater(read[Information](data.toString()))
    a.map { result =>
      Ok(write(result))
    }
  }

}