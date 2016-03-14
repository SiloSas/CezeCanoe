package Admin

import java.util.UUID

import ArticleWithSlider.{ArticleWithSlider, ArticleWithSliderForBack}
import Booking.{BookingDetail, BookingForm, BookingFormClient, BookingService}
import Descentes.DescenteService
import DescentsClient._
import Home.{ArticleForBack, HomeService}
import Lang.LangService
import Occasions.OccasionService
import ServicesPage.ServicesService
import com.greencatsoft.angularjs.core.{SceService, Timeout}
import com.greencatsoft.angularjs.{AbstractController, injectable}
import org.scalajs.dom.{alert, console}
import partner.{Partner, PartnerForBack, PartnersService}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.scalajs.js
import scala.scalajs.js.JSConverters.JSRichGenTraversableOnce
import scala.scalajs.js.Object
import scala.scalajs.js.annotation.JSExportAll
import scala.util.{Failure, Success}


@JSExportAll
@injectable("adminController")
class AdminController(adminScope: AdminScope, descenteService: DescenteService, $sce: SceService, servicesService: ServicesService,
                      langService: LangService, timeout: Timeout, homeService: HomeService, bookingService: BookingService,
                      occasionService: OccasionService, partnerService: PartnersService)
  extends AbstractController[AdminScope](adminScope) {

  // init var & scope
  adminScope.newImage = ""
  adminScope.descente = new Object().asInstanceOf[DescenteMutable]
//  adminScope.bookings = js.Array[BookingForm]()
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
      presentation = descente.presentations.toSeq map versionedStringScopeToVersionedString,
      tour = descente.tour.toSeq map versionedStringScopeToVersionedString,
      images = descente.images.toSeq,
      distance = descente.distance.toSeq map versionedStringScopeToVersionedString,
      prices = newPrices.toSeq,
      time = descente.time.toSeq map versionedStringScopeToVersionedString,
      isVisible = descente.isVisible,
      groupReduction = descente.groupReduction)
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
    newDescente.presentations = descente.presentation map {a =>
      val b =new js.Object().asInstanceOf[VersionedStringScope]
      b.presentation = a.presentation.toString
      b.lang = a.lang
      b
    }
    newDescente.distance = descente.distance
    newDescente.images = descente.images
    newDescente.prices = descente.prices map { price =>
      val newPrice = new Object().asInstanceOf[Price]
      newPrice.id = price.id
      newPrice.isBookable = price.isBookable
      newPrice.isSupplement = price.isSupplement
      newPrice.medias = price.medias
      newPrice.name = price.name
      newPrice.price = price.price
      newPrice
    }
    newDescente.time = descente.time
    newDescente.tour = descente.tour
    newDescente.isVisible = descente.isVisible
    newDescente.groupReduction = descente.groupReduction
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
      newDescente.presentations = emptyVersionedStringArray()
      newDescente.distance = emptyVersionedStringArray()
      newDescente.images = Seq.empty[String].toJSArray
      newDescente.prices = Seq.empty[Price].toJSArray
      newDescente.time = emptyVersionedStringArray()
      newDescente.tour = emptyVersionedStringArray()
      newDescente.isVisible = true
      newDescente.groupReduction = 0.0
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
          getPrices
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

  def getPrices: Unit = {
    descenteService.findTariffs().onComplete {
      case Success(prices) =>
        getBooking()
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
              adminScope.articles.push(adminScope.article)
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
        console.log(adminScope.images)
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

  //Booking
  def getBooking(): Unit = {
    bookingService.get().onComplete {
      case Success(bookings) =>
        timeout( () => {
          console.log(bookings)
          val test = bookings.toJSArray.map{booking =>
            BookingForm(booking.id, booking.descentId, BookingFormClient(booking.bookingFormClient.startTime, booking.bookingFormClient.date,
              booking.bookingFormClient.name, booking.bookingFormClient.firstName, booking.bookingFormClient.address,
              booking.bookingFormClient.phoneNumber, booking.bookingFormClient.email, booking.bookingFormClient.isGroup), booking.details, booking.isGroup)
          }
          adminScope.bookings = test
        })
      case _ =>
        console.log("error get bookings")
    }
  }

  def computePrice(descenteId: String, prices: js.Array[BookingDetail]): Double = {
    adminScope.descentes.find(_.id == descenteId) match {
      case Some(descente) =>
        console.log(1)
        var priceRef = 0.0
        var total = 0.0
        descente.prices.headOption match {
          case Some(price) =>
            priceRef = price.price
          case _ =>
            priceRef = 0.0
        }
        prices.foreach {price =>
          console.log(descente.prices)
          descente.prices.find(_.id == price.priceId) match {
            case Some(descentPriceSupplement) if descentPriceSupplement.isSupplement =>
              total = total + (descentPriceSupplement.price + priceRef) * price.number
            case Some(descentPrice) =>
              console.log("yo1")
              total = total + descentPrice.price * price.number
            case _ =>
              adminScope.prices.find(_.id == price.priceId) match {
                case Some(descentPriceSupplement) if descentPriceSupplement.isSupplement =>
                  total = total + (descentPriceSupplement.price + priceRef) * price.number
                case Some(descentPrice) =>
                  total = total + descentPrice.price * price.number
                case _ =>
                  total = total
              }
          }
        }
        total
    }
  }

  // Services

  getServices()
  def getServices(): Unit = {
    servicesService.findAll().onComplete {
      case Success(services) =>
        timeout( () => {
          adminScope.services = services.toJSArray
        })
      case Failure(t: Throwable) =>
        console.log("fail get services")
    }
  }

  def setService(service: ArticleWithSlider): Unit = {
    if (needToSave) alert("Veuillez sauvegarder ou annuler les changements")
    else {
      val newService = new Object().asInstanceOf[ArticleWithSliderMutable]
      newService.id = service.id
      newService.content = service.content
      newService.images = service.images
      adminScope.service = newService
      console.log(newService)
      adminScope.formTemplate = "assets/templates/Admin/servicesForm.html"
      adminScope.validate = () => {
        val articleToPost = ArticleWithSliderForBack(id = adminScope.service.id, content = adminScope.service.content.toSeq.map(versionedStringToBindScopeToVersionedString),
          adminScope.service.images)
        servicesService.update(articleToPost) onComplete {
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

  def setNewService(): Unit = {
    if (needToSave) alert("Veuillez sauvegarder ou annuler les changements")
    else {
      adminScope.formTemplate = "assets/templates/Admin/servicesForm.html"
      val newService = new Object().asInstanceOf[ArticleWithSliderMutable]
      newService.id = UUID.randomUUID().toString
      newService.content = emptyVersionedStringToBindArray()
      newService.images = Seq.empty[String].toJSArray
      adminScope.service = newService
      adminScope.validate = () => {
      val articleToPost = ArticleWithSliderForBack(id = adminScope.service.id, content = adminScope.service.content.toSeq.map(versionedStringToBindScopeToVersionedString),
          adminScope.service.images)
        postService(articleToPost)
      }
    }
  }

  def postService(articleWithSlider: ArticleWithSliderForBack): Unit = {
    servicesService.post(articleWithSlider) onComplete {
      case Success(int) =>
        timeout ( () => {
          adminScope.services.push(ArticleWithSlider(adminScope.service.id, adminScope.service.content, adminScope.service.images))
          needToSave = false
          console.log("success")
        })
      case _ =>
        console.log("error post service")
    }
  }

  def deleteService(id: String): Unit = {
    servicesService.delete(id) onComplete {
      case Success(int) =>
        adminScope.services.find(_.id == id) match {
          case Some(service) =>
            timeout( () => {
              adminScope.services.splice(adminScope.services.indexOf(service), 1)
            })
          case _ =>
            console.log("no service for this id")
        }
      case Failure(t: Throwable) =>
        console.log("error delete service:" + t)
    }
  }
  // Occasions

  getOccasions()
  def getOccasions(): Unit = {
    occasionService.findAll().onComplete {
      case Success(occasions) =>
        timeout( () => {
          adminScope.occasions = occasions.toJSArray
        })
      case Failure(t: Throwable) =>
        console.log("fail get occasions")
    }
  }

  def setOccasion(occasion: ArticleWithSlider): Unit = {
    if (needToSave) alert("Veuillez sauvegarder ou annuler les changements")
    else {
      val newOccasion = new Object().asInstanceOf[ArticleWithSliderMutable]
      newOccasion.id = occasion.id
      newOccasion.content = occasion.content
      newOccasion.images = occasion.images
      adminScope.occasion = newOccasion
      console.log(newOccasion)
      adminScope.formTemplate = "assets/templates/Admin/occasionForm.html"
      adminScope.validate = () => {
        val articleToPost = ArticleWithSliderForBack(id = adminScope.occasion.id, content = adminScope.occasion.content.toSeq.map(versionedStringToBindScopeToVersionedString),
          adminScope.occasion.images)
        occasionService.update(articleToPost) onComplete {
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

  def setNewOccasion(): Unit = {
    if (needToSave) alert("Veuillez sauvegarder ou annuler les changements")
    else {
      adminScope.formTemplate = "assets/templates/Admin/occasionForm.html"
      val newOccasion = new Object().asInstanceOf[ArticleWithSliderMutable]
      newOccasion.id = UUID.randomUUID().toString
      newOccasion.content = emptyVersionedStringToBindArray()
      newOccasion.images = Seq.empty[String].toJSArray
      adminScope.occasion = newOccasion
      adminScope.validate = () => {
      val articleToPost = ArticleWithSliderForBack(id = adminScope.occasion.id, content = adminScope.occasion.content.toSeq.map(versionedStringToBindScopeToVersionedString),
          adminScope.occasion.images)
        postOccasion(articleToPost)
      }
    }
  }

  def postOccasion(articleWithSlider: ArticleWithSliderForBack): Unit = {
    occasionService.post(articleWithSlider) onComplete {
      case Success(int) =>
        timeout ( () => {
          adminScope.occasions.push(ArticleWithSlider(adminScope.occasion.id, adminScope.occasion.content, adminScope.occasion.images))
          needToSave = false
          console.log("success")
        })
      case _ =>
        console.log("error post occasion")
    }
  }

  def deleteOccasion(id: String): Unit = {
    occasionService.delete(id) onComplete {
      case Success(int) =>
        adminScope.occasions.find(_.id == id) match {
          case Some(occasion) =>
            timeout( () => {
              adminScope.occasions.splice(adminScope.occasions.indexOf(occasion), 1)
            })
          case _ =>
            console.log("no occasion for this id")
        }
      case Failure(t: Throwable) =>
        console.log("error delete occasion:" + t)
    }
  }
   
   // Partners

  getPartners()
  def getPartners(): Unit = {
    partnerService.findAll().onComplete {
      case Success(partners) =>
        timeout( () => {
          adminScope.partners = partners.toJSArray
        })
      case Failure(t: Throwable) =>
        console.log("fail get partners")
    }
  }

  def setPartner(partner: Partner): Unit = {
    if (needToSave) alert("Veuillez sauvegarder ou annuler les changements")
    else {
      val newPartner = new Object().asInstanceOf[PartnerMutable]
      newPartner.id = partner.id
      newPartner.content = partner.content
      newPartner.media = partner.media
      newPartner.link = partner.link
      adminScope.partner = newPartner
      console.log(newPartner)
      adminScope.formTemplate = "assets/templates/Admin/partnersForm.html"
      adminScope.validate = () => {
        val partnerToPost = PartnerForBack(id = adminScope.partner.id, content = adminScope.partner.content.toSeq.map(versionedStringToBindScopeToVersionedString),
          adminScope.partner.media, adminScope.partner.link)
        partnerService.update(partnerToPost) onComplete {
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

  def setNewPartner(): Unit = {
    if (needToSave) alert("Veuillez sauvegarder ou annuler les changements")
    else {
      adminScope.formTemplate = "assets/templates/Admin/partnersForm.html"
      val newPartner = new Object().asInstanceOf[PartnerMutable]
      newPartner.id = UUID.randomUUID().toString
      newPartner.content = emptyVersionedStringToBindArray()
      newPartner.media = ""
      newPartner.link = ""
      adminScope.partner = newPartner
      adminScope.validate = () => {
        val partnerToPost = PartnerForBack(id = adminScope.partner.id, content = adminScope.partner.content.toSeq.map(versionedStringToBindScopeToVersionedString),
          adminScope.partner.media, adminScope.partner.link)
        postPartner(partnerToPost)
      }
    }
  }

  def postPartner(partnerForBack: PartnerForBack): Unit = {
    partnerService.post(partnerForBack) onComplete {
      case Success(int) =>
        timeout ( () => {
          adminScope.partners.push(Partner(adminScope.partner.id, adminScope.partner.content, adminScope.partner.media, adminScope.partner.link))
          needToSave = false
          console.log("success")
        })
      case _ =>
        console.log("error post partner")
    }
  }

  def deletePartner(id: String): Unit = {
    partnerService.delete(id) onComplete {
      case Success(int) =>
        adminScope.partners.find(_.id == id) match {
          case Some(partner) =>
            timeout( () => {
              adminScope.partners.splice(adminScope.partners.indexOf(partner), 1)
            })
          case _ =>
            console.log("no partner for this id")
        }
      case Failure(t: Throwable) =>
        console.log("error delete partner:" + t)
    }
  }
   
   // Informations

  getInformations()
  def getInformations(): Unit = {
    descenteService.findInformations().onComplete {
      case Success(informations) =>
        timeout( () => {
          adminScope.informations = informations.toJSArray
        })
      case Failure(t: Throwable) =>
        console.log("fail get informations")
    }
  }

  def setInformation(information: Information): Unit = {
    if (needToSave) alert("Veuillez sauvegarder ou annuler les changements")
    else {
      val newInformation = new Object().asInstanceOf[InformationMutable]
      newInformation.id = information.id
      newInformation.information = information.information
      adminScope.information = newInformation
      console.log(newInformation)
      adminScope.formTemplate = "assets/templates/Admin/informationsForm.html"
      adminScope.validate = () => {
        val informationToPost = InformationForBack(id = adminScope.information.id,
          information = adminScope.information.information.toSeq.map(versionedStringToBindScopeToVersionedString))
        descenteService.updateInformations(informationToPost) onComplete {
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

  def setNewInformation(): Unit = {
    if (needToSave) alert("Veuillez sauvegarder ou annuler les changements")
    else {
      adminScope.formTemplate = "assets/templates/Admin/informationsForm.html"
      val newInformation = new Object().asInstanceOf[InformationMutable]
      newInformation.id = UUID.randomUUID().toString
      newInformation.information = emptyVersionedStringToBindArray()
      adminScope.information = newInformation
      adminScope.validate = () => {
        val informationToPost = InformationForBack(id = adminScope.information.id,
          information = adminScope.information.information.toSeq.map(versionedStringToBindScopeToVersionedString))
        postInformation(informationToPost)
      }
    }
  }

  def postInformation(informationForBack: InformationForBack): Unit = {
    descenteService.postInformations(informationForBack) onComplete {
      case Success(int) =>
        timeout ( () => {
          adminScope.informations.push(Information(adminScope.information.id, adminScope.information.information))
          needToSave = false
          console.log("success")
        })
      case _ =>
        console.log("error post information")
    }
  }

  def deleteInformation(id: String): Unit = {
    descenteService.deleteInformations(id) onComplete {
      case Success(int) =>
        adminScope.informations.find(_.id == id) match {
          case Some(information) =>
            timeout( () => {
              adminScope.informations.splice(adminScope.informations.indexOf(information), 1)
            })
          case _ =>
            console.log("no information for this id")
        }
      case Failure(t: Throwable) =>
        console.log("error delete information:" + t)
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