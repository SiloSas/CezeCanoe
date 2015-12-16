package Descentes

import com.greencatsoft.angularjs.{AbstractController, Controller, injectable}
import shared.{Price, Descente}

import scala.scalajs.js
import scala.scalajs.js.annotation.JSExportAll
import scala.scalajs.js.JSConverters.JSRichGenTraversableOnce

@JSExportAll
@injectable("descenteController")
class DescenteController(descenteScope: DescenteScope) extends AbstractController[DescenteScope](descenteScope) {

  val descente = Descente(
    id = "1",
    name = "Les Rochers de St Gély",
    presentation = "Sur ce parcours de 7 km (2h non-stop) vous pouvez garder le canoë 3 ou 4 heures pour pique-niquer et " +
      "vous baigner. Riche en faune et en flore, la rivière vous offrira un agréable moment de détente. Rendez-vous à notre " +
      "base de Goudargues au centre du village,\net partez immédiatement de 9h à 16h !",
  tour = "de Goudargues à Cazernau",
  images = js.Array("assets/images/img1.jpg", "assets/images/img2.jpg"),
  distance = "7 km",
  prices = js.Array(Price(name = "enfant", price = 6.0), Price(name = "adulte en canoë prix guichet : 9€80", price = 9.0)),
  time = "parcours à la 1/2 journée"
  )
  val descentes = Seq(descente)
  descenteScope.descentes = descentes.toJSArray
}