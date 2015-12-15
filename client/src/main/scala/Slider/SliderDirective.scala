package Slider

import com.greencatsoft.angularjs.core.{Timeout, Window}
import com.greencatsoft.angularjs._
import org.scalajs.dom.{Element, document}
import org.scalajs.dom.html.Html
import org.scalajs.dom.raw.UIEvent

import scala.scalajs.js.annotation.JSExport

@JSExport
@injectable("slider")
class SliderDirective(window: Window, timeout: Timeout) extends ElementDirective with TemplatedDirective {
  override val templateUrl = "assets/templates/Slider/slider.html"

  override def link(scope: ScopeType, elements: Seq[Element], attrs: Attributes): Unit = {
    elements.headOption.map(_.asInstanceOf[Html]) foreach { element =>
      def setNewHeight(newHeight: Double): Unit = {
          if (newHeight < window.innerHeight) {
            element.style.height = newHeight + "px"
          } else {
            element.style.height = window.innerHeight + "px"
          }
      }
      timeout (fn = () => {
        setNewHeight(window.innerWidth * 0.4)
      }, 50, false)

      window.onresize = (event: UIEvent) =>
        setNewHeight(window.innerWidth * 0.4)

    }
  }
}

@JSExport
@injectable("sliderContent")
class SliderContentDirective(window: Window, timeout: Timeout) extends ClassDirective {

  override def link(scope: ScopeType, elements: Seq[Element], attrs: Attributes): Unit = {
    elements.headOption.map(_.asInstanceOf[Html]) foreach { element =>
      def setNewHeight(newHeight: Double): Unit = {
          if (newHeight < window.innerHeight) {
            element.style.marginTop = newHeight + "px"
          } else {
            element.style.marginTop = window.innerHeight + "px"
          }
      }

      timeout (fn = () => {
        setNewHeight(window.innerWidth * 0.4)
      }, 50, false)

      window.onresize = (event: UIEvent) =>
        setNewHeight(window.innerWidth * 4)

    }
  }
}
