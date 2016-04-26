package BookingBack

import javax.inject.Inject

import Descentes.ArticleForBack
import database.MyPostgresDriver.api._
import database.{MyDBTableDefinitions, MyPostgresDriver}
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}

import scala.concurrent.Future
import scala.language.postfixOps


class BookingMethods @Inject()(protected val dbConfigProvider: DatabaseConfigProvider)
  extends HasDatabaseConfigProvider[MyPostgresDriver]
  with MyDBTableDefinitions {

  def findAll: Future[Seq[BookingForm]] = {
    db.run(booking.result)
  }
  def save(bookingForm: BookingForm): Future[Int] = {
    db.run(booking += bookingForm)
  }
  def update(article: ArticleForBack): Future[Int] = {
    db.run(articles.filter(_.id === article.id).update(article))
  }
  def delete(id: String): Future[Int] = {
    db.run(articles.filter(_.id === id).delete)
  }
  def deleteBooking(id: String): Future[Int] = {
    db.run(booking.filter(_.id === id).delete)
  }
}