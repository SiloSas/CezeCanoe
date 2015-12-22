package Admin

import Descentes.DescenteService
import com.greencatsoft.angularjs.core.{SceService, SceProvider}
import com.greencatsoft.angularjs.{AbstractController, injectable}
import shared.Descente
import scala.concurrent.ExecutionContext.Implicits.global
import scala.scalajs.js
import scala.scalajs.js.Object
import scala.scalajs.js.annotation.JSExportAll
import scala.util.{Failure, Success}
import scala.scalajs.js.JSConverters.JSRichGenTraversableOnce


@JSExportAll
@injectable("adminController")
class AdminController(adminScope: AdminScope, descenteService: DescenteService, $sce: SceService) extends AbstractController[AdminScope](adminScope) {

  descenteService.findAll().onComplete {
    case Success(descentes) =>
      adminScope.descentes = descentes.toJSArray
    case Failure(t: Throwable) =>
      println("adminController.FindAllDescentes: fail")
  }

  adminScope.setDescente = (descente: Descente) => {
    val newDescente = new Object().asInstanceOf[DescenteMutable]
    newDescente.id = descente.id
    newDescente.name = descente.name
    val langRegex = "(.lang_\\w*.)".r
    newDescente.presentations = descente.presentation.toString.trim.split("(?=.lang_\\w*.)").toSeq.filter(_.nonEmpty).toJSArray.map { presentation =>
      val newPresentation = new Object().asInstanceOf[Presentation]
      val lang = langRegex.findAllIn(presentation).next()
      newPresentation.lang = lang
      val pre = langRegex.replaceAllIn(presentation, "")
      newPresentation.presentation = $sce.trustAsHtml(pre)
      newPresentation
    }

    newDescente.distance = descente.distance
    newDescente.images = descente.images
    newDescente.prices = descente.prices map { price =>
      val p = new Object().asInstanceOf[Price]
      p.isBookable = price.isBookable
      p.isSupplement = price.isSupplement
      p.medias = price.medias
      p.name = price.name
      p.price = price.price
      p
    }
    newDescente.time = descente.time
    newDescente.tour = descente.tour
    adminScope.descente = newDescente
    adminScope.formTemplate = "assets/templates/Admin/descenteForm.html"
  }
}