package Descentes

import Lang.LangService
import com.greencatsoft.angularjs.core.SceService
import com.greencatsoft.angularjs.{AbstractController, Controller, injectable}
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
 /* var a = langService.get()
  */

  var lang = langService.lang
  descenteService.findAll().onComplete {
    case Success(descentes) =>
        descenteScope.descentes = descentes.map { descente =>
          val presentation: String = setLang(descente.presentation.toString, lang)
          descente.copy(presentation = $sce.trustAsHtml(presentation))
        }.toJSArray
      def updateDescentesScope(): Unit = {
        lang = langService.lang
        console.log("vfgf")
        descenteScope.descentes = descenteScope.descentes.map { descente =>
          val presentation: String = setLang(descentes.filter(_.id == descente.id).head.presentation.toString, lang)
          descente.copy(presentation = $sce.trustAsHtml(presentation))
        }
      }

      lang = langService.get(descenteScope, () => updateDescentesScope())
    case _ =>
      println("miss")
  }

  def setLang(string: String, lang: String): String = {
    "(.lang_\\w*.)".r.replaceAllIn(
      string.trim.split("(?=.lang_\\w*.)").find(_.indexOf("lang_" + lang) > -1) match {
        case Some(presentationInLang) =>
          presentationInLang
        case _ =>
          string.split("(?=.lang_\\w*.)").head
      }, ""
    )
  }

  descenteService.findTariffs().onComplete {
    case Success(tariffs) =>
      descenteScope.tariffs = tariffs.toJSArray
    case _ =>
      println("miss")
  }
  descenteScope.informations = "* Enfant de moins de 30 kg en 3ème place, 6€ sur TOUS nos parcours !\n Prix guichet : 7€"
}