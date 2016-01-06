package Descentes

import Admin.{VersionedString, VersionedStringScope, VersionedStringToBindScope}
import com.greencatsoft.angularjs.core.{HttpService, SceService}
import com.greencatsoft.angularjs.{Factory, Service, injectable}
import shared.Descente
import shared.DescenteForBack
import shared.Price
import shared.PriceForBack
import shared._
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


  /*val newNameEn = new Object().asInstanceOf[VersionedString]
  newNameEn.lang = "En"
  newNameEn.presentation = "Les Rochers de St Gély in English"

  val names = Seq(newName, newNameEn).toJSArray

  val presFr = new Object().asInstanceOf[VersionedStringToBind]
  presFr.lang = "Fr"
  presFr.presentation = sce.trustAsHtml( "<p>Sur ce parcours de <b>7 km</b> (2h non-stop) vous pouvez garder le canoë 3 ou 4 heures pour pique-niquer et" +
    "vous baigner. Riche en faune et en flore, la rivière vous offrira un agréable moment de détente. Rendez-vous à notre " +
    "base de Goudargues au centre du village,<br/>et <b>partez immédiatement de 9h à 16h !</b></p>")

  val presEn = new Object().asInstanceOf[VersionedStringToBind]
  presEn.lang = "En"
  presEn.presentation = sce.trustAsHtml("<p>Discover the valley of the Ceze" +
    "river between Goudargues and Cazernau all along this 7 km trip, with a duration of about 2 hours if you choose to do it without any stops." +
    "You just have to show up in our base of Goudargues in the village center, then you can go immediately from 9am to 4pm !")

  val presentations = Seq(presFr, presEn).toJSArray


  val newTour = new Object().asInstanceOf[VersionedString]
  newTour.lang = "Fr"
  newTour.presentation = "...de Goudargues à Cazernau"

  val tours = Seq(newTour).toJSArray



  val newDistance = new Object().asInstanceOf[VersionedString]
  newDistance.lang = "Fr"
  newDistance.presentation = "7 km"

  val distances = Seq(newDistance).toJSArray


  val descente = Descente(
    id = "1",
    name = names,
    presentation = presentations,
    tour = tours,
    images = js.Array("assets/images/img1.jpg", "assets/images/img2.jpg"),
    distance = distances,
    prices = js.Array(Price(id = "1", name = names, price = 9.0, isBookable = true,
      medias = js.Array(), isSupplement = false),
      Price(id = "1", name = names, price = 6.0, isBookable = true,
        medias = js.Array(), isSupplement = false), Price(id = "1", name = names, price = 9.80, isBookable = false,
        medias = js.Array(), isSupplement = false)),
    time = distances
  )*/
  /*val descente1 = Descente(
    id = "2",
    name ="[lang_Fr]Les Petites Gorges",
    presentation ="[lang_Fr]<p>Cette descente necessite une réservation. Merci. <br/><br/>Vous avez 1h30 de pagayage " +
      "et vous pouvez garder vos embarcations 3 ou 4 heures.<br/>Cette courte descente est idéale pour s'initier et découvrir " +
      "les paisibles gorges de la Cèze. Nous vous attendons à notre base du pont de Saint André pour vous équiper et " +
      "vous emmener 5 km en amont, au départ de Montclus. Arrivé au Pont de St André, vous déposerez votre bateau et " +
      "retrouverez votre véhicule. <b>Départ jusqu'à 13h.</b></p>",
    tour = "[lang_Fr]... de Montclus à St André",
    images = js.Array("assets/images/img3.jpg", "assets/images/img1.jpg"),
    distance = "[lang_Fr]7 km",
    prices = js.Array(Price(name = "[lang_Fr]adulte en canoë", price = 11.0, isBookable = true,
      medias = js.Array(), isSupplement = false),
      Price(name = "[lang_Fr]enfant", price = 6.0, isBookable = true,
        medias = js.Array(), isSupplement = false),
      Price(name = "[lang_Fr]prix adulte guichet", price = 12, isBookable = false,
        medias = js.Array(), isSupplement = false)),
    time = "[lang_Fr]parcours à la 1/2 journée"
  )
  val descente2 = Descente(
    id = "3",
    name = "[lang_Fr]L'île Bleue",
    presentation = "[lang_Fr]<p>Arrêtez-vous sur les plages sauvages qui longent la rivière sur ce parcours de " +
      "7 km entre le pont de St André et Goudargues. Comptez 2 heures de pagayages non-stop, location à la 1/2 journée. " +
      "Rendez-vous sur nos bases du Pont de St André de Juin à Aout ou de Goudargues Centre d'Avril à Septembre, et  " +
      "<b>partez immédiatement de 9 h à 16 h !</b></p>",
    tour = "[lang_Fr]... de St André à Goudargues",
    images = js.Array("assets/images/img2.jpg", "assets/images/img3.jpg"),
    distance = "[lang_Fr]7 km",
    prices = js.Array(Price(name = "[lang_Fr]adulte en canoë", price = 11.0, isBookable = true,
      medias = js.Array(), isSupplement = false),
      Price(name = "[lang_Fr]enfant", price = 6.0, isBookable = true,
        medias = js.Array(), isSupplement = false), Price(name = "[lang_Fr]prix adulte guichet", price = 12, isBookable = false,
        medias = js.Array(), isSupplement = false)),
    time = "[lang_Fr]parcours à la 1/2 journée"
  )
  val descente3 = Descente(
    id = "4",
    name = "[lang_Fr]Les Gorges & l'île Bleue",
    presentation = "[lang_Fr]<p>Profitez d'une journée nature avec cette petite randonnée à canoë de 12 km partant " +
      "de Montclus. Passez juste à côté de ce magnifique village puis naviguez entre les gorges de la Cèze, et laissez " +
      "vous guidez par les paisibles eaux jusqu'à votre arrivée à notre base de Goudargues. Comptez 3 heures de pagayage. " +
      "La location des bateaux sur ce parcours est à la journée :<b> partez avant 13 h !</b><br/><br/>Réservation conseillée.</p>",
    tour = "[lang_Fr]... de Montclus à Goudargues",
    images = js.Array("assets/images/img2.jpg"),
    distance = "[lang_Fr]12 km",
    prices = js.Array(Price(name = "[lang_Fr]adulte en canoë", price = 18.0, isBookable = true,
      medias = js.Array(), isSupplement = false),
      Price(name = "[lang_Fr]enfant", price = 6.0, isBookable = true,
        medias = js.Array(), isSupplement = false), Price(name = "[lang_Fr]prix adulte guichet", price = 19, isBookable = false,
        medias = js.Array(), isSupplement = false)),
    time = "[lang_Fr]parcours à la journée"
  )*/
  val descentes = Seq.empty[Descente]

  def versionedStringToVersionedStringScope(versionedString: VersionedString): VersionedStringScope = {
   val newVersionedString = new Object().asInstanceOf[VersionedStringScope]
   newVersionedString.lang = versionedString.lang
   newVersionedString.presentation = versionedString.presentation
   newVersionedString
 }

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

  @JSExport
  def findTariffs(): Future[Seq[Price]] = /*flatten*/ {
    // Append a timestamp to prevent some old browsers from caching the result.
    http.get[js.Any]("/prices")
      .map {JSON.stringify(_)}
      .map { prices =>
        val pricesFoBack = read[Seq[PriceForBack]](prices)
        pricesFoBack map { priceForBack =>
          Price(id = priceForBack.id, name = priceForBack.name.map(versionedStringToVersionedStringScope).toJSArray, price = priceForBack.price,
          isBookable = priceForBack.isBookable, medias = priceForBack.medias.toJSArray, isSupplement = priceForBack.isSupplement)
        }
      }
  }
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

  def versionedStringToVersionedStringToBindScope(presentation: VersionedString): VersionedStringToBindScope = {
    val pres = new Object().asInstanceOf[VersionedStringToBindScope]
    pres.lang = presentation.lang
    pres.presentation = sce.trustAsHtml(presentation.presentation)
    pres
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
