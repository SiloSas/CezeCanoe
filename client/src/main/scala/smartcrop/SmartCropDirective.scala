package smartcrop

import com.greencatsoft.angularjs._
import com.greencatsoft.angularjs.core.{Timeout, Window}
import org.scalajs.dom.raw.{Event, UIEvent, HTMLImageElement}
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
      def getParentHeight(parent: Html): Int = {
        if (parent.getBoundingClientRect().height > 0) parent.getBoundingClientRect().height.toInt
        else getParentHeight(parent.parentNode.asInstanceOf[Html])
      }

      def getParentWidth(parent: Html): Int = {
        if (parent.getBoundingClientRect().width > 0)  {
          parent.getBoundingClientRect().width.toInt
        }
        else getParentHeight(parent.parentNode.asInstanceOf[Html])
      }

      def defineSize: CropSize = {
        val size = new Object().asInstanceOf[CropSize]
        val width = element.getAttribute("width")
        if (width != null) {
          if (width.indexOf("%") == -1) {
            size.width = width.toInt
          } else {
            val percent = width.replace("%", "").toInt
            size.width = getParentWidth(element.parentNode.asInstanceOf[Html]) * percent / 100
          }
        } else {
          size.width = getParentWidth(element)
        }
        val height = element.getAttribute("height")
        if (height != null) {
          if (height.indexOf("%") > -1) {
            val percent = height.replace("%", "").toInt
            size.height = getParentHeight(element) * percent / 100
          } else if (height.indexOf("w") > -1) {
            val ratio = height.replace("w", "").toDouble
            size.height = (getParentWidth(element.parentNode.asInstanceOf[Html]) * ratio).toInt
          } else {
            size.height = height.toInt
          }
        } else {
          size.height = 500
        }
        size
      }

      val image = element.getElementsByTagName("img").item(0).asInstanceOf[Image]

      val callback: Function = (result: CropResult) => {
//        timeout( fn = () => {
          val crop = result.topCrop
          val canvas = element.getElementsByTagName("canvas").item(0).asInstanceOf[Canvas]
          val ctx = canvas.getContext("2d")
          canvas.width = defineSize.width
          canvas.height = defineSize.height
          if (image.complete) {
            ctx.drawImage(image, crop.x, crop.y, crop.width, crop.height, 0, 0, canvas.width, canvas.height)
          }
//        }, 0, true)
      }

      def cropImage: Unit = {
        if (image.complete) {
          smartCropService.crop(image, defineSize, callback)
        } else {
          timeout( fn = () => {
            cropImage
          }, 10)
        }
      }
      timeout( fn = () => {
        scope.$apply()
        defineSize
        scope.$watch("activeImage", (newVal: Any) => {
          timeout( fn = () => {
          image.src = element.getAttribute("image")
          timeout( fn = () => {
            cropImage
          }, 20)
        }, 20, true)
        })

        val resize = (event: Event) => {
          defineSize
          cropImage
        }

        attrs.$observe("image", (newAttr: String) => {
          image.src = newAttr
          cropImage
        } )

        window.addEventListener("resize", resize)
        scope.$on("$destroy", () => {
          window.removeEventListener("resize", resize)
        })

      }, 10)
    }
  }
}