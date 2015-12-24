package VideoPlayer

import com.greencatsoft.angularjs.core.{Window, Timeout}
import com.greencatsoft.angularjs.{Attributes, ElementDirective, injectable}
import org.scalajs.dom.html._
import org.scalajs.dom.{Element, console}
import scala.scalajs.js
import scala.scalajs.js.annotation.JSExport

@JSExport
@injectable("videoPlayer")
class VideoPlayerDirective(timeout: Timeout, window: Window) extends ElementDirective {

  override def link(scope: ScopeType, elements: Seq[Element], attrs: Attributes): Unit = {
    elements.headOption.map(_.asInstanceOf[Html]) foreach { element =>
      timeout(fn = () => {
        val url = element.getAttribute("url")
        val size = js.Object.asInstanceOf[VideoPlayerSize]

        def getParentHeight(parent: Html): Int = {
          if (parent.getBoundingClientRect().height > 20) parent.getBoundingClientRect().height.toInt
          else getParentHeight(parent.parentNode.asInstanceOf[Html])
        }

        def defineSize: Unit = {
          val width = element.getAttribute("width")
          if (width != null) {
            if (width.indexOf("%") == -1) {
              size.width = width.toInt
            } else {
              val percent = width.replace("%", "").toInt
              size.width = (element.parentNode.asInstanceOf[Html].getBoundingClientRect().width * percent / 100).toInt
            }
          } else {
            size.width = window.innerWidth
          }
          val height = element.getAttribute("height")
          if (height != null) {
            if (height.indexOf("%") == -1) {
              size.height = height.toInt
            } else {
              val percent = height.replace("%", "").toInt
              size.height = getParentHeight(element) * percent / 100
            }
          } else {
            size.height = 500
          }
        }
        defineSize
        element.style.height =  size.height + "px"
        element.innerHTML = "<object width=\"100%\" height=\" 100%\">" +
          "<iframe src=\"" + url.replace("http:", "") +
          "?wmode=transparent&autoplay=0&theme=dark&controls=1&autohide=0&loop=0&showinfo=0&rel=0&playlist=false&enablejsapi=0\"" +
          "type=\"application/x-shockwave-flash\"" +
          "allowscriptaccess=\"always\"width=\"100%\" height=\"100%\" style='border: none'>" +
          "</iframe></object>"
      }, 10, true)
    }
  }
}

