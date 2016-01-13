package Occasion

import javax.inject.Inject

import Services.ArticleWithSlider
import administration.Authenticated
import play.api.db.slick.DatabaseConfigProvider
import play.api.libs.json.JsObject
import play.api.mvc.{Action, _}
import upickle.default._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class OccasionController @Inject()(protected val dbConfigProvider: DatabaseConfigProvider, val occasionMethods: OccasionMethods)
  extends Controller {

  def findAll() = Action.async {
    occasionMethods.findAll map { occasion =>
      Ok(write(occasion))
    }
  }

  def find(id: String) = Action.async {
    occasionMethods.find(id) map {
      case Some(occasion) =>
        Ok(write(occasion))
      case _ =>
        NotFound
    }
  }

  def save() = process(occasionMethods.save)
  def update() = process(occasionMethods.update)
  def delete(id: String) = Authenticated.async { request =>
    occasionMethods.delete(id) map { result =>
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