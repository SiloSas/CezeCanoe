package Prices

import javax.inject.Inject
import Descentes.Price
import database.MyPostgresDriver.api._
import database.{MyDBTableDefinitions, MyPostgresDriver}
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import play.api.libs.concurrent.Execution.Implicits._
import scala.concurrent.Future
import scala.language.postfixOps


class PricesMethods @Inject()(protected val dbConfigProvider: DatabaseConfigProvider)
  extends HasDatabaseConfigProvider[MyPostgresDriver]
  with MyDBTableDefinitions {

  def findAll: Future[Seq[Price]] = {
    db.run(tariffs.result)
  }
  def find(id: String): Future[Option[Price]] = {
    db.run(tariffs.filter(_.id === id).result.headOption)
  }

  def save(Price: Price): Future[Int] = {
    db.run(tariffs += Price)
  }
  def update(price: Price): Future[Int] = {
    db.run(tariffs.filter(_.id === price.id).update(price))
  }
  def delete(id: String): Future[Int] = {
    db.run(tariffs.filter(_.id === id).delete)
  }
}