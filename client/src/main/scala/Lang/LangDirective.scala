package Lang

import com.greencatsoft.angularjs.{ElementDirective, TemplatedDirective, injectable}
import org.scalajs.dom.console
import scala.scalajs.js.annotation.JSExport

@JSExport
@injectable("lang")
class LangDirective(langService: LangService) extends ElementDirective with TemplatedDirective {
  override val templateUrl = "assets/templates/Lang/lang.html"

  @JSExport
  def setLang(lang: String): Any = {
    console.log("tut")
    langService.set(lang)
  }
}
