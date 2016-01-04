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