package partner

import Lang.LangService
import ServicesPage.ServicesScope
import com.greencatsoft.angularjs.core.{Scope, SceService, Timeout}
import com.greencatsoft.angularjs.{AbstractController, injectable}
import org.scalajs.dom.console

import scala.concurrent.ExecutionContext.Implicits.global
import scala.scalajs.js
import scala.scalajs.js.JSConverters.JSRichGenTraversableOnce
import scala.scalajs.js.annotation.JSExportAll
import scala.util.{Failure, Success}

@JSExportAll
@injectable("partnersController")
class PartnersController(scope: ServicesScope, partnerService: PartnersService, $sce: SceService, timeout: Timeout,
                         sceService: SceService, langService: LangService)
  extends AbstractController[ServicesScope](scope) {

  var lang = langService.lang
  var partners = js.Array[Partner]()
  langService.get(scope, () => lang = langService.lang)
  partnerService.findAll().onComplete {
    case Success(foundPartners) =>
      timeout( () => {
        partners = foundPartners.toJSArray
      })
    case Failure(t: Throwable) =>
      console.log("error get partners")
  }

}
