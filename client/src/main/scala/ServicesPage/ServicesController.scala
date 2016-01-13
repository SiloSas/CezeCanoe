package ServicesPage

import ArticleWithSlider.ArticleWithSlider
import Lang.LangService
import com.greencatsoft.angularjs.core.{SceService, Timeout}
import com.greencatsoft.angularjs.{AbstractController, injectable}
import org.scalajs.dom.console

import scala.concurrent.ExecutionContext.Implicits.global
import scala.scalajs.js
import scala.scalajs.js.JSConverters.JSRichGenTraversableOnce
import scala.scalajs.js.annotation.JSExportAll
import scala.util.{Failure, Success}

@JSExportAll
@injectable("servicesController")
class ServicesController(scope: ServicesScope, servicesService: ServicesService, $sce: SceService, timeout: Timeout,
                         sceService: SceService, langService: LangService)
  extends AbstractController[ServicesScope](scope) {


  var lang = langService.lang
  langService.get(scope, () => lang = langService.lang)
  scope.articles = js.Array[ArticleWithSlider]()
  servicesService.findAll().onComplete {
    case Success(articles) =>
      timeout( () => {
        scope.articles = articles.toJSArray
      })
    case Failure(t: Throwable) =>
      console.log("error get articles")
  }



}
