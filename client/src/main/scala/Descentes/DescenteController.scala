package Descentes

import com.greencatsoft.angularjs.core.SceService
import com.greencatsoft.angularjs.{AbstractController, Controller, injectable}
import shared.{Price, Descente}

import scala.scalajs.js
import scala.scalajs.js.annotation.JSExportAll
import scala.scalajs.js.JSConverters.JSRichGenTraversableOnce

@JSExportAll
@injectable("descenteController")
class DescenteController(descenteScope: DescenteScope, $sce: SceService) extends AbstractController[DescenteScope](descenteScope) {

  val descente = Descente(
    id = "1",
    name = "Les Rochers de St Gély",
    presentation = $sce.trustAsHtml("<p>Sur ce parcours de <b>7 km</b> (2h non-stop) vous pouvez garder le canoë 3 ou 4 heures pour pique-niquer et " +
      "vous baigner. Riche en faune et en flore, la rivière vous offrira un agréable moment de détente. Rendez-vous à notre " +
      "base de Goudargues au centre du village,\net <b>partez immédiatement de 9h à 16h !</b></p>"),
  tour = "...de Goudargues à Cazernau",
  images = js.Array("assets/images/img1.jpg", "assets/images/img2.jpg"),
  distance = "7 km",
  prices = js.Array(Price(name = "adulte en canoë", price = 9.0), Price(name = "enfant", price = 6.0), Price(name = "prix adulte guichet", price = 9.80)),
  time = "parcours à la 1/2 journée"
  )
  val descente1 = Descente(
    id = "2",
    name = "Les Petites Gorges",
    presentation = $sce.trustAsHtml("<p>Cette descente necessite une réservation. Merci.\n\n \n\nVous avez 1h30 de pagayage " +
      "et vous pouvez garder vos embarcations 3 ou 4 heures.\n\nCette courte descente est idéale pour s'initier et découvrir " +
      "les paisibles gorges de la Cèze. Nous vous attendons à notre base du pont de Saint André pour vous équiper et " +
      "vous emmener 5 km en amont, au départ de Montclus. Arrivé au Pont de St André, vous déposerez votre bateau et " +
      "retrouverez votre véhicule. <b>Départ jusqu'à 13h.</b></p>"),
  tour = "... de Montclus à St André",
  images = js.Array("assets/images/img3.jpg", "assets/images/img1.jpg"),
  distance = "7 km",
  prices = js.Array(Price(name = "adulte en canoë", price = 11.0), Price(name = "enfant", price = 6.0), Price(name = "prix adulte guichet", price = 12)),
  time = "parcours à la 1/2 journée"
  )
  val descente2 = Descente(
    id = "3",
    name = "L'île Bleue",
    presentation = $sce.trustAsHtml("<p>Arrêtez-vous sur les plages sauvages qui longent la rivière sur ce parcours de " +
      "7 km entre le pont de St André et Goudargues. Comptez 2 heures de pagayages non-stop, location à la 1/2 journée. " +
      "Rendez-vous sur nos bases du Pont de St André de Juin à Aout ou de Goudargues Centre d'Avril à Septembre, et  " +
      "<b>partez immédiatement de 9 h à 16 h !</b></p>"),
  tour = "... de St André à Goudargues",
  images = js.Array("assets/images/img2.jpg", "assets/images/img3.jpg"),
  distance = "7 km",
  prices = js.Array(Price(name = "adulte en canoë", price = 11.0), Price(name = "enfant", price = 6.0), Price(name = "prix adulte guichet", price = 12)),
  time = "parcours à la 1/2 journée"
  )
  val descente3 = Descente(
    id = "4",
    name = "Les Gorges & l'île Bleue",
    presentation = $sce.trustAsHtml("<p>Profitez d'une journée nature avec cette petite randonnée à canoë de 12 km partant " +
      "de Montclus. Passez juste à côté de ce magnifique village puis naviguez entre les gorges de la Cèze, et laissez " +
      "vous guidez par les paisibles eaux jusqu'à votre arrivée à notre base de Goudargues. Comptez 3 heures de pagayage. " +
      "La location des bateaux sur ce parcours est à la journée :<b> partez avant 13 h !</b>\n\nRéservation conseillée.</p>"),
  tour = "... de Montclus à Goudargues",
  images = js.Array("assets/images/img2.jpg"),
  distance = "12 km",
  prices = js.Array(Price(name = "adulte en canoë", price = 18.0), Price(name = "enfant", price = 6.0), Price(name = "prix adulte guichet", price = 19)),
  time = "parcours à la journée"
  )
  val descentes = Seq(descente, descente1, descente2, descente3)
  descenteScope.descentes = descentes.toJSArray
}