package Group

import javax.inject.Inject
import Services.ArticleWithSlider
import database.MyPostgresDriver.api._
import database.{MyDBTableDefinitions, MyPostgresDriver}
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import scala.concurrent.Future
import scala.language.postfixOps


class GroupMethods @Inject()(protected val dbConfigProvider: DatabaseConfigProvider)
  extends HasDatabaseConfigProvider[MyPostgresDriver]
  with MyDBTableDefinitions {

  def findAll: Future[Seq[ArticleWithSlider]] = {
    db.run(groups.result)
  }
  def find(id: String): Future[Option[ArticleWithSlider]] = {
    db.run(groups.filter(_.id === id).result.headOption)
  }

  def save(Service: ArticleWithSlider): Future[Int] = {
    db.run(groups += Service)
  }
  def update(service: ArticleWithSlider): Future[Int] = {
    db.run(groups.filter(_.id === service.id).update(service))
  }
  def delete(id: String): Future[Int] = {
    db.run(groups.filter(_.id === id).delete)
  }
}