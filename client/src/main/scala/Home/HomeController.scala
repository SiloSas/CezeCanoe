package Home

import Lang.LangService
import com.greencatsoft.angularjs.core.{Location, SceService, Timeout}
import com.greencatsoft.angularjs.{AbstractController, injectable}
import org.scalajs.dom.console

import scala.concurrent.ExecutionContext.Implicits.global
import scala.scalajs.js
import scala.scalajs.js.JSConverters.JSRichGenTraversableOnce
import scala.scalajs.js.annotation.JSExportAll
import scala.util.{Failure, Success}

@JSExportAll
@injectable("homeController")
class HomeController(scope: HomeScope, homeService: HomeService, $sce: SceService, timeout: Timeout, sceService: SceService,
                     langService: LangService, location: Location)
  extends AbstractController[HomeScope](scope) {


  var lang = langService.lang
  langService.get(scope, () => lang = langService.lang)
  scope.articles = js.Array[Article]()
  homeService.findAll().onComplete {
    case Success(articles) =>
      timeout( () => {
        scope.articles = articles.toJSArray
      })
    case Failure(t: Throwable) =>
      console.log("error get articles")
  }
  var current = initCurrent()

  def initCurrent(): Int = {
    location.path() match {
      case descentes if descentes.indexOf("descentes") > -1 =>
        1
      case services if services.indexOf("services") > -1 =>
        2
      case contact if contact.indexOf("contact") > -1 =>
        3
      case occasions if occasions.indexOf("occasions") > -1 =>
        5
      case partners if partners.indexOf("partners") > -1 =>
        7
      case _ =>
        0
    }
  }
  scope.$on("$locationChangeSuccess", () => {
    current = initCurrent()
  })
  def setCurrent(int: Int): Unit = {
    timeout( () => {
      current = int
    })
  }


}


@JSExportAll
@injectable("mainController")
class MainController(scope: HomeScope, homeService: HomeService, timeout: Timeout)
  extends AbstractController[HomeScope](scope) {

  scope.images = js.Array[String]()
  homeService.findHomeImages().onComplete {
    case Success(images) =>
      timeout( () => {
        scope.images = images.toJSArray
        console.log(scope.images)
      })
    case Failure(t: Throwable) =>
      console.log("error get images")
  }

}

