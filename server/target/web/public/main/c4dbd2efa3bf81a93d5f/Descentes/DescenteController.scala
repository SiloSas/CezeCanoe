package Descentes

import Lang.LangService
import com.greencatsoft.angularjs.core.SceService
import com.greencatsoft.angularjs.{Angular, AbstractController, Controller, injectable}
import shared.{Price, Descente}

import org.scalajs.dom.console
import scala.scalajs.js
import scala.scalajs.js.Any
import scala.scalajs.js.annotation.JSExportAll
import scala.scalajs.js.JSConverters.JSRichGenTraversableOnce
import scala.util.Success
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.matching.Regex

@JSExportAll
@injectable("descenteController")
class DescenteController(descenteScope: DescenteScope, $sce: SceService, descenteService: DescenteService, langService: LangService)
extends AbstractController[DescenteScope](descenteScope) {

  descenteService.findAll().onComplete {
    case Success(descentes) =>
      var lang = langService.lang
      descenteScope.descentes = descentes.map { descente =>
          val presentation: String = langService.setLang(descente.presentation.toString, lang)
          descente.copy(presentation = $sce.trustAsHtml(presentation))
        }.toJSArray
      def updateDescentesScope(): Unit = {
        lang = langService.lang
        console.log("vfgf")
        descenteScope.descentes = descenteScope.descentes.map { descente =>
          val presentation: String = langService.setLang(descentes.filter(_.id == descente.id).head.presentation.toString, lang)
          descente.copy(presentation = $sce.trustAsHtml(presentation))
        }
      }

      lang = langService.get(descenteScope, () => updateDescentesScope())
    case _ =>
      println("miss")
  }


  descenteService.findTariffs().onComplete {
    case Success(tariffs) =>
      descenteScope.tariffs = tariffs.toJSArray
    case _ =>
      println("miss")
  }
  descenteScope.informations = "* Enfant de moins de 30 kg en 3ème place, 6€ sur TOUS nos parcours !\n Prix guichet : 7€"
}