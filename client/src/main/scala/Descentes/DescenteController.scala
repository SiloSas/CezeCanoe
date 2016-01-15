package Descentes

import DescentsClient.Descente
import Lang.LangService
import com.greencatsoft.angularjs.core.{SceService, Timeout}
import com.greencatsoft.angularjs.{AbstractController, injectable}
import org.scalajs.dom.console

import scala.concurrent.ExecutionContext.Implicits.global
import scala.scalajs.js
import scala.scalajs.js.JSConverters.JSRichGenTraversableOnce
import scala.scalajs.js.annotation.JSExportAll
import scala.util.Success

@JSExportAll
@injectable("descenteController")
class DescenteController(descenteScope: DescenteScope, $sce: SceService, descenteService: DescenteService,
                         langService: LangService, timeout: Timeout)
extends AbstractController[DescenteScope](descenteScope) {

  var lang = langService.lang
  var descentesC = Seq.empty[Descente]
  descenteScope.descentes = new js.Array[Descente]
  langService.get(descenteScope, () => lang = langService.lang)
  descenteService.findAll().onComplete {
    case Success(descentes) =>
        descentesC = descentes.toJSArray
        timeout( () => {
          descenteScope.descentes = descentes.toJSArray
        }, 50, true)
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