package database

import Descentes.{DescentePriceRelation, Price, VersionedString, Descente}
import shared.Room
import MyPostgresDriver.api._
import upickle.default._

trait MyDBTableDefinitions {

  def stringToSet(string: String): Array[String] = {
    string.split(",").map(_.trim).filter(_.nonEmpty)
  }

  def stringToVersionedString(string: String): Seq[VersionedString] = {
    read[Array[VersionedString]](string)
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
  
  
  class Descentes(tag: Tag) extends Table[Descente](tag, "descentes") {
    def id = column[String]("id")
    def name = column[String]("name")
    def presentation = column[String]("presentation")
    def tour = column[String]("tour")
    def images = column[String]("images")
    def distance = column[String]("distance")
    def time = column[String]("time")

    def * = (id, name, presentation, tour, images, distance, time).shaped <> (
      { case (id, name, presentation, tour, images, distance, time) =>
        Descente(id, stringToVersionedString(name), stringToVersionedString(presentation), stringToVersionedString(tour),
          stringToSet(images), stringToVersionedString(distance), stringToVersionedString(time))
      }, { descente: Descente =>
      Some((descente.id, descente.name.mkString(","), descente.presentation.mkString(","), 
        descente.tour.mkString(","), descente.images.mkString(","), descente.distance.mkString(","), descente.time.mkString(",")))
    })
  }
  lazy val descentes = TableQuery[Descentes]
  
  class Prices(tag: Tag) extends Table[Price](tag, "prices") {
    def id = column[String]("id")
    def name = column[String]("name")
    def price = column[Double]("price")
    def isBookable = column[Boolean]("isbookable")
    def medias = column[String]("medias")
    def isSupplement = column[Boolean]("issupplement")

    def * = (id, name, price, isBookable, medias, isSupplement).shaped <> (
      { case (id, name, price, isBookable, medias, isSupplement) =>
        Price(id, read[Array[VersionedString]](name), price, isBookable, stringToSet(medias), isSupplement)
      }, { price: Price =>
      Some((price.id, price.name.mkString(","), price.price, price.isBookable, price.medias.mkString(","), price.isSupplement))
    })
  }
  lazy val prices = TableQuery[Prices]

  class DescentePriceRelations(tag: Tag) extends Table[DescentePriceRelation](tag, "descentepricerelations") {
      def descenteId = column[String]("descenteid")
      def priceId = column[String]("priceid")

    def * = (descenteId, priceId) <> ((DescentePriceRelation.apply _).tupled, DescentePriceRelation.unapply)
  }
  lazy val descentePriceRelations = TableQuery[DescentePriceRelations]

}