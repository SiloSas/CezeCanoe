package smartcrop

import com.greencatsoft.angularjs._
import com.greencatsoft.angularjs.core.{Timeout, Window}
import org.scalajs.dom.raw.{UIEvent, HTMLImageElement}
import org.scalajs.dom.{Element, console}
import org.scalajs.dom.html.{Image, Canvas, Html}
import scala.scalajs.js
import scala.scalajs.js.{Any, Function, Object}
import scala.scalajs.js.annotation.{JSName, JSExport}

@JSExport
@injectable("smartCrop")
class SmartCropDirective(window: Window, smartCropService: SmartCropService, timeout: Timeout) extends ElementDirective with TemplatedDirective {

  override val templateUrl = "assets/templates/smartcrop/smartcrop.html"

  override def link(scope: ScopeType, elements: Seq[Element], attrs: Attributes): Unit = {

    elements.headOption.map(_.asInstanceOf[Html]) foreach { element =>
      val size = new Object().asInstanceOf[CropSize]

      def getParentHeight(parent: Html): Int = {
        if (parent.getBoundingClientRect().height > 0) parent.getBoundingClientRect().height.toInt
        else getParentHeight(parent.parentNode.asInstanceOf[Html])
      }

      def defineSize: Unit = {
        val width = element.getAttribute("width")
        if (width != null) {
          if (width.indexOf("%") == -1) {
            size.width = width.toInt
          } else {
            val percent = width.replace("%", "").toInt
            size.width = window.innerWidth * percent / 100
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

      val image = element.getElementsByTagName("img").item(0).asInstanceOf[Image]

      val callback: Function = (result: CropResult) => {
        console.log("result", result)
        val crop = result.topCrop
        val canvas = element.getElementsByTagName("canvas").item(0).asInstanceOf[Canvas]
        val ctx = canvas.getContext("2d")
        canvas.width = size.width
        canvas.height = size.height
        if (image.complete) {
          ctx.drawImage(image, crop.x, crop.y, crop.width, crop.height, 0, 0, canvas.width, canvas.height)
        }
      }

      def cropImage: Unit = {
        if (image.complete) {
          smartCropService.crop(image, size, callback)
        } else {
          timeout( fn = () => {
            cropImage
          }, 10)
        }
      }
      scope.$watch("activeImage", (newVal: Any) => {
        console.log(newVal)
      timeout( fn = () => {
        image.src = element.getAttribute("image")
        scope.$apply()
        console.log(image)
        timeout( fn = () => {
          cropImage
        }, 0)
      }, 0, true)
      })
      window.onresize = (event: UIEvent) => {
        defineSize
        cropImage
      }


     /* val a = scope.$watch(element.getAttribute("image"), new js.Function() {
        //image.src = element.getAttribute("image")
        console.log(image)
        /*timeout( fn = () => {
          cropImage
        }, 0)*/
      })
      console.log(a)*/
       //, function(newValue){});
    }
  }
}