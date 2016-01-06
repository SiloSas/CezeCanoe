package Lang

import com.greencatsoft.angularjs.core.{Timeout, Compile}
import com.greencatsoft.angularjs.{IsolatedScope, ElementDirective, TemplatedDirective, injectable}
import org.scalajs.dom.console
import scala.scalajs.js.annotation.{JSExportAll, JSExport}

@JSExport
@injectable("lang")
class LangDirective(langService: LangService,  compile: Compile ,timeout: Timeout) extends ElementDirective
with TemplatedDirective with IsolatedScope {
  override val templateUrl = "assets/templates/Lang/lang.html"

  @JSExport
  var currentLang = "Fr"

  @JSExport
  def setNewLang(newLang: String): Unit = {
    console.log("yoyoyoyooy")
    currentLang = newLang
    langService.set(newLang)
  }
  timeout( () => {
    setNewLang(currentLang)
    compile
  }, 2000, true)
}
