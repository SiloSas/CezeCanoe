package Descentes

import Lang.LangService
import com.greencatsoft.angularjs.core.{Timeout, SceService}
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
class DescenteController(descenteScope: DescenteScope, $sce: SceService, descenteService: DescenteService,
                         langService: LangService, timeout: Timeout)
extends AbstractController[DescenteScope](descenteScope) {

  var lang = langService.lang
  var descentesC = Seq.empty[Descente]
  langService.get(descenteScope, () => lang = langService.lang)
  descenteService.findAll().onComplete {
    case Success(descentes) =>
      timeout( () => {
        descentesC = descentes.toJSArray
        descenteScope.descentes = descentes.toJSArray
      })
    case _ =>
      println("miss")
  }


  descenteService.findTariffs().onComplete {
    case Success(tariffs) =>
      descenteScope.tariffs = tariffs.toJSArray
    case _ =>
      println("miss")
  }

  descenteService.findInformations().onComplete {
    case Success(informations) =>
      timeout( () => {
        console.log(informations)
        descenteScope.informations = informations.toJSArray
      })
    case _ =>
      println("miss get informations")
  }
}