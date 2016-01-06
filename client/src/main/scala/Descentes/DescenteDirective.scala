package Descentes

import com.greencatsoft.angularjs._
import com.greencatsoft.angularjs.core.Timeout
import org.scalajs.dom._
import org.scalajs.dom.html.Html

import scala.scalajs.js
import scala.scalajs.js.Object
import scala.scalajs.js.annotation.JSExport

@JSExport
@injectable("descente")
class DescenteDirective() extends ElementDirective with TemplatedDirective {
  override val templateUrl = "assets/templates/Descentes/descente.html"

  @JSExport
  def prevIndex(index: Int, maxIndex: Int): Int = {
    if (index > 0) index -1
    else maxIndex
  }
  @JSExport
  def nextIndex(index: Int, maxIndex: Int): Int = {
    if (index < maxIndex) index + 1
    else 0
  }
}


trait fullSliderParent extends js.Object {
  var $parent: fullSlider = js.native
}

trait fullSlider extends js.Object {
  var fullSlider: Boolean = js.native
}
@JSExport
@injectable("fullSlider")
class fullSliderDirective(timeout: Timeout) extends AttributeDirective {

  @JSExport
  var index = 0
  var maxIndex = 0

  @JSExport
  def prevIndex(): Unit = {
    if (index > 0) index = index -1
    else index = maxIndex
  }

  @JSExport
  def nextIndex(): Unit = {
    if (index < maxIndex) index = index + 1
    else index = 0
  }

  override def link(scopeType: ScopeType, elements: Seq[Element], attributes: Attributes): Unit ={
      elements.map{_.asInstanceOf[Html]}.foreach { element =>
        timeout ( () => {
          index = 0
          maxIndex = element.getAttribute("full-slider").toString.toInt
          console.log(document.getElementById("mainContent").scrollTop)
          console.log( element.parentElement.getBoundingClientRect().top)
          document.getElementById("mainContent").scrollTop = element.getBoundingClientRect().top + document.getElementById("mainContent").scrollTop
        })
        val eventKeyPress = (event: Event) => {
          val keyEvent = event.asInstanceOf[KeyboardEvent]
          if (keyEvent.keyCode == 37) timeout(() => {prevIndex()})
          if (keyEvent.keyCode == 39) timeout(() => {nextIndex()})
          if (keyEvent.keyCode == 27) {
            timeout(() => {
              scopeType.asInstanceOf[fullSliderParent].$parent.fullSlider = false
            }, 0, true)
          }

        }
        window.addEventListener("keydown", eventKeyPress)
        scopeType.$on("destroy",  () => window.removeEventListener("keydown", eventKeyPress))
      }
  }


}