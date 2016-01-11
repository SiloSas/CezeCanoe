package Information

import javax.inject.Inject
import Descentes.{Information, Price}
import database.MyPostgresDriver.api._
import database.{MyDBTableDefinitions, MyPostgresDriver}
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import play.api.libs.concurrent.Execution.Implicits._
import scala.concurrent.Future
import scala.language.postfixOps


class InformationsMethods @Inject()(protected val dbConfigProvider: DatabaseConfigProvider)
  extends HasDatabaseConfigProvider[MyPostgresDriver]
  with MyDBTableDefinitions {

  def findAll: Future[Seq[Information]] = {
    db.run(informations.result)
  }
  def find(id: String): Future[Option[Information]] = {
    db.run(informations.filter(_.id === id).result.headOption)
  }

  def save(information: Information): Future[Int] = {
    db.run(informations += information)
  }
  def update(information: Information): Future[Int] = {
    db.run(informations.filter(_.id === information.id).update(information))
  }
  def delete(id: String): Future[Int] = {
    db.run(informations.filter(_.id === id).delete)
  }
  def findHomeImages(): Future[Seq[Images]] = {
    db.run(homeimages.result)
  }
  def updateHomeImages(images: String): Future[Int] = {
    db.run(homeimages.update(Images(images = images)))
  }
}