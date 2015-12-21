package Slider

import com.greencatsoft.angularjs.core.{Timeout, Window}
import com.greencatsoft.angularjs._
import org.scalajs.dom.{Element, document, console}
import org.scalajs.dom.html._
import org.scalajs.dom.raw.{Event, UIEvent}
import smartcrop.{SmartCropService, CropResult, CropSize}

import scala.scalajs.js
import scala.scalajs.js.{UndefOr, Any, Function, Object}
import scala.scalajs.js.annotation.JSExport
import scala.scalajs.js.JSConverters.JSRichGenTraversableOnce

@JSExport
@injectable("slider")
class SliderDirective(window: Window, timeout: Timeout, smartCropService: SmartCropService) extends ElementDirective with TemplatedDirective  {
  override val templateUrl = "assets/templates/Slider/slider.html"

  override def link(scope: ScopeType, elements: Seq[Element], attrs: Attributes): Unit = {
    elements.headOption.map(_.asInstanceOf[Html]) foreach { element =>

      var images: js.Array[String] = element.getAttribute("images").split(",").toSeq.toJSArray
      val imageContainer = element.getElementsByTagName("img").item(0).asInstanceOf[Image]
      val imageContainer1 = element.getElementsByTagName("img").item(1).asInstanceOf[Image]
      val slide = element.getElementsByTagName("li").item(0).asInstanceOf[Html]
      val slide1 = element.getElementsByTagName("li").item(1).asInstanceOf[Html]
      var activeSlide = 0
      var play = true

      def setNewHeight(newHeight: Double): Unit = {
        if (newHeight < window.innerHeight) {
            element.style.height = newHeight + "px"
          } else {
            element.style.height = window.innerHeight + "px"
          }
      }

      def next (nextImages: js.Array[String]): Unit = {
          nextImages.headOption match {
            case Some(image) =>
              if (activeSlide == 0) activeSlide = 1
              else activeSlide = 0
              val image0 =
                if (activeSlide == 0) image
                else nextImages.tail.headOption match {
                  case Some(nextImage) =>
                    nextImage
                  case _ =>
                    images.head
                }

              val image1 =
                if (activeSlide == 1) image
                else nextImages.tail.headOption match {
                  case Some(nextImage) =>
                    nextImage
                  case _ =>
                    images.head
                }

              imageContainer.src = image0
              imageContainer1.src = image1

              if (activeSlide == 0) {
                changeImage(imageContainer, slide, slide1)
              } else {
                changeImage(imageContainer1, slide1, slide)
              }
              if (play) {
                timeout(fn = () => {
                  next(nextImages.tail)
                },
                  delay = 10000 + Math.floor((Math.random() * 1000) + 1).toInt,
                  invokeApply = false
                )
              }

            case _ =>
              next(images)
          }
      }

      timeout (fn = () => {
        setNewHeight(element.clientWidth * 0.4)
        images = element.getAttribute("images").replaceAll("\"", "").replace("[", "").replace("]", "").split(",").toSeq.toJSArray
        if (images.length > 1) {
          imageContainer.src = images.head
          imageContainer1.src = images.tail.head
          changeImage(imageContainer, slide, slide1)
          timeout(fn = () => {
            next(images.tail)
          },
            delay = 10000 + Math.floor((Math.random() * 1500) + 50).toInt,
            invokeApply = false
          )
        } else {
          imageContainer.src = images.head
          changeImage(imageContainer, slide, js.undefined)
        }
      }, 50, false)


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
        size.width = element.clientWidth
        size.height =  element.clientHeight
        size
      }

      def changeImage (image: Image, container: Html, oldContainer: UndefOr[Html]): Unit = {

        val callback: Function = (result: CropResult) => {
          val crop = result.topCrop
          val canvas = container.getElementsByTagName("canvas").item(0).asInstanceOf[Canvas]
          val ctx = canvas.getContext("2d")
          canvas.width = element.clientWidth
          canvas.height = element.clientHeight
          if (image.complete) {
            ctx.drawImage(image, crop.x, crop.y, crop.width, crop.height, 0, 0, canvas.width, canvas.height)
            if (oldContainer.isDefined) {
              oldContainer.asInstanceOf[Html].classList.add("ng-hide-add")
              container.asInstanceOf[Html].classList.add("ng-hide-remove")
              container.asInstanceOf[Html].classList.remove("ng-hide")
              timeout( () => {
                oldContainer.asInstanceOf[Html].classList.add("ng-hide")
                oldContainer.asInstanceOf[Html].classList.remove("ng-hide-add")
                container.asInstanceOf[Html].classList.remove("ng-hide-remove")
              }, 50, false )
            }
          }
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
        cropImage
      }

      val changeHeight = (event: Event) => {
        setNewHeight(element.clientWidth * 0.4)
        if (images.length > 1) {
          imageContainer.src = images.head
          imageContainer1.src = images.tail.head
          changeImage(imageContainer, slide, slide1)
        } else {
          imageContainer.src = images.head
          changeImage(imageContainer, slide, js.undefined)
        }
      }

      window.addEventListener("resize", changeHeight)
      scope.$on("$destroy", () => {
        play = false
        window.removeEventListener("resize", changeHeight)
      })
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

      val changeHeight = (event: Event) =>
        setNewHeight(window.innerWidth * 4)
      window.addEventListener("resize", changeHeight)

      scope.$on("$destroy", () => {
        window.removeEventListener("resize", changeHeight)
      })
    }
  }
}
