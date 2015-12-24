package Descentes

import com.greencatsoft.angularjs.core.HttpService
import com.greencatsoft.angularjs.{Factory, Service, injectable}
import shared.{Descente, Price}

import scala.concurrent.Future
import scala.scalajs.js
import scala.scalajs.js.annotation.JSExport
import scala.util.{Failure, Success, Try}
import scala.concurrent.ExecutionContext.Implicits.global

@injectable("descenteService")
class DescenteService(http: HttpService) extends Service {
  require(http != null, "Missing argument 'http'.")


  val descente = Descente(
    id = "1",
    name = "Les Rochers de St Gély",
    presentation = "[lang_Fr]<p>Sur ce parcours de <b>7 km</b> (2h non-stop) vous pouvez garder le canoë 3 ou 4 heures pour pique-niquer et " +
      "vous baigner. Riche en faune et en flore, la rivière vous offrira un agréable moment de détente. Rendez-vous à notre " +
      "base de Goudargues au centre du village,<br/>et <b>partez immédiatement de 9h à 16h !</b></p>" +
      "[lang_En] <p>Discover the valley of the Ceze " +
      "river between Goudargues and Cazernau all along this 7 km trip, with a duration of about 2 hours if you choose to do it without any stops. " +
      "You just have to show up in our base of Goudargues in the village center, then you can go immediately from 9am to 4pm !",
    tour = "...de Goudargues à Cazernau",
    images = js.Array("assets/images/img1.jpg", "assets/images/img2.jpg"),
    distance = "7 km",
    prices = js.Array(Price(name = "adulte en canoë", price = 9.0, isBookable = true,
      medias = js.Array(), isSupplement = false),
      Price(name = "enfant", price = 6.0, isBookable = true,
        medias = js.Array(), isSupplement = false), Price(name = "prix adulte guichet", price = 9.80, isBookable = false,
        medias = js.Array(), isSupplement = false)),
    time = "parcours à la 1/2 journée"
  )
  val descente1 = Descente(
    id = "2",
    name = "Les Petites Gorges",
    presentation ="[lang_Fr]<p>Cette descente necessite une réservation. Merci. <br/><br/>Vous avez 1h30 de pagayage " +
      "et vous pouvez garder vos embarcations 3 ou 4 heures.<br/>Cette courte descente est idéale pour s'initier et découvrir " +
      "les paisibles gorges de la Cèze. Nous vous attendons à notre base du pont de Saint André pour vous équiper et " +
      "vous emmener 5 km en amont, au départ de Montclus. Arrivé au Pont de St André, vous déposerez votre bateau et " +
      "retrouverez votre véhicule. <b>Départ jusqu'à 13h.</b></p>",
    tour = "... de Montclus à St André",
    images = js.Array("assets/images/img3.jpg", "assets/images/img1.jpg"),
    distance = "7 km",
    prices = js.Array(Price(name = "adulte en canoë", price = 11.0, isBookable = true,
      medias = js.Array(), isSupplement = false),
      Price(name = "enfant", price = 6.0, isBookable = true,
        medias = js.Array(), isSupplement = false),
      Price(name = "prix adulte guichet", price = 12, isBookable = false,
        medias = js.Array(), isSupplement = false)),
    time = "parcours à la 1/2 journée"
  )
  val descente2 = Descente(
    id = "3",
    name = "L'île Bleue",
    presentation = "[lang_Fr]<p>Arrêtez-vous sur les plages sauvages qui longent la rivière sur ce parcours de " +
      "7 km entre le pont de St André et Goudargues. Comptez 2 heures de pagayages non-stop, location à la 1/2 journée. " +
      "Rendez-vous sur nos bases du Pont de St André de Juin à Aout ou de Goudargues Centre d'Avril à Septembre, et  " +
      "<b>partez immédiatement de 9 h à 16 h !</b></p>",
    tour = "... de St André à Goudargues",
    images = js.Array("assets/images/img2.jpg", "assets/images/img3.jpg"),
    distance = "7 km",
    prices = js.Array(Price(name = "adulte en canoë", price = 11.0, isBookable = true,
      medias = js.Array(), isSupplement = false),
      Price(name = "enfant", price = 6.0, isBookable = true,
        medias = js.Array(), isSupplement = false), Price(name = "prix adulte guichet", price = 12, isBookable = false,
        medias = js.Array(), isSupplement = false)),
    time = "parcours à la 1/2 journée"
  )
  val descente3 = Descente(
    id = "4",
    name = "Les Gorges & l'île Bleue",
    presentation = "[lang_Fr]<p>Profitez d'une journée nature avec cette petite randonnée à canoë de 12 km partant " +
      "de Montclus. Passez juste à côté de ce magnifique village puis naviguez entre les gorges de la Cèze, et laissez " +
      "vous guidez par les paisibles eaux jusqu'à votre arrivée à notre base de Goudargues. Comptez 3 heures de pagayage. " +
      "La location des bateaux sur ce parcours est à la journée :<b> partez avant 13 h !</b><br/><br/>Réservation conseillée.</p>",
    tour = "... de Montclus à Goudargues",
    images = js.Array("assets/images/img2.jpg"),
    distance = "12 km",
    prices = js.Array(Price(name = "adulte en canoë", price = 18.0, isBookable = true,
      medias = js.Array(), isSupplement = false),
      Price(name = "enfant", price = 6.0, isBookable = true,
        medias = js.Array(), isSupplement = false), Price(name = "prix adulte guichet", price = 19, isBookable = false,
        medias = js.Array(), isSupplement = false)),
    time = "parcours à la journée"
  )
  val descentes = Seq(descente, descente1, descente2, descente3)

  val informations = "* Enfant de moins de 30 kg en 3ème place, 6€ sur TOUS nos parcours !<br/> Prix guichet : 7€"
  val tariffs = Seq(
    Price(
    name = "Kayak une place",
    price = 4,
    isBookable = true,
    medias = js.Array("assets/images/kayak.jpg", "assets/images/kayak1.jpg"),
      isSupplement = true
    )
  )

  @JSExport
  def findAll(): Future[Seq[Descente]] = /*flatten*/ {
    Future(descentes)
    // Append a timestamp to prevent some old browsers from caching the result.
   /* http.get[js.Any]("/rooms")
      .map {JSON.stringify(_)}
      .map { read[Seq[Room]] }*/
  }

  @JSExport
  def findTariffs(): Future[Seq[Price]] = /*flatten*/ {
    Future(tariffs)
    // Append a timestamp to prevent some old browsers from caching the result.
   /* http.get[js.Any]("/rooms")
      .map {JSON.stringify(_)}
      .map { read[Seq[Room]] }*/
  }

  @JSExport
  def findById(id: String): Future[Option[Descente]] = /*flatten*/ {
    Future(descentes.find(_.id == id))
    // Append a timestamp to prevent some old browsers from caching the result.
   /* http.get[js.Any]("/rooms")
      .map {JSON.stringify(_)}
      .map { read[Seq[Room]] }*/
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
class DescenteServiceFactory(http: HttpService) extends Factory[DescenteService] {

  override def apply(): DescenteService = new DescenteService(http)
}
