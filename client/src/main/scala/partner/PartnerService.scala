package partner

import Admin.{VersionedString, VersionedStringScope, VersionedStringToBindScope}
import com.greencatsoft.angularjs.core.{SceService, HttpService}
import com.greencatsoft.angularjs.{Service, Factory, injectable}
import org.scalajs.dom.console
import upickle.default._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.scalajs.js
import scala.scalajs.js.JSConverters.JSRichGenTraversableOnce
import scala.scalajs.js.annotation.JSExport
import scala.scalajs.js.{JSON, Object}

@injectable("partnersService")
class PartnersService(http: HttpService, sce: SceService) extends Service {
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

  def articleForBackToArticle(partnerForBack: PartnerForBack): Partner = {
    console.log(partnerForBack.toString)
    Partner(partnerForBack.id, partnerForBack.content.map(versionedStringToVersionedStringToBindScope).toJSArray, partnerForBack.media, partnerForBack.link)
  }

  @JSExport
  def findAll(): Future[Seq[Partner]] = {
    http.get[js.Any](s"/partners")
      .map(JSON.stringify(_))
      .map { stringArticle =>
        console.log(stringArticle)
        read[Seq[PartnerForBack]](stringArticle).map { article =>
          articleForBackToArticle(article)
        }
      }
  }

  @JSExport
  def update(articleForBack: PartnerForBack): Future[String] = {
    http.put[js.Any](s"/partners", write(articleForBack))
      .map(JSON.stringify(_))
  }

  @JSExport
  def post(articleForBack: PartnerForBack): Future[String] = {
    http.post[js.Any](s"/partners", write(articleForBack))
      .map(JSON.stringify(_))
  }

  @JSExport
  def delete(id: String): Future[String] = {
    http.delete[js.Any](s"/partners/"+ id)
      .map(JSON.stringify(_))
  }

}


@injectable("partnersService")
class PartnersServiceFactory(http: HttpService, sce: SceService) extends Factory[PartnersService] {

  override def apply(): PartnersService = new PartnersService(http, sce)
}
