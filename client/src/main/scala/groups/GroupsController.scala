package groups

import ArticleWithSlider.ArticleWithSlider
import Groups.GroupService
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
@injectable("groupsController")
class GroupsController(groupScope: GroupScope, GroupService: GroupService, $sce: SceService, timeout: Timeout,
                         sceService: SceService, langService: LangService)
  extends AbstractController[GroupScope](groupScope) {

  var lang = langService.lang
  langService.get(scope, () => lang = langService.lang)
  groupScope.groups = js.Array[ArticleWithSlider]()
  GroupService.findAll().onComplete {
    case Success(articles) =>
      timeout( () => {
        groupScope.groups = articles.toJSArray
      })
    case Failure(t: Throwable) =>
      console.log("error get articles")
  }
}
