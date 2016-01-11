package Articles

import javax.inject.Inject

import Descentes.ArticleForBack
import administration.Authenticated
import play.api.db.slick.DatabaseConfigProvider
import play.api.libs.json.JsObject
import play.api.mvc.{Action, _}
import upickle.default._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class ArticlesController @Inject()(protected val dbConfigProvider: DatabaseConfigProvider, val articlesMethods: ArticlesMethods)
  extends Controller {

  def findAll() = Action.async {
    articlesMethods.findAll map { articles =>
      println(articles)
      Ok(write(articles))
    }
  }

  def find(id: String) = Action.async {
    articlesMethods.find(id) map {
      case Some(article) =>
        Ok(write(article))
      case _ =>
        NotFound
    }
  }

  def save() = process(articlesMethods.save)
  def update() = process(articlesMethods.update)
  def delete(id: String) = Authenticated.async { request =>
    articlesMethods.delete(id) map { result =>
      Ok(write(result))
    }
  }

  def process(updater: ArticleForBack => Future[Int]) = Authenticated.async(parse.json) { request =>
    val data = request.body.as[JsObject]

    val a = updater(read[ArticleForBack](data.toString()))
    a.map { result =>
      Ok(write(result))
    }
  }

}