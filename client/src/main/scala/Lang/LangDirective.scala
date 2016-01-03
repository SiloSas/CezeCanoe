package Lang

import com.greencatsoft.angularjs.{ElementDirective, TemplatedDirective, injectable}
import org.scalajs.dom.console
import scala.scalajs.js.annotation.{JSExportAll, JSExport}

@JSExport
@injectable("lang")
class LangDirective(langService: LangService) extends ElementDirective with TemplatedDirective {
  override val templateUrl = "assets/templates/Lang/lang.html"

  @JSExport
  def setlang(lang: String): Unit = {
    langService.set(lang)
  }
}
