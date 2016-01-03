package Admin

import java.util.UUID

import Descentes.DescenteService
import Lang.LangService
import com.greencatsoft.angularjs.core.{Timeout, SceService}
import com.greencatsoft.angularjs.{AbstractController, injectable}
import org.scalajs.dom.{console, alert}
import shared.{Descente, Price}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.scalajs.js
import scala.scalajs.js.JSConverters.JSRichGenTraversableOnce
import scala.scalajs.js.Object
import scala.scalajs.js.annotation.JSExportAll
import scala.util.{Failure, Success}


@JSExportAll
@injectable("adminController")
class AdminController(adminScope: AdminScope, descenteService: DescenteService, $sce: SceService,
                      langService: LangService, timeout: Timeout)
  extends AbstractController[AdminScope](adminScope) {

  adminScope.newImage = ""
  adminScope.descente = new Object().asInstanceOf[DescenteMutable]
  var needToSave = false
  val langs = langService.getAvailableLang()
  var initDescentes = Seq.empty[Descente]
  getDescentes

  def getDescentes: Unit = {
    descenteService.findAll().onComplete {
      case Success(descentes) =>
        initDescentes = descentes
        timeout(() => {
          adminScope.descentes = initDescentes.map { descente =>
            descenteToMutableDesente(descente)
          }.toJSArray
        })
      case Failure(t: Throwable) =>
        println("adminController.FindAllDescentes: fail")
    }
  }

  def setNeedToSaveToTrue(): Unit = {
    needToSave = true
  }
  def cancelChanges(): Unit = {
    needToSave = false
    timeout(() => {
      adminScope.descentes = initDescentes.map { descente =>
        descenteToMutableDesente(descente)
      }.toJSArray
      val descenteInEdit = adminScope.descentes.filter(_.id == adminScope.descente.id)
      console.log(descenteInEdit)
      if (adminScope.formTemplate == "assets/templates/Admin/descenteForm.html" && descenteInEdit.nonEmpty ) {
        console.log( descenteInEdit.head)
        adminScope.descente = descenteInEdit.head
      }
    })
  }

  def stringToVersionedString(string: String): js.Array[VersionedString] = {
    val langRegex = "(.lang_\\w*.)".r
    val array = string.trim.split("(?=.lang_\\w*.)").toSeq.filter(_.nonEmpty).toJSArray.map { presentation =>
      console.log(presentation)
      val newPresentation = new Object().asInstanceOf[VersionedString]
      val lang = langRegex.findAllIn(presentation).next()
      newPresentation.lang = lang
      val pre = langRegex.replaceAllIn(presentation, "")
      newPresentation.presentation = $sce.trustAsHtml(pre)
      newPresentation
    }
    if(array.length < langs.length) {
      langs.map { lang =>
        if (array.filter(_.lang == "[lang_" + lang + "]").length == 0) {
          val newPresentation = new Object().asInstanceOf[VersionedString]
          newPresentation.lang = "[lang_" + lang + "]"
          newPresentation.presentation = array.head.presentation
          console.log(newPresentation)
          array.push(newPresentation)
        }
      }
    }
    array
  }
  def descenteToMutableDesente(descente: Descente): DescenteMutable = {
    val newDescente = new Object().asInstanceOf[DescenteMutable]
    newDescente.id = descente.id
    newDescente.name = stringToVersionedString(descente.name)
    newDescente.presentations = stringToVersionedString(descente.presentation.toString)
    newDescente.distance = stringToVersionedString(descente.distance)
    newDescente.images = descente.images
    newDescente.prices = descente.prices map { price =>
      val newPrice = new Object().asInstanceOf[Price]
      newPrice.isBookable = price.isBookable
      newPrice.isSupplement = price.isSupplement
      newPrice.medias = price.medias
      newPrice.name = stringToVersionedString(price.name)
      newPrice.price = price.price
      newPrice
    }
    newDescente.time = stringToVersionedString(descente.time)
    newDescente.tour = stringToVersionedString(descente.tour)
    newDescente
  }

  def versionedStringArrayToString(versionedStrings: js.Array[VersionedString]): String = {
    versionedStrings.map { versionedString =>
      versionedString.lang + versionedString.presentation
    } mkString ","
  }

  def mutableDescenteToDescente(descenteMutable: DescenteMutable): Descente = {
    val descente = descenteMutable
    val newPresentation = versionedStringArrayToString(descente.presentations)
    val newPrices = descente.prices map { price =>
      Price(versionedStringArrayToString(price.name), price.price, price.isBookable, price.medias, price.isSupplement)
    }
    Descente(id = descente.id, name = versionedStringArrayToString(descente.name), presentation = newPresentation,
      tour = versionedStringArrayToString(descente.tour), images = descente.images,
      distance = versionedStringArrayToString(descente.distance), prices = newPrices, time = versionedStringArrayToString(descente.time))
  }

  descenteService.findTariffs().onComplete {
    case Success(prices) =>
      adminScope.prices = prices.map { price =>
        val newPrice = new Object().asInstanceOf[Price]
        newPrice.isBookable = price.isBookable
        newPrice.isSupplement = price.isSupplement
        newPrice.medias = price.medias
        newPrice.name = stringToVersionedString(price.name)
        newPrice.price = price.price
        newPrice
      }.toJSArray
    case Failure(t: Throwable) =>
      println("failure find tariffs = " + t)
  }

  adminScope.setDescente = (descente: DescenteMutable) => {
    if (needToSave) alert("Veuillez sauvegarder ou annuler les changements")
    else {
      adminScope.descente = descente
      adminScope.formTemplate = "assets/templates/Admin/descenteForm.html"
      adminScope.validate = () => {
        console.log("validate")
        needToSave = false
        /*mutableDescenteToDescente(adminScope.descente)
      }*/
      }
    }
  }

  def setPrices(): Unit = {
    if (needToSave) alert("Veuillez sauvegarder ou annuler les changements")
    else adminScope.formTemplate = "assets/templates/Admin/tariffsForm.html"
  }

  def updatePrice(price: Price): Unit = {
      if(adminScope.prices.indexOf(price) == -1)  {
        adminScope.prices += price
      }
  }

  def addNewMedia(newPrice: Price, newMedia: String): Unit = {
    newPrice.medias.push(newMedia)
  }

  def emptyVersionedStringArray(): js.Array[VersionedString] = {
    langs.toJSArray.map { lang =>
      val newPresentation = new Object().asInstanceOf[VersionedString]
      newPresentation.lang = "[lang_"+lang+"]"
      newPresentation.presentation = $sce.trustAsHtml("")
      newPresentation
    }
  }

  adminScope.setNewDescente = () => {
    if (needToSave) alert("Veuillez sauvegarder ou annuler les changements")
    else {
      val newDescente = new Object().asInstanceOf[DescenteMutable]
      newDescente.id = UUID.randomUUID().toString
      newDescente.name = emptyVersionedStringArray()
      newDescente.presentations = emptyVersionedStringArray()
      newDescente.distance = emptyVersionedStringArray()
      newDescente.images = Seq.empty[String].toJSArray
      newDescente.prices = Seq.empty[Price].toJSArray
      newDescente.time = emptyVersionedStringArray()
      newDescente.tour = emptyVersionedStringArray()
      adminScope.descente = newDescente
      adminScope.formTemplate = "assets/templates/Admin/descenteForm.html"
      adminScope.validate = () => {
        //mutableDescenteToDescente(adminScope.descente)
        console.log(adminScope.descente)
        adminScope.descentes += adminScope.descente
        adminScope.setNewDescente
      }
    }
  }

  adminScope.$watch("newImage", () => {
    console.log(adminScope.descente.images)
    if (adminScope.descente.images.indexOf(adminScope.newImage) == -1) {
      adminScope.descente.images += adminScope.newImage
    }
  })

}