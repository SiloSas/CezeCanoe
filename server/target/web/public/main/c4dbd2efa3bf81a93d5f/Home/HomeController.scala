package Home

import com.greencatsoft.angularjs.{AbstractController, injectable}
import com.greencatsoft.angularjs.core.SceService
import example.RoomService
import shared.Article

import scala.scalajs.js
import scala.scalajs.js.annotation.JSExportAll
import scala.scalajs.js.JSConverters.JSRichGenTraversableOnce

@JSExportAll
@injectable("homeController")
class HomeController(scope: HomeScope, service: RoomService, $sce: SceService) extends AbstractController[HomeScope](scope) {

  val article1 = Article(
  content = $sce.trustAsHtml("<h2>La Cèze en canoë ...</h2><p>Découvrez la Cèze et ses gorges, au cours d'une balade en canoë ou en kayak.</p>" +
  "<p><b>La Cèze est une rivière paisible et accessible à tous,</b> sur laquelle vous pourrez naviguer en famille, " +
    "entre amis, ou en solitaire. Vous pourrez prendre un pique-nique, et apprécier ce moment de nature et de détente au " +
    "cours d'une demi-journée pour les petits parcours (de 1 à 2 h de pagayage non-stop) ou durant la journée entière " +
    "pour les plus grands parcours (3 à 4 h non-stop).</p><p>Rendez-vous sur une de nos deux bases : à <b>Goudargues au centre du village et au" +
    "Pont de St André.</b> A très bientôt ..!</p>"),
  media = "https://www.youtube.com/embed/7foc3g23ROA",
  yellowThing = ""
  )
  val article2 = Article(
  content = $sce.trustAsHtml("<h2>des parcours pour tous ...</h2><p>Vous pourrez naviguer sur la Cèze entre Montclus, Saint-André de Roquepertuis, " +
    "Goudargues et La Roque sur Cèze, de la petite balade de 5 km pour s'initier au canoe, à la grande descente de 19 km" +
    " pour profiter d'une journée de sport. Nous vous proposons aussi des parcours de 7 km; 12 km et 14 km.</p>" +
  "<h2>... des embarcations pour tous ...</h2>\n\n \n\n<p>Nous vous proposons d'embarquer à bord de canoës de deux à " +
    "quatre places, ou bien en solo sur des kayaks une place, en demi-journée ou en journée. </p>"
  ),
  media = "assets/images/accueil.jpg",
  yellowThing = "Gilets homologués, pagaies doubles et simples, bidons étanches, casques pour vos enfants ... Tout est fourni !"
  )
  val article3 = Article(
  content = $sce.trustAsHtml("<h2>Retour immédiat ...</h2><p>Des navettes vous ramènes à votre véhicule, à Goudargues " +
    "Centre Village ou au Pont de St André, dès votre arrivée sur nos plages.</p>"
  ),
  media = "assets/images/bus.jpg",
  yellowThing = ""
  )
  val articles = Seq(article1, article2, article3)
  scope.articles = js.Array[Article]()


  scope.articles = articles.toJSArray

}

