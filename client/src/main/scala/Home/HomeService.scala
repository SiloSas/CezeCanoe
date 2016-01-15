package Home

import Admin.{VersionedString, VersionedStringScope, VersionedStringToBindScope}
import com.greencatsoft.angularjs.core.{HttpService, SceService}
import com.greencatsoft.angularjs.{Factory, Service, injectable}
import org.scalajs.dom.console
import upickle.default._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.scalajs.js
import scala.scalajs.js.JSConverters.JSRichGenTraversableOnce
import scala.scalajs.js.annotation.JSExport
import scala.scalajs.js.{JSON, Object}

@injectable("homeService")
class HomeService(http: HttpService, sce: SceService) extends Service {
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

  def articleForBackToArticle(articleForBack: ArticleForBack): Article = {
    console.log(articleForBack.toString)
    Article(articleForBack.id, articleForBack.content.map(versionedStringToVersionedStringToBindScope).toJSArray, articleForBack.media,
      articleForBack.yellowThing.map(versionedStringToVersionedStringScope).toJSArray)
  }

  @JSExport
  def findAll(): Future[Seq[Article]] = {
    http.get[js.Any](s"/articles")
      .map(JSON.stringify(_))
      .map { stringArticle =>
        console.log(stringArticle)
        read[Seq[ArticleForBack]](stringArticle).map { article =>
          articleForBackToArticle(article)
        }
      }
  }
  @JSExport
  def findHomeImages(): Future[Seq[String]] = {
    http.get[js.Any](s"/homeImages")
      .map(JSON.stringify(_))
      .map { stringArticle =>
        console.log(stringArticle)
        read[Seq[String]](stringArticle)
      }
  }
  @JSExport
  def updateImages(images: Seq[String]): Future[String] = {
    console.log(write(images))
    http.put[js.Any](s"/homeImages", write(images))
      .map(JSON.stringify(_))
  }

  @JSExport
  def update(articleForBack: ArticleForBack): Future[String] = {
    http.put[js.Any](s"/articles", write(articleForBack))
      .map(JSON.stringify(_))
  }

  @JSExport
  def post(articleForBack: ArticleForBack): Future[String] = {
    http.post[js.Any](s"/articles", write(articleForBack))
      .map(JSON.stringify(_))
  }

  @JSExport
  def delete(id: String): Future[String] = {
    http.delete[js.Any](s"/articles/"+ id)
      .map(JSON.stringify(_))
  }

}


@injectable("homeService")
class HomeServiceFactory(http: HttpService, sce: SceService) extends Factory[HomeService] {

  override def apply(): HomeService = new HomeService(http, sce: SceService)
}
