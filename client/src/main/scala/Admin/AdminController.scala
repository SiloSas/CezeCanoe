package Admin

import java.util.UUID

import Descentes.DescenteService
import Lang.LangService
import com.greencatsoft.angularjs.core.{Timeout, SceService}
import com.greencatsoft.angularjs.{AbstractController, injectable}
import org.scalajs.dom.{console, alert}
import shared.{PriceForBack, DescenteForBack, Descente, Price}
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
  var initDescentes = Vector.empty[Descente]
  getDescentes()

  def getDescentes(): Unit = {
    descenteService.findAll().onComplete {
      case Success(descentes) =>
        initDescentes = descentes.toVector
        timeout(() => {
          adminScope.descentes = descentes.map {
            descenteToMutableDesente
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
    descenteService.findAll().onComplete {
      case Success(descentes) =>
        timeout(() => {
          adminScope.descentes = descentes.map {
            descenteToMutableDesente
          }.toJSArray
          val descenteInEdit = adminScope.descentes.filter(_.id == adminScope.descente.id)
          if (adminScope.formTemplate == "assets/templates/Admin/descenteForm.html" && descenteInEdit.nonEmpty ) {
            console.log( descenteInEdit.head)
            adminScope.descente = descenteInEdit.head
          }
        })
      case Failure(t: Throwable) =>
        println("adminController.FindAllDescentes: fail")
    }
  }

  def descenteToMutableDesente(descente: Descente): DescenteMutable = {
    val newDescente = new Object().asInstanceOf[DescenteMutable]
    newDescente.id = descente.id
    newDescente.name = descente.name
    newDescente.presentations = descente.presentation
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
    console.log(newDescente)
    newDescente
  }

  def versionedStringArrayToString(versionedStrings: js.Array[VersionedString]): String = {
    versionedStrings.map { versionedString =>
      versionedString.lang + versionedString.presentation
    } mkString ","
  }

  def versionedStringScopeToVersionedString(versionedStringScope: VersionedStringScope): VersionedString = {
    VersionedString(versionedStringScope.lang, versionedStringScope.presentation)
  }
  def versionedStringToBindScopeToVersionedString(versionedStringToBindScope: VersionedStringToBindScope): VersionedString = {
    VersionedString(versionedStringToBindScope.lang, versionedStringToBindScope.presentation.toString)
  }
  
  def mutableDescenteToDescenteForBack(descenteMutable: DescenteMutable): DescenteForBack = {
    val descente = descenteMutable
    val newPrices = descente.prices.toSeq map { price =>
      PriceForBack(id = price.id,
        name = price.name.toSeq map versionedStringScopeToVersionedString,
        price = price.price,
        isBookable = price.isBookable,
        medias = price.medias.toSeq,
        isSupplement = price.isSupplement)
    }
    DescenteForBack(id = descente.id,
      name = descente.name.toSeq map versionedStringScopeToVersionedString,
      presentation = descente.presentations.toSeq map versionedStringToBindScopeToVersionedString,
      tour = descente.tour.toSeq map versionedStringScopeToVersionedString,
      images = descente.images.toSeq,
      distance = descente.distance.toSeq map versionedStringScopeToVersionedString,
      prices = newPrices ,
      time = descente.time.toSeq map versionedStringScopeToVersionedString)
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

  def emptyVersionedStringArray(): js.Array[VersionedStringScope] = {
    langs.toJSArray.map { lang =>
      val newPresentation = new Object().asInstanceOf[VersionedStringScope]
      newPresentation.lang = lang
      newPresentation.presentation = ""
      newPresentation
    }
  }
  def emptyVersionedStringToBindArray(): js.Array[VersionedStringToBindScope] = {
    langs.toJSArray.map { lang =>
      val newPresentation = new Object().asInstanceOf[VersionedStringToBindScope]
      newPresentation.lang = lang
      newPresentation.presentation = $sce.trustAsHtml("")
      newPresentation
    }
  }

  adminScope.setNewDescente = () => {
    setNewDescente
  }

  def setNewDescente: Unit = {
    if (needToSave) alert("Veuillez sauvegarder ou annuler les changements")
    else {
      val newDescente = new Object().asInstanceOf[DescenteMutable]
      newDescente.id = UUID.randomUUID().toString
      newDescente.name = emptyVersionedStringArray()
      newDescente.presentations = emptyVersionedStringToBindArray()
      newDescente.distance = emptyVersionedStringArray()
      newDescente.images = Seq.empty[String].toJSArray
      newDescente.prices = Seq.empty[Price].toJSArray
      newDescente.time = emptyVersionedStringArray()
      newDescente.tour = emptyVersionedStringArray()
      adminScope.descente = newDescente
      adminScope.formTemplate = "assets/templates/Admin/descenteForm.html"
      adminScope.validate = () => {
        mutableDescenteToDescenteForBack(adminScope.descente)
        descenteService.add(mutableDescenteToDescenteForBack(adminScope.descente)) onComplete {
          case Success(int) =>
            timeout(() => {
              needToSave = false
              adminScope.descentes += adminScope.descente
              setNewDescente
            })

          case Failure(t: Throwable) =>
            console.log("bad")
        }
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