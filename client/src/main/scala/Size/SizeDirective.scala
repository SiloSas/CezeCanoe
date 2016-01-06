package Size

import com.greencatsoft.angularjs.core.{Timeout, Event, Window}
import com.greencatsoft.angularjs._
import org.scalajs.dom.console
import scala.scalajs.js.annotation.{JSExportAll, JSExport}

@JSExport
@injectable("size")
class SizeDirective(window: Window, timeout: Timeout) extends AttributeDirective {

  @JSExport
  var width = window.innerWidth
  @JSExport
  var height = window.innerHeight

  def setSizesEvent = (event: Event) => {
    setSizes
  }

  def setSizes: Unit = {
    timeout(() => {
      width = window.innerWidth
      height = window.innerHeight
      console.log(width)
    }, 0, true)
  }

  setSizes
  window.addEventListener("resize", setSizesEvent)

}
