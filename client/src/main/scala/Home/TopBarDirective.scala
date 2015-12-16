package Home


import com.greencatsoft.angularjs.core.{Timeout, Window}
import com.greencatsoft.angularjs._
import org.scalajs.dom.{Element, document}
import org.scalajs.dom.html.Html
import org.scalajs.dom.raw.UIEvent
import org.scalajs.dom.raw.Event
import com.greencatsoft.angularjs.extensions.material.Sidenav
import org.scalajs.dom.console

import scala.scalajs.js.annotation.JSExport


@JSExport
@injectable("topBar")
class TopBarDirective(window: Window, sidenav: Sidenav) extends ClassDirective {

  override def link(scope: ScopeType, elements: Seq[Element], attrs: Attributes): Unit = {
    elements.headOption.map (_.asInstanceOf[Html] ) foreach { element =>
      def setTopBarVisibility(): Unit = {
        val sliderHeight = document.getElementsByTagName("slider").item(0).asInstanceOf[Html].getBoundingClientRect().height + 20
        if (document.getElementsByTagName("md-content").item(0).asInstanceOf[Html].scrollTop > sliderHeight) {
          document.getElementById("title").classList.add("ng-hide")
          element.classList.remove("ng-hide")
        }
        else {
          element.classList.add("ng-hide")
          document.getElementById("title").classList.remove("ng-hide")
        }
      }
      def removeScrollListener() = {
        document.getElementById("mainContent").asInstanceOf[Html].onscroll = (event: Event) => null
        document.getElementById("title").classList.add("ng-hide")
        element.classList.remove("ng-hide")
      }
      def addScrollListener() = {
        document.getElementById("mainContent").asInstanceOf[Html].onscroll = (event: Event) => setTopBarVisibility()
        document.getElementById("title").classList.remove("ng-hide")
        element.classList.add("ng-hide")
      }
      def setScroll: Unit = {
        if (window.innerWidth > 950) {
          addScrollListener()
        } else {
          removeScrollListener()
        }
      }
      setScroll
      window.addEventListener("resize", (event: UIEvent) => {
        setScroll
      })
    }
  }
}


@JSExport
@injectable("topBarContent")
class TopBarContentDirective(sidenav: Sidenav) extends ElementDirective with TemplatedDirective {
  override val templateUrl = "assets/templates/Home/topBar.html"
  @JSExport
  def toggleLeft(): Any = {
    console.log("yo")
    sidenav("left").toggle()
  }
}