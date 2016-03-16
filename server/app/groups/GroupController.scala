package Group

import javax.inject.Inject

import Services.ArticleWithSlider
import administration.Authenticated
import play.api.db.slick.DatabaseConfigProvider
import play.api.libs.json.JsObject
import play.api.mvc.{Action, _}
import upickle.default._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class GroupController @Inject()(protected val dbConfigProvider: DatabaseConfigProvider, val groupMethods: GroupMethods)
  extends Controller {

  def findAll() = Action.async {
    groupMethods.findAll map { group =>
      Ok(write(group))
    }
  }

  def find(id: String) = Action.async {
    groupMethods.find(id) map {
      case Some(group) =>
        Ok(write(group))
      case _ =>
        NotFound
    }
  }

  def save() = process(groupMethods.save)
  def update() = process(groupMethods.update)
  def delete(id: String) = Authenticated.async { request =>
    groupMethods.delete(id) map { result =>
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