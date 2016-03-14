package Admin

import com.greencatsoft.angularjs.core.Timeout
import com.greencatsoft.angularjs._
import org.scalajs.dom.Element
import org.scalajs.dom.html.Html

import scala.scalajs.js.annotation.JSExport

@JSExport
@injectable("height")
class heightDirective(timeout: Timeout) extends AttributeDirective {

  override def link(scopeType: ScopeType, elements: Seq[Element], attributes: Attributes): Unit = {
    elements.map(_.asInstanceOf[Html]).foreach { element =>
      val height = element.getAttribute("height").toDouble
      org.scalajs.dom.console.log(height)
      element.style.height = element.clientWidth.toDouble * height + "px"
    }
  }
}