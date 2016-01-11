package Admin

import java.util.UUID

import Descentes.DescenteService
import Home.HomeService
import Lang.LangService
import com.greencatsoft.angularjs.core.{Timeout, SceService}
import com.greencatsoft.angularjs.{AbstractController, injectable}
import org.scalajs.dom.{console, alert}
import shared.Descente
import shared.DescenteForBack
import shared.PriceForBack
import shared._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.scalajs.js
import scala.scalajs.js.JSConverters.JSRichGenTraversableOnce
import scala.scalajs.js.Object
import scala.scalajs.js.annotation.JSExportAll
import scala.util.{Failure, Success}


@JSExportAll
@injectable("adminController")
class AdminController(adminScope: AdminScope, descenteService: DescenteService, $sce: SceService,
                      langService: LangService, timeout: Timeout, homeService: HomeService)
  extends AbstractController[AdminScope](adminScope) {

  // init var & scope
  adminScope.newImage = ""
  adminScope.descente = new Object().asInstanceOf[DescenteMutable]
  adminScope.setNewDescente = () => {
    setNewDescente
  }
  adminScope.setDescente = (descente: DescenteMutable) => {
    if (needToSave) alert("Veuillez sauvegarder ou annuler les changements")
    else {
      adminScope.descente = descente
      adminScope.formTemplate = "assets/templates/Admin/descenteForm.html"
      adminScope.validate = () => {
        mutableDescenteToDescenteForBack(adminScope.descente)
        descenteService.update(mutableDescenteToDescenteForBack(adminScope.descente)) onComplete {
          case Success(int) =>
            timeout(() => {
              needToSave = false
            })

          case Failure(t: Throwable) =>
            console.log("bad")
        }
      }
    }
  }
  var needToSave = false
  val langs = langService.getAvailableLang()
  var initDescentes = Vector.empty[Descente]

  //refactoring
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
    val newPrices: Array[PriceForBack] = descente.prices.toArray.map { price =>
      priceToPriceForBack(price)
    }
    DescenteForBack(id = descente.id,
      name = descente.name.toSeq map versionedStringScopeToVersionedString,
      presentation = descente.presentations.toSeq map versionedStringToBindScopeToVersionedString,
      tour = descente.tour.toSeq map versionedStringScopeToVersionedString,
      images = descente.images.toSeq,
      distance = descente.distance.toSeq map versionedStringScopeToVersionedString,
      prices = newPrices.toSeq,
      time = descente.time.toSeq map versionedStringScopeToVersionedString)
  }
  def mutableArticleToArticleForBack(articleMutable: ArticleMutable): ArticleForBack = {
    val article = articleMutable
    ArticleForBack(id = article.id,
      content = article.content.toSeq map versionedStringToBindScopeToVersionedString,
      media = article.media,
      yellowThing = article.yellowThing.toSeq map versionedStringScopeToVersionedString)
  }

  def priceToPriceForBack(price: Price): PriceForBack = {
    PriceForBack(id = price.id,
      name = price.name.toSeq map versionedStringScopeToVersionedString,
      price = price.price,
      isBookable = price.isBookable,
      medias = price.medias.toSeq,
      isSupplement = price.isSupplement)
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

  // utilities
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
  def stringUuid(): String = UUID.randomUUID().toString

  def setPrices(): Unit = {
    if (needToSave) alert("Veuillez sauvegarder ou annuler les changements")
    else adminScope.formTemplate = "assets/templates/Admin/tariffsForm.html"
  }

  def addNewMedia(newPrice: Price, newMedia: String): Unit = {
    newPrice.medias.push(newMedia)
  }

  def setVersionedText(versionedStrings: Seq[VersionedStringScope]): js.Array[VersionedStringScope] = {
    var refText = ""
    versionedStrings.find(_.presentation.trim.length > 0) match {
      case Some(versionedString) =>
        refText = versionedString.presentation
      case _ =>
        console.log("no ref")
    }
    versionedStrings.map { versionedString =>
      if (versionedString.presentation.trim.length > 0) refText = versionedString.presentation
      else versionedString.presentation = refText
      versionedString
    }.toJSArray
  }

  def setVersionedDescenteTexts(): Unit = {
    adminScope.descente.name = setVersionedText(adminScope.descente.name.toSeq)
    adminScope.descente.distance = setVersionedText(adminScope.descente.distance.toSeq)
    adminScope.descente.time = setVersionedText(adminScope.descente.time.toSeq)
    adminScope.descente.tour = setVersionedText(adminScope.descente.tour.toSeq)
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


  //http calls

    //descentes
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

  def deleteDescente(id: String): Unit = {
    descenteService.delete(id) onComplete {
      case Success(string) =>
        timeout( () => {
          val descente = adminScope.descentes.filter(_.id == id).head
          adminScope.descentes.splice(adminScope.descentes.indexOf(descente), 1)
        })
      case Failure(t: Throwable) =>
        console.log("cannot delete")
    }
  }

  //prices

  getPrices

  def getPrices: Unit = {
    descenteService.findTariffs().onComplete {
      case Success(prices) =>
        adminScope.prices = prices.map { price =>
          val newPrice = new Object().asInstanceOf[Price]
          newPrice.id = price.id
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
  }

  def updatePrice(price: Price): Unit = {
    console.log(price)
    if(adminScope.prices.indexOf(price) == -1)  {
      descenteService.addTariff(priceToPriceForBack(price)) onComplete {
        case Success(int) =>
          timeout( () => {
            adminScope.prices += price
          })
        case Failure(t: Throwable) =>
          console.log("error add tariff")
      }
    }
    else {
      descenteService.updateTariff(priceToPriceForBack(price)) onComplete {
        case Success(int) =>
          console.log("youhou")
        case Failure(t: Throwable) =>
          console.log("error update tariff")
      }
    }
  }

  def deletePrice(id: String): Unit = {
    descenteService.deleteTariff(id) onComplete {
      case Success(int) =>
        adminScope.prices.find(_.id == id) match {
          case Some(price) =>
            timeout( () => {
              adminScope.prices.splice(adminScope.prices.indexOf(price), 1)
            })
          case _ =>
            console.log("no price for this id")
        }
      case Failure(t: Throwable) =>
        console.log("error delete price:" + t)
    }
  }

  //articles
  getArticles

  def setArticle(article: ArticleMutable): Unit = {
    if (needToSave) alert("Veuillez sauvegarder ou annuler les changements")
    else {
      adminScope.article = article
      console.log(adminScope.article)
      adminScope.formTemplate = "assets/templates/Admin/articleForm.html"
      adminScope.validate = () => {
        mutableArticleToArticleForBack(adminScope.article)
        homeService.update(mutableArticleToArticleForBack(adminScope.article)) onComplete {
          case Success(int) =>
            timeout(() => {
              needToSave = false
            })

          case Failure(t: Throwable) =>
            console.log("bad")
        }
      }
    }
  }
  def setNewArticle(): Unit = {
    if (needToSave) alert("Veuillez sauvegarder ou annuler les changements")
    else {
      val article = new Object().asInstanceOf[ArticleMutable]
      article.id = stringUuid()
      article.content = emptyVersionedStringToBindArray()
      article.media = ""
      article.yellowThing = emptyVersionedStringArray()
      adminScope.article = article
      console.log(adminScope.article)
      adminScope.formTemplate = "assets/templates/Admin/articleForm.html"
      adminScope.validate = () => {
        mutableArticleToArticleForBack(adminScope.article)
        homeService.post(mutableArticleToArticleForBack(adminScope.article)) onComplete {
          case Success(int) =>
            timeout(() => {
              needToSave = false
            })

          case Failure(t: Throwable) =>
            console.log("bad")
        }
      }
    }
  }

  def getArticles: Unit = {
    homeService.findAll().onComplete {
      case Success(articles) =>
        adminScope.articles = articles.map { article =>
          val newArticle = new Object().asInstanceOf[ArticleMutable]
          newArticle.id = article.id
          newArticle.content = article.content
          newArticle.media = article.media
          newArticle.yellowThing = article.yellowThing
          newArticle
        }.toJSArray
      case Failure(t: Throwable) =>
        println("failure find article = ", throw(t))
    }
  }

  def updateArticle(article: ArticleMutable): Unit = {
    if(adminScope.articles.indexOf(article) == -1)  {
      homeService.post(mutableArticleToArticleForBack(article)) onComplete {
        case Success(int) =>
          timeout( () => {
            adminScope.articles += article
          })
        case Failure(t: Throwable) =>
          console.log("error add tariff")
      }
    }
    else {
      homeService.update(mutableArticleToArticleForBack(article)) onComplete {
        case Success(int) =>
          console.log("youhou")
        case Failure(t: Throwable) =>
          console.log("error update tariff")
      }
    }
  }

  def deleteArticle(id: String): Unit = {
    homeService.delete(id) onComplete {
      case Success(int) =>
        adminScope.articles.find(_.id == id) match {
          case Some(article) =>
            timeout( () => {
              adminScope.articles.splice(adminScope.articles.indexOf(article), 1)
            })
          case _ =>
            console.log("no article for this id")
        }
      case Failure(t: Throwable) =>
        console.log("error delete article:" + t)
    }
  }

  //images
  getImages()
  def getImages(): Unit = {
    homeService.findHomeImages().onComplete {
      case Success(images) =>
        timeout( () => {
          adminScope.images = images.toJSArray
        })
      case Failure(t: Throwable) =>
        console.log("fail get images")
    }
  }

  def setImages(): Unit = {
    if (needToSave) alert("Veuillez sauvegarder ou annuler les changements")
    else {
      adminScope.formTemplate = "assets/templates/Admin/homeImages.html"
      adminScope.validate = () => {
        homeService.updateImages(adminScope.images.toSeq) onComplete {
          case Success(int) =>
            timeout(() => {
              needToSave = false
            })

          case Failure(t: Throwable) =>
            console.log("bad")
        }
      }
    }
  }


  // listeners

  adminScope.$watch("newImage", () => {
    console.log(adminScope.descente.images)
    if (adminScope.descente.images.indexOf(adminScope.newImage) == -1) {
      adminScope.descente.images += adminScope.newImage
      needToSave = true
    }
  })

}