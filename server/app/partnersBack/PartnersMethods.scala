package partnersBack

import javax.inject.Inject
import database.MyPostgresDriver.api._
import database.{MyDBTableDefinitions, MyPostgresDriver}
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import scala.concurrent.Future
import scala.language.postfixOps


class PartnersMethods @Inject()(protected val dbConfigProvider: DatabaseConfigProvider)
  extends HasDatabaseConfigProvider[MyPostgresDriver]
  with MyDBTableDefinitions {

  def findAll: Future[Seq[Partner]] = {
    db.run(partners.result)
  }
  def find(id: String): Future[Option[Partner]] = {
    db.run(partners.filter(_.id === id).result.headOption)
  }

  def save(Partner: Partner): Future[Int] = {
    db.run(partners += Partner)
  }
  def update(partner: Partner): Future[Int] = {
    db.run(partners.filter(_.id === partner.id).update(partner))
  }
  def delete(id: String): Future[Int] = {
    db.run(partners.filter(_.id === id).delete)
  }
}