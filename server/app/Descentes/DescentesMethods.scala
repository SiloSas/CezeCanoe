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

    val query = for {
      descente <- descentes joinLeft
        (descentePriceRelations join prices on (_.priceId === _.id)) on (_.id === _._1.descenteId)
    } yield descente

    db.run(query.result) map { descentesAndOptionalPrice =>
      descentesAndPricesToDescentesWithPrices(descentesAndOptionalPrice)
    }
  }

  def save(descenteWithPrices: DescenteWithPrice): Future[Int] = {
    val descente = descenteWithPricesToDescente(descenteWithPrices)
    val descentePrices = descenteWithPrices.prices
    descentePrices.map { price =>
      db.run(for {
        foundPrice <- prices.filter(_.id === price.id).result.headOption
        result <- foundPrice.map(DBIO.successful).getOrElse(prices returning prices.map(_.id) += price)
      } yield result match {
        case savePrice: Price =>
          descentePriceRelations += DescentePriceRelation(descenteId = descente.id, priceId = savePrice.id)
        case id: String =>
          prices.filter(_.id === price.id).update(price)
          descentePriceRelations += DescentePriceRelation(descenteId = descente.id, priceId = price.id)
      })
    }
    db.run(descentes += descente)
  }

  def descenteWithPricesToDescente(descenteWithPrices: DescenteWithPrice): Descente = {
    Descente(
      id = descenteWithPrices.id,
      name = descenteWithPrices.name,
      presentation = descenteWithPrices.presentation,
      tour = descenteWithPrices.tour,
      images = descenteWithPrices.images,
      distance = descenteWithPrices.distance,
      time = descenteWithPrices.time
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
        time = descente.time)
    }

    descentesWithPrices toSeq
  }
}