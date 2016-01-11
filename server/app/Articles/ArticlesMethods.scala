package Articles

import javax.inject.Inject

import Descentes.ArticleForBack
import database.MyPostgresDriver.api._
import database.{MyDBTableDefinitions, MyPostgresDriver}
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}

import scala.concurrent.Future
import scala.language.postfixOps


class ArticlesMethods @Inject()(protected val dbConfigProvider: DatabaseConfigProvider)
  extends HasDatabaseConfigProvider[MyPostgresDriver]
  with MyDBTableDefinitions {

  def findAll: Future[Seq[ArticleForBack]] = {
    db.run(articles.result)
  }
  def find(id: String): Future[Option[ArticleForBack]] = {
    db.run(articles.filter(_.id === id).result.headOption)
  }

  def save(article: ArticleForBack): Future[Int] = {
    db.run(articles += article)
  }
  def update(article: ArticleForBack): Future[Int] = {
    db.run(articles.filter(_.id === article.id).update(article))
  }
  def delete(id: String): Future[Int] = {
    db.run(articles.filter(_.id === id).delete)
  }
}