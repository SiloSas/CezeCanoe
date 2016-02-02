package Contact

import Lang.LangService
import ServicesPage.ServicesScope
import com.greencatsoft.angularjs.{AbstractController, injectable}

import scala.scalajs.js.annotation.JSExportAll
@JSExportAll
@injectable("contactController")
class ContactController(scope: ServicesScope, langService: LangService)
  extends AbstractController[ServicesScope](scope) {


  var lang = langService.lang
  langService.get(scope, () => lang = langService.lang)



}
