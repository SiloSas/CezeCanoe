package database

import BookingBack.{BookingDetail, BookingFormClient, BookingForm}
import Descentes._
import Services.ArticleWithSlider
import administration.UserActor.User
import DescentsClient.{Room}
import MyPostgresDriver.api._
import upickle.default._

trait MyDBTableDefinitions {

  def stringToSet(string: String): Seq[String] = {
    if (string.nonEmpty)  string.split(",").map(_.trim).filter(_.nonEmpty)
    else Seq.empty[String]
  }

  def stringToVersionedString(string: String): Seq[VersionedString] = {
    println("a: " + string)
    read[Seq[VersionedString]](string)
  }

  
  class Rooms(tag: Tag) extends Table[Room](tag, "rooms") {
    def id = column[String]("id")
    def name = column[String]("name")
    def presentation = column[String]("presentation")
    def header = column[String]("header")
    def images = column[String]("images")
    def isAnApartment = column[Boolean]("isanapartment")
    def price = column[String]("price")

    def * = (id, name, presentation, header, images, isAnApartment, price) <> ((Room.apply _).tupled, Room.unapply)
  }
  lazy val rooms = TableQuery[Rooms]
  
  
  class Descentes(tag: Tag) extends Table[DescenteWithPrice](tag, "descentes") {
    def id = column[String]("id")
    def name = column[String]("name")
    def presentation = column[String]("presentation")
    def tour = column[String]("tour")
    def images = column[String]("images")
    def distance = column[String]("distance")
    def prices = column[String]("prices")
    def time = column[String]("time")

    def * = (id, name, presentation, tour, images, distance, prices, time).shaped <> (
      { case (id, name, presentation, tour, images, distance, prices, time) =>
        DescenteWithPrice(id, stringToVersionedString(name), stringToVersionedString(presentation), stringToVersionedString(tour),
          read[Seq[String]](images), stringToVersionedString(distance), read[Seq[Price]](prices), stringToVersionedString(time))
      }, { descente: DescenteWithPrice =>
      Some((descente.id,  write(descente.name),  write(descente.presentation),
        write(descente.tour), write(descente.images),  write(descente.distance), write(descente.prices), write(descente.time)))
    })
  }
  lazy val descentes = TableQuery[Descentes]
  
  class Tariffs(tag: Tag) extends Table[Price](tag, "tariffs") {
    def id = column[String]("id")
    def name = column[String]("name")
    def price = column[Double]("price")
    def isBookable = column[Boolean]("isbookable")
    def medias = column[String]("medias")
    def isSupplement = column[Boolean]("issupplement")

    def * = (id, name, price, isBookable, medias, isSupplement).shaped <> (
      { case (id, name, price, isBookable, medias, isSupplement) =>
        Price(id, read[Seq[VersionedString]](name), price, isBookable, read[Seq[String]](medias), isSupplement)
      }, { price: Price =>
      Some((price.id, write(price.name), price.price, price.isBookable,  write(price.medias), price.isSupplement))
    })
  }
  lazy val tariffs = TableQuery[Tariffs]

  class Informations(tag: Tag) extends Table[Information](tag, "informations") {
      def id = column[String]("id")
      def information = column[String]("information")

    def * = (id, information).shaped <> (
      { case (id, information) =>
        Information(id, read[Seq[VersionedString]](information))
      }, { information: Information =>
          Some(information.id, write(information.information))
    })
  }
  lazy val informations = TableQuery[Informations]

  class Articles(tag: Tag) extends Table[ArticleForBack](tag, "articles") {
      def id = column[String]("id")
      def content = column[String]("content")
      def media = column[String]("media")
      def yellowThing = column[String]("yellowthing")

    def * = (id, content, media, yellowThing).shaped <> (
      { case (id, content, media, yellowThing) =>
        ArticleForBack(id, read[Seq[VersionedString]](content), media, read[Seq[VersionedString]](yellowThing))
      }, { article: ArticleForBack =>
          Some(article.id, write(article.content), article.media, write(article.yellowThing))
    })
  }
  lazy val articles = TableQuery[Articles]

  class Users(tag: Tag) extends Table[User](tag, "users") {
    def id = column[Int]("userid", O.PrimaryKey)
    def login = column[String]("login")
    def password = column[String]("password")

    def * = login <> (User, User.unapply)
  }
  lazy val users = TableQuery[Users]

  class HomeImages(tag: Tag) extends Table[Images](tag, "homeimages") {
    def images = column[String]("images")

    def * = images <> (Images, Images.unapply)
  }
  lazy val homeimages = TableQuery[HomeImages]
  case class Images(images: String)



  class Booking(tag: Tag) extends Table[BookingForm](tag, "booking") {
    def id = column[String]("id")
    def descentId = column[String]("descentid")
    def clientForm = column[String]("clientform")
    def details = column[String]("details")

    def * = (id, descentId, clientForm, details).shaped <> (
      { case (id, descentId, clientForm, details) =>
        BookingForm(id, descentId, read[BookingFormClient](clientForm), read[Seq[BookingDetail]](details))
      }, { bookingForm: BookingForm =>
      Some(bookingForm.id, bookingForm.descentId, write(bookingForm.bookingFormClient), write(bookingForm.details))
    })
  }
  lazy val booking = TableQuery[Booking]

  class Services(tag: Tag) extends Table[ArticleWithSlider](tag, "services") {
    def id = column[String]("id")
    def content = column[String]("content")
    def images = column[String]("images")

    def * = (id, content, images).shaped <> (
      { case (id, content, images) =>
        ArticleWithSlider(id, read[Seq[VersionedString]](content), read[Seq[String]](images))
      }, { article: ArticleWithSlider =>
      Some(article.id, write(article.content), write(article.images))
    })
  }
  lazy val services = TableQuery[Services]

  class Occasions(tag: Tag) extends Table[ArticleWithSlider](tag, "occasions") {
    def id = column[String]("id")
    def content = column[String]("content")
    def images = column[String]("images")

    def * = (id, content, images).shaped <> (
      { case (id, content, images) =>
        ArticleWithSlider(id, read[Seq[VersionedString]](content), read[Seq[String]](images))
      }, { article: ArticleWithSlider =>
      Some(article.id, write(article.content), write(article.images))
    })
  }
  lazy val occasions = TableQuery[Occasions]

}