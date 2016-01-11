package Home

import Admin.VersionedStringToBindScope
import Lang.LangService
import com.greencatsoft.angularjs.{AbstractController, injectable}
import com.greencatsoft.angularjs.core.{Timeout, SceService}
import shared.Article
import org.scalajs.dom.console
import scala.scalajs.js
import scala.scalajs.js.Object
import scala.scalajs.js.annotation.JSExportAll
import scala.scalajs.js.JSConverters.JSRichGenTraversableOnce
import scala.util.{Failure, Success}
import scala.concurrent.ExecutionContext.Implicits.global

@JSExportAll
@injectable("homeController")
class HomeController(scope: HomeScope, homeService: HomeService, $sce: SceService, timeout: Timeout, sceService: SceService, langService: LangService)
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

