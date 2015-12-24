package Lang

import com.greencatsoft.angularjs.core.{RootScope, Scope}
import com.greencatsoft.angularjs.{Factory, Service, injectable}

import scala.concurrent.Future
import org.scalajs.dom.console
import scala.scalajs.concurrent.JSExecutionContext.Implicits.runNow
import scala.scalajs.js
import scala.scalajs.js.annotation.JSExport
import scala.util.{Failure, Success, Try}

@injectable("langService")
class LangService(rootScope: RootScope) extends Service {
  var lang = "Fr"
  var langs = Seq("Fr", "En")

  @JSExport
  def getAvailableLang(): Seq[String] = {
    langs
  }

  @JSExport
  def get(scope: Scope ,callback: js.Function): String = /*flatten*/ {
    var handler = rootScope.$on("notifying", callback)
    /*scope.$on("$destroy", () => {
      handler
    })*/
    lang
  }

  @JSExport
  def set(newLang: String): Unit = /*flatten*/ {
    lang = newLang
    rootScope.$emit("notifying")
  }

  @JSExport
  def setLang(string: String, lang: String): String = {
    "(.lang_\\w*.)".r.replaceAllIn(
      string.trim.split("(?=.lang_\\w*.)").find(_.indexOf("lang_" + lang) > -1) match {
        case Some(presentationInLang) =>
          presentationInLang
        case _ =>
          string.split("(?=.lang_\\w*.)").head
      }, ""
    )
  }

  protected def flatten[T](future: Future[Try[T]]): Future[T] = future flatMap {
    case Success(s) => Future.successful(s)
    case Failure(f) => Future.failed(f)
  }
  protected def parameterizeUrl(url: String, parameters: Map[String, Any]): String = {
    require(url != null, "Missing argument 'url'.")
    require(parameters != null, "Missing argument 'parameters'.")

    parameters.foldLeft(url)((base, kv) =>
      base ++ { if (base.contains("?")) "&" else "?" } ++ kv._1 ++ "=" + kv._2)
  }
}


@injectable("langService")
class LangServiceFactory(rootScope: RootScope) extends Factory[LangService] {

  override def apply(): LangService = new LangService(rootScope: RootScope)
}
