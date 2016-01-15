package Descentes

import Admin.{VersionedString, VersionedStringScope, VersionedStringToBindScope}
import com.greencatsoft.angularjs.core.{HttpService, SceService}
import com.greencatsoft.angularjs.{Factory, Service, injectable}
import DescentsClient.Descente
import DescentsClient.DescenteForBack
import DescentsClient.Price
import DescentsClient.PriceForBack
import DescentsClient._
import upickle.default._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.scalajs.js
import scala.scalajs.js.annotation.JSExport
import scala.scalajs.js.{JSON, Object}
import scala.util.{Failure, Success, Try}
import org.scalajs.dom.console
import scala.scalajs.js.JSConverters.JSRichGenTraversableOnce

@injectable("descenteService")
class DescenteService(http: HttpService, sce: SceService) extends Service {
  require(http != null, "Missing argument 'http'.")


  val newName = VersionedString(lang = "Fr", presentation = "Les Rochers de St Gély")
  val newName1 = VersionedString(lang = "En", presentation = "Les Rochers de St Gély")


  @JSExport
  def findAll(): Future[Seq[Descente]] = /*flatten*/ {
    // Append a timestamp to prevent some old browsers from caching the result.
    http.get[js.Any]("/descentes")
      .map { JSON.stringify(_)}
      .map { descentesString =>
        read[Seq[DescenteForBack]](descentesString) map { descenteForBack =>
          val presentation = descenteForBack.presentation.map { presentation =>
            versionedStringToVersionedStringToBindScope(presentation)
          }.toJSArray
          Descente(descenteForBack.id, descenteForBack.name.map(versionedStringToVersionedStringScope).toJSArray,
            presentation, descenteForBack.tour.map(versionedStringToVersionedStringScope).toJSArray, descenteForBack.images.toJSArray,
            descenteForBack.distance.map(versionedStringToVersionedStringScope).toJSArray, descenteForBack.prices.map { price =>
              Price(price.id, name = price.name.map(versionedStringToVersionedStringScope).toJSArray, price.price, price.isBookable,
              price.medias.toJSArray, price.isSupplement)
            }.toJSArray,
            descenteForBack.time.map(versionedStringToVersionedStringScope).toJSArray)
        }
      }
  }

  @JSExport
  def findById(id: String): Future[Descente] = /*flatten*/ {
    // Append a timestamp to prevent some old browsers from caching the result.
    http.get[js.Any]("/descentes/" + id)
      .map {JSON.stringify(_)}
      .map { string =>
        val descenteForBack = read[DescenteForBack](string)
        val presentation = descenteForBack.presentation.map { presentation =>
          versionedStringToVersionedStringToBindScope(presentation)
        }.toJSArray
        Descente(descenteForBack.id, descenteForBack.name.map(versionedStringToVersionedStringScope).toJSArray,
          presentation, descenteForBack.tour.map(versionedStringToVersionedStringScope).toJSArray, descenteForBack.images.toJSArray,
          descenteForBack.distance.map(versionedStringToVersionedStringScope).toJSArray, descenteForBack.prices.map { price =>
            Price(price.id, name = price.name.map(versionedStringToVersionedStringScope).toJSArray, price.price, price.isBookable,
              price.medias.toJSArray, price.isSupplement)
          }.toJSArray,
          descenteForBack.time.map(versionedStringToVersionedStringScope).toJSArray)
      }
  }

  @JSExport
  def add(descente: DescenteForBack): Future[String] = /*flatten*/ {
    http.post[js.Any](s"/descentes", write(descente))
      .map { p =>
        JSON.stringify(p)
      }
  }

  @JSExport
  def update(descente: DescenteForBack): Future[String] = /*flatten*/ {
    http.put[js.Any](s"/descentes", write(descente))
      .map { p =>
        console.log(p)
        JSON.stringify(p)
      }
  }
  @JSExport
  def delete(id: String): Future[String] = /*flatten*/ {
    http.delete[js.Any](s"/descentes/" + id)
      .map { p =>
        console.log(p)
        JSON.stringify(p)
      }
  }

  //tariffs

  @JSExport
  def findTariffs(): Future[Seq[Price]] = /*flatten*/ {
    // Append a timestamp to prevent some old browsers from caching the result.
    http.get[js.Any]("/prices")
      .map {JSON.stringify(_)}
      .map { prices =>
        val pricesFoBack = read[Seq[PriceForBack]](prices)
        pricesFoBack map { priceForBack =>
          console.log(prices)
          Price(id = priceForBack.id, name = priceForBack.name.map(versionedStringToVersionedStringScope).toJSArray, price = priceForBack.price,
            isBookable = priceForBack.isBookable, medias = priceForBack.medias.toJSArray, isSupplement = priceForBack.isSupplement)
        }
      }
  }

  @JSExport
  def updateTariff(priceForBack: PriceForBack): Future[String] = /*flatten*/ {
    http.put[js.Any](s"/prices", write(priceForBack))
      .map { p =>
        console.log(p)
        JSON.stringify(p)
      }
  }

  @JSExport
  def addTariff(priceForBack: PriceForBack): Future[String] = /*flatten*/ {
    console.log(write(priceForBack))
    http.post[js.Any](s"/prices", write(priceForBack))
      .map { p =>
        console.log(p)
        JSON.stringify(p)
      }
  }


  @JSExport
  def deleteTariff(id: String): Future[String] = /*flatten*/ {
    http.delete[js.Any](s"/prices/" + id)
      .map { p =>
        console.log(p)
        JSON.stringify(p)
      }
  }


  //informations
  @JSExport
  def findInformations(): Future[Seq[Information]] = /*flatten*/ {
    // Append a timestamp to prevent some old browsers from caching the result.
    http.get[js.Any]("/informations")
      .map {JSON.stringify(_)}
      .map { informations =>
        val informationsForBack = read[Seq[InformationForBack]](informations)
        informationsForBack map { informationForBack =>
          Information(id = informationForBack.id, information = informationForBack.information.map(versionedStringToVersionedStringToBindScope).toJSArray)
        }
      }
  }


  def versionedStringToVersionedStringToBindScope(presentation: VersionedString): VersionedStringToBindScope = {
    val pres = new Object().asInstanceOf[VersionedStringToBindScope]
    pres.lang = presentation.lang
    pres.presentation = sce.trustAsHtml(presentation.presentation)
    pres
  }

  def versionedStringToVersionedStringScope(versionedString: VersionedString): VersionedStringScope = {
    val newVersionedString = new Object().asInstanceOf[VersionedStringScope]
    newVersionedString.lang = versionedString.lang
    newVersionedString.presentation = versionedString.presentation
    newVersionedString
  }

  protected def flatten[T](future: Future[Try[T]]): Future[T] = future flatMap {
    case Success(s) => Future.successful(s)
    case Failure(f) => Future.failed(f)
  }
  protected def parameterizeUrl(url: String, parameters: Map[String, Any]): String = {
    require(url != null, "Missing argument 'url'.")
    require(parameters != null, "Missing argument 'parameters'.")

    parameters.foldLeft(url)((base, kv) =>
      base ++ { if (base.contains("?")) "&" else "?" } ++ kv._1 ++ "=" + kv._2)
  }
}


@injectable("descenteService")
class DescenteServiceFactory(http: HttpService, sce: SceService) extends Factory[DescenteService] {

  override def apply(): DescenteService = new DescenteService(http, sce: SceService)
}
