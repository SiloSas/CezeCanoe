package ServicesPage

import Admin.{VersionedStringScope, VersionedStringToBind, VersionedStringToBindScope, VersionedString}
import ArticleWithSlider.{ArticleWithSlider, ArticleWithSliderForBack}
import com.greencatsoft.angularjs.core.{SceService, HttpService}
import com.greencatsoft.angularjs.{Factory, Service, injectable}
import shared.{ArticleForBack, Article}
import upickle.default._
import scala.concurrent.Future
import scala.scalajs.js
import org.scalajs.dom.console
import scala.scalajs.js.{Object, JSON}
import scala.scalajs.js.annotation.JSExport
import scala.scalajs.js.JSConverters.JSRichGenTraversableOnce
import scala.concurrent.ExecutionContext.Implicits.global

@injectable("servicesService")
class ServicesService(http: HttpService, sce: SceService) extends Service {
  require(http != null, "Missing argument 'http'.")

  def versionedStringToVersionedStringToBindScope(presentation: VersionedString): VersionedStringToBindScope = {
    val pres = new Object().asInstanceOf[VersionedStringToBindScope]
    pres.lang = presentation.lang
    pres.presentation = sce.trustAs("html", presentation.presentation)
    pres
  }

  def versionedStringToVersionedStringScope(versionedString: VersionedString): VersionedStringScope = {
    val newVersionedString = new Object().asInstanceOf[VersionedStringScope]
    newVersionedString.lang = versionedString.lang
    newVersionedString.presentation = versionedString.presentation
    newVersionedString
  }

  def articleForBackToArticle(articleForBack: ArticleWithSliderForBack): ArticleWithSlider = {
    console.log(articleForBack.toString)
    ArticleWithSlider(articleForBack.id, articleForBack.content.map(versionedStringToVersionedStringToBindScope).toJSArray, articleForBack.images.toJSArray)
  }

  @JSExport
  def findAll(): Future[Seq[ArticleWithSlider]] = {
    http.get[js.Any](s"/services")
      .map(JSON.stringify(_))
      .map { stringArticle =>
        console.log(stringArticle)
        read[Seq[ArticleWithSliderForBack]](stringArticle).map { article =>
          articleForBackToArticle(article)
        }
      }
  }

  @JSExport
  def update(articleForBack: ArticleWithSliderForBack): Future[String] = {
    http.put[js.Any](s"/services", write(articleForBack))
      .map(JSON.stringify(_))
  }

  @JSExport
  def post(articleForBack: ArticleWithSliderForBack): Future[String] = {
    http.post[js.Any](s"/services", write(articleForBack))
      .map(JSON.stringify(_))
  }

  @JSExport
  def delete(id: String): Future[String] = {
    http.delete[js.Any](s"/services/"+ id)
      .map(JSON.stringify(_))
  }

}


@injectable("servicesService")
class ServicesServiceFactory(http: HttpService, sce: SceService) extends Factory[ServicesService] {

  override def apply(): ServicesService = new ServicesService(http, sce: SceService)
}
