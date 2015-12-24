package Admin

import java.util.UUID

import Descentes.DescenteService
import Lang.LangService
import com.greencatsoft.angularjs.core.{SceService, SceProvider}
import com.greencatsoft.angularjs.{AbstractController, injectable}
import shared.{Price, Descente}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.scalajs.js
import scala.scalajs.js.Object
import scala.scalajs.js.annotation.JSExportAll
import scala.util.{Failure, Success}
import scala.scalajs.js.JSConverters.JSRichGenTraversableOnce


@JSExportAll
@injectable("adminController")
class AdminController(adminScope: AdminScope, descenteService: DescenteService, $sce: SceService, langService: LangService) extends AbstractController[AdminScope](adminScope) {

  val langs = langService.getAvailableLang()
  descenteService.findAll().onComplete {
    case Success(descentes) =>
      adminScope.descentes = descentes.toJSArray
    case Failure(t: Throwable) =>
      println("adminController.FindAllDescentes: fail")
  }

  descenteService.findTariffs().onComplete {
    case Success(prices) =>
      adminScope.prices = prices.map { price =>
        val newPrice = new Object().asInstanceOf[Price]
        newPrice.isBookable = price.isBookable
        newPrice.isSupplement = price.isSupplement
        newPrice.medias = price.medias
        newPrice.name = price.name
        newPrice.price = price.price
        newPrice
      }.toJSArray
    case Failure(t: Throwable) =>
      println("failure find tariffs = " + t)
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
      val newPrice = new Object().asInstanceOf[Price]
      newPrice.isBookable = price.isBookable
      newPrice.isSupplement = price.isSupplement
      newPrice.medias = price.medias
      newPrice.name = price.name
      newPrice.price = price.price
      newPrice
    }
    newDescente.time = descente.time
    newDescente.tour = descente.tour
    adminScope.descente = newDescente
    adminScope.formTemplate = "assets/templates/Admin/descenteForm.html"
    adminScope.validate = () => {
      val newPresentation = adminScope.descente.presentations.map { presentation =>
        presentation.lang + presentation.presentation
      } mkString ","
      val newPrices = adminScope.descente.prices map { price =>
        Price(price.name, price.price, price.isBookable, price.medias, price.isSupplement)
      }
      val descenteToSave = Descente(id = adminScope.descente.id, adminScope.descente.name, newPresentation,
        adminScope.descente.tour, adminScope.descente.images, adminScope.descente.distance, newPrices,
        adminScope.descente.time)
      adminScope.descentes = adminScope.descentes.map { descente =>
        if (descente.id == descenteToSave.id) descenteToSave
        else descente
      }
    }
  }

  adminScope.setNewDescente = () => {
    val newDescente = new Object().asInstanceOf[DescenteMutable]
    newDescente.id = UUID.randomUUID().toString
    newDescente.name = ""
    println("langs = " + langs)
    newDescente.presentations = langs.toJSArray.map { lang =>
      val newPresentation = new Object().asInstanceOf[Presentation]
      newPresentation.lang = "[lang_"+lang+"]"
      newPresentation.presentation = $sce.trustAsHtml("")
      newPresentation
    }
    newDescente.distance = ""
    newDescente.images = Seq.empty[String].toJSArray
    newDescente.prices = Seq.empty[Price].toJSArray
    newDescente.time = ""
    newDescente.tour = ""
    adminScope.descente = newDescente
    adminScope.formTemplate = "assets/templates/Admin/descenteForm.html"
    adminScope.validate = () => {
      val newPresentation = adminScope.descente.presentations.map { presentation =>
        presentation.lang + presentation.presentation
      } mkString ","
      val newPrices = adminScope.descente.prices map { price =>
        Price(price.name, price.price, price.isBookable, price.medias, price.isSupplement)
      }
      val descenteToSave = Descente(id = adminScope.descente.id, adminScope.descente.name, newPresentation,
        adminScope.descente.tour, adminScope.descente.images, adminScope.descente.distance, newPrices,
        adminScope.descente.time)

      adminScope.descentes :+ descenteToSave
      adminScope.setNewDescente
    }
  }
}