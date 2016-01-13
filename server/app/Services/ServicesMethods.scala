package Services

import javax.inject.Inject
import database.MyPostgresDriver.api._
import database.{MyDBTableDefinitions, MyPostgresDriver}
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import scala.concurrent.Future
import scala.language.postfixOps


class ServicesMethods @Inject()(protected val dbConfigProvider: DatabaseConfigProvider)
  extends HasDatabaseConfigProvider[MyPostgresDriver]
  with MyDBTableDefinitions {

  def findAll: Future[Seq[ArticleWithSlider]] = {
    db.run(services.result)
  }
  def find(id: String): Future[Option[ArticleWithSlider]] = {
    db.run(services.filter(_.id === id).result.headOption)
  }

  def save(Service: ArticleWithSlider): Future[Int] = {
    db.run(services += Service)
  }
  def update(service: ArticleWithSlider): Future[Int] = {
    db.run(services.filter(_.id === service.id).update(service))
  }
  def delete(id: String): Future[Int] = {
    db.run(services.filter(_.id === id).delete)
  }
}