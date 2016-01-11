package Slider

import java.awt.Container

import com.greencatsoft.angularjs.core.{Timeout, Window}
import com.greencatsoft.angularjs._
import org.scalajs.dom.{Element, document, console}
import org.scalajs.dom.html._
import org.scalajs.dom.raw.{Event, UIEvent}
import smartcrop.{SmartCropService, CropResult, CropSize}

import scala.scalajs.js
import scala.scalajs.js.timers._
import scala.scalajs.js.{UndefOr, Any, Function, Object}
import scala.scalajs.js.annotation.JSExport
import scala.scalajs.js.JSConverters.JSRichGenTraversableOnce


@JSExport
case class ImageToDraw(image: String, cropX: Double, cropY: Double, cropWidth: Double, cropHeight: Double,
            any: Int, any2: Int, canvasWidth: Double, canvasHeight: Double)


@JSExport
@injectable("slider")
class SliderDirective(window: Window, timeout: Timeout, smartCropService: SmartCropService) extends ElementDirective
  with TemplatedDirective with IsolatedScope {
  override val templateUrl = "assets/templates/Slider/slider.html"

  def setNewHeight(newHeight: Double, element: Html): Unit = {
    if (newHeight < window.innerHeight) {
      element.style.height = newHeight + "px"
    } else {
      element.style.height = window.innerHeight + "px"
    }
  }

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

  def defineSize(element: Html): CropSize = {
    val size = new Object().asInstanceOf[CropSize]
    size.width = element.clientWidth
    size.height =  element.clientHeight
    size
  }

  def drawImage(imageToDraw: ImageToDraw, imageContainer: Image,  canvas: Canvas, element: Html): Unit = {
    val ctx = canvas.getContext("2d")
    canvas.width = element.clientWidth
    canvas.height = element.clientHeight
    ctx.drawImage(imageContainer, imageToDraw.cropX, imageToDraw.cropY, imageToDraw.cropWidth, imageToDraw.cropHeight,
      0, 0, canvas.width, canvas.height)
  }

  override def link(scope: ScopeType, elements: Seq[Element], attrs: Attributes): Unit = {
    elements.headOption.map(_.asInstanceOf[Html]) foreach { element =>

      var images: js.Array[String] = element.getAttribute("images").replace("[", "").replace("]", "").split(",").toSeq.toJSArray
      val imageContainer = element.getElementsByTagName("img").item(0).asInstanceOf[Image]
      val imageContainer1 = element.getElementsByTagName("img").item(1).asInstanceOf[Image]
      val slide = element.getElementsByTagName("li").item(0).asInstanceOf[Html]
      val slide1 = element.getElementsByTagName("li").item(1).asInstanceOf[Html]
      var activeSlide = 0
      var play = false
      var imagesToDraw: Seq[ImageToDraw] = Seq.empty[ImageToDraw]
      var index = 0
      var imagesLength = images.length

      def insertImages(image: String, image1: String): Unit = {
        imageContainer.src = image
        imageContainer1.src = image1
      }

      def next (): Unit = {
        index = index + 1
        if (activeSlide == 0) activeSlide = 1
        else activeSlide = 0
        if (index >= imagesLength) {
          index = 0
        }
        console.log(index)
        val image =
          if (activeSlide == 0) images(index)
          else if (index < imagesLength - 2) images(index + 1)
          else images(0)
        val image1 =
          if (activeSlide == 1) images(index)
          else if (index < imagesLength - 2) images(index + 1)
          else images(0)

        insertImages(image, image1)

        if (activeSlide == 0) {
          if(imagesToDraw.exists(_.image == imageContainer.src )) {
            drawImage(imagesToDraw.filter(_.image == imageContainer.src).head, imageContainer,
              slide.getElementsByTagName("canvas").item(0).asInstanceOf[Canvas], element)
            hideOldContainer(slide1: Html, slide: Html)
          } else {
            changeImage(imageContainer, slide, slide1)
          }
        } else {
          if(imagesToDraw.exists(_.image == imageContainer1.src)) {
            drawImage(imagesToDraw.filter(_.image == imageContainer1.src).head, imageContainer1,
              slide1.getElementsByTagName("canvas").item(0).asInstanceOf[Canvas], element)
            hideOldContainer(slide: Html, slide1: Html)
          } else {
            changeImage(imageContainer1, slide1, slide)
          }
        }
        if (play) {
          timeout(fn = () => {
            next()
          },
            delay = 10000 + Math.floor((Math.random() * 1000) + 1).toInt,
            invokeApply = false
          )
        }
      }

      timeout (fn = () => {
        setNewHeight(element.clientWidth * 0.4, element)
        images = element.getAttribute("images").replaceAll("\"", "").replace("[", "").replace("]", "").split(",").toSeq.toJSArray
        imagesLength = images.length
        if (images.length > 1) {
          console.log(images)
          imageContainer.src = images(0)
          imageContainer1.src = images(1)
          changeImage(imageContainer, slide, slide1)
          if (!play) {
            play = true
            timeout(fn = () => {
              next()
            },
              delay = 10000 + Math.floor((Math.random() * 1500) + 50).toInt,
              invokeApply = false
            )
          }
        } else {
          imageContainer.src = images.head
          changeImage(imageContainer, slide, js.undefined)
        }
      }, 50, false)



      def hideOldContainer(oldContainer: Html, container: Html): Unit = {
        oldContainer.asInstanceOf[Html].classList.add("ng-hide-add")
        container.asInstanceOf[Html].classList.add("ng-hide-remove")
        container.asInstanceOf[Html].classList.remove("ng-hide")
        timeout( () => {
          oldContainer.asInstanceOf[Html].classList.add("ng-hide")
          oldContainer.asInstanceOf[Html].classList.remove("ng-hide-add")
          container.asInstanceOf[Html].classList.remove("ng-hide-remove")
        }, 900, false )
      }


      def changeImage (image: Image, container: Html, oldContainer: UndefOr[Html]): Unit = {

        val callback: Function = (result: CropResult) => {
          val crop = result.topCrop
          val canvas = container.getElementsByTagName("canvas").item(0).asInstanceOf[Canvas]
          if (image.complete) {
            val a = ImageToDraw(image = image.src, cropX = crop.x, cropY = crop.y, cropWidth = crop.width,
              cropHeight = crop.height, any = 0, any2 = 0, canvasWidth = canvas.width, canvasHeight = canvas.height)
            imagesToDraw = imagesToDraw ++ Seq(a)
            drawImage(a, image, canvas, element)
            if (oldContainer.isDefined) {
              hideOldContainer(oldContainer.asInstanceOf[Html], container)
            }
          }
        }
        def cropImage: Unit = {
          if (image.complete) {
            smartCropService.crop(image, defineSize(element), callback)
          } else {
            timeout( fn = () => {
              cropImage
            }, 10)
          }
        }
        cropImage
      }



      def doChange(): Unit = {
        imagesToDraw = Seq.empty[ImageToDraw]
        setNewHeight(element.clientWidth * 0.4, element)
        if (images.length > 1) {
          imageContainer.src = images.head
          imageContainer1.src = images.tail.head
          changeImage(imageContainer, slide, slide1)
          if (!play) {
            play = true
            timeout(fn = () => {
              next()
            },
              delay = 10000 + Math.floor((Math.random() * 1500) + 50).toInt,
              invokeApply = false
            )
          }
        } else {
          imageContainer.src = images.head
          changeImage(imageContainer, slide, js.undefined)
        }
      }

      var timer: SetTimeoutHandle = setTimeout(30)(doChange())
      val changeHeight = (event: Event) => {
        clearTimeout(timer)
        timer = setTimeout(50)(doChange())
      }
      attrs.$observe("images", (newImages: String) => {
        images = newImages.replaceAll("\"", "").replace("[", "").replace("]", "").split(",").toSeq.toJSArray
        imagesLength = images.length
        doChange()
      })

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
