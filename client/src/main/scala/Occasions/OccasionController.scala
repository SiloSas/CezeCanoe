package Occasions

import ArticleWithSlider.ArticleWithSlider
import Lang.LangService
import com.greencatsoft.angularjs.core.{SceService, Timeout}
import com.greencatsoft.angularjs.{AbstractController, injectable}
import org.scalajs.dom.console
import scala.scalajs.js
import scala.scalajs.js.annotation.JSExportAll
import scala.util.{Failure, Success}
import scala.scalajs.js.JSConverters.JSRichGenTraversableOnce
import scala.concurrent.ExecutionContext.Implicits.global

@JSExportAll
@injectable("occasionController")
class OccasionController(occasionScope: OccasionScope, occasionService: OccasionService, $sce: SceService, timeout: Timeout,
                         sceService: SceService, langService: LangService)
  extends AbstractController[OccasionScope](occasionScope) {


  var lang = langService.lang
  langService.get(scope, () => lang = langService.lang)
  occasionScope.occasions = js.Array[ArticleWithSlider]()
  occasionService.findAll().onComplete {
    case Success(articles) =>
      timeout( () => {
        occasionScope.occasions = articles.toJSArray
      })
    case Failure(t: Throwable) =>
      console.log("error get articles")
  }



}
