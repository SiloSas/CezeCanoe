package Contact

import Lang.LangService
import ServicesPage.ServicesScope
import com.greencatsoft.angularjs.core.HttpService
import com.greencatsoft.angularjs.extensions.ModalInstance
import com.greencatsoft.angularjs.{AbstractController, injectable}
import materialDesign.MdToastService

import scala.concurrent.ExecutionContext.Implicits.global
import scala.scalajs.js
import scala.scalajs.js.annotation.JSExportAll
@JSExportAll
@injectable("contactModalController")
class ContactModalController(scope: ServicesScope, langService: LangService, modalInstance: ModalInstance[Any],
                        mdToastService: MdToastService, httpService: HttpService)
  extends AbstractController[ServicesScope](scope) {


  var lang = langService.lang
  langService.get(scope, () => lang = langService.lang)
  var waiting = false

  def sendMessage(message: js.Any): Unit = {
    waiting = true
    httpService.post[js.Any]("/mails",  message) map { resp =>
      waiting = false
      modalInstance.close()
      mdToastService.show(mdToastService.simple("votre message est bien envoyé"))
    } recover {
      case t: Throwable =>  {
        waiting = false
        mdToastService.show(mdToastService.simple("Une erreur s'est produite"))
      }
    }
  }

  def closeModal(): Unit = {
    modalInstance.close()
  }


}
