package Descentes

import javax.inject.Inject
import database.MyPostgresDriver.api._
import database.{MyDBTableDefinitions, MyPostgresDriver}
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import play.api.libs.concurrent.Execution.Implicits._
import scala.concurrent.Future
import scala.language.postfixOps


class DescentesMethods @Inject()(protected val dbConfigProvider: DatabaseConfigProvider)
  extends HasDatabaseConfigProvider[MyPostgresDriver]
  with MyDBTableDefinitions {

  def findAll: Future[Seq[DescenteWithPrice]] = {
    db.run(descentes.result)
  }
  def find(id: String): Future[Option[DescenteWithPrice]] = {
    db.run(descentes.filter(_.id === id).result.headOption)
  }

  def save(descenteWithPrices: DescenteWithPrice): Future[Int] = {
    db.run(descentes += descenteWithPrices)
  }
  def update(descenteWithPrices: DescenteWithPrice): Future[Int] = {
    db.run(descentes.filter(_.id === descenteWithPrices.id).update(descenteWithPrices))
  }
  def delete(id: String): Future[Int] = {
    db.run(descentes.filter(_.id === id).delete)
  }

  def descenteWithPricesToDescente(descenteWithPrices: DescenteWithPrice): Descente = {
    Descente(
      id = descenteWithPrices.id,
      name = descenteWithPrices.name,
      presentation = descenteWithPrices.presentation,
      tour = descenteWithPrices.tour,
      images = descenteWithPrices.images,
      distance = descenteWithPrices.distance,
      time = descenteWithPrices.time,
      isVisible = descenteWithPrices.isVisible,
      groupReduction = descenteWithPrices.groupReduction
    )
  }

  def descentesAndPricesToDescentesWithPrices(descentesAndOptionalPrice: Seq[(Descente, Option[(DescentePriceRelation, Price)])]):
  Seq[DescenteWithPrice] = {
    val groupedByDescente = descentesAndOptionalPrice.groupBy(_._1)

    val descentesWithPrices = groupedByDescente map { tupleDescenteAndOptionalPrices =>
      val descente = tupleDescenteAndOptionalPrices._1

      val prices = tupleDescenteAndOptionalPrices._2.collect {
        case (_, Some((_, price))) => price
      }

      DescenteWithPrice(
        id = descente.id,
        name = descente.name,
        presentation = descente.presentation,
        tour = descente.tour,
        images = descente.images,
        distance = descente.distance,
        prices = prices.distinct,
        time = descente.time,
        isVisible = descente.isVisible,
        groupReduction = descente.groupReduction
      )
    }

    descentesWithPrices toSeq
  }
}