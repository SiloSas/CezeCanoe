package Slider

import com.greencatsoft.angularjs._
import com.greencatsoft.angularjs.core.{Timeout, Window}
import org.scalajs.dom.html._
import org.scalajs.dom.raw.Event
import org.scalajs.dom.{Element, console}
import smartcrop.{CropSize, SmartCropService}

import scala.scalajs.js
import scala.scalajs.js.JSConverters.JSRichGenTraversableOnce
import scala.scalajs.js.Object
import scala.scalajs.js.annotation.JSExport
import scala.scalajs.js.timers._


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
    else if (parent.parentNode != null) getParentHeight(parent.parentNode.asInstanceOf[Html])
    else window.innerHeight
  }

  def getParentWidth(parent: Html): Int = {
    if (parent.getBoundingClientRect().width > 0)  {
      parent.getBoundingClientRect().width.toInt
    }
    else if (parent.parentNode != null) getParentHeight(parent.parentNode.asInstanceOf[Html])
    else window.innerWidth
  }

  def defineSize(element: Html): CropSize = {
    val size = new Object().asInstanceOf[CropSize]
    size.width = element.clientWidth
    size.height =  element.clientHeight
    size
  }

  /*def drawImage(imageToDraw: ImageToDraw, imageContainer: Image,  canvas: Canvas, element: Html): Unit = {
    val ctx = canvas.getContext("2d")
    canvas.width = getParentWidth(element)
    canvas.height = getParentHeight(element)
    console.log(1)
    timeout(() => {
      ctx.drawImage(imageContainer, imageToDraw.cropX, imageToDraw.cropY, imageToDraw.cropWidth, imageToDraw.cropHeight,
        0, 0, canvas.width, canvas.height)
      console.log(2)
    },10)
  }*/

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
      var isAlreadyLaunched = false

      def insertImages(image: String, image1: String): Unit = {
        val margin1 = image.split('?').length match {
          case 2 => image.split('?')(1).toDouble
          case _ => 0.0
        }
        val margin2 = image.split('?').length match {
          case 2 => image1.split('?')(1).toDouble
          case _ => 0.0
        }

        imageContainer.src = image
        imageContainer.style.marginTop = margin1 + "%"
        imageContainer1.style.marginTop = margin2 + "%"
        imageContainer1.src = image1
      }

      def next (): Unit = {
        isAlreadyLaunched = true
        if (activeSlide == 0) activeSlide = 1
        else activeSlide = 0
        if (index >= imagesLength) {
          index = 0
        }
        val image =
          if (activeSlide == 0) images(index)
          else if (index < imagesLength - 1) images(index + 1)
          else images(0)
        val image1 =
          if (activeSlide == 1) images(index)
          else if (index < imagesLength - 1) images(index + 1)
          else images(0)

        insertImages(image, image1)

        timeout(fn = () => {
          if (activeSlide == 0) {
            //if(imagesToDraw.exists(_.image == imageContainer.src )) {
              /*drawImage(imagesToDraw.filter(_.image == imageContainer.src).head, imageContainer,
                slide.getElementsByTagName("canvas").item(0).asInstanceOf[Canvas], element)*/
                hideOldContainer(slide: Html, slide1: Html)
            //} else {
              //changeImage(imageContainer, slide, slide1)
            //}
          } else {
            //if(imagesToDraw.exists(_.image == imageContainer1.src)) {
              /*drawImage(imagesToDraw.filter(_.image == imageContainer1.src).head, imageContainer1,
                slide1.getElementsByTagName("canvas").item(0).asInstanceOf[Canvas], element)*/
              hideOldContainer(slide1: Html, slide: Html)
           /* } else {
              changeImage(imageContainer1, slide1, slide)
            }*/
          }
          if (play) {
              index = index + 1
              next()
          }
        },
          delay = 10000 + Math.floor((Math.random() * 2000) + 100).toInt,
          invokeApply = false
        )
      }

      timeout (fn = () => {
        setNewHeight(element.clientWidth * 0.4, element)
        images = element.getAttribute("images").replaceAll("\"", "").replace("[", "").replace("]", "").split(",").toSeq.toJSArray
        imagesLength = images.length
        if (images.length > 1) {
          if (!play) {
            index = 0
            play = true
            if (!isAlreadyLaunched) {
              next()
            }
          }
        } else if (imagesLength != 0) {
          imageContainer.src = images.head
          //changeImage(imageContainer, slide, js.undefined)
        }
      }, 50 + Math.floor((Math.random() * 20) + 5).toInt, false)



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


      /*def changeImage (image: Image, container: Html, oldContainer: UndefOr[Html]): Unit = {

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
            timeout( () => {
              smartCropService.crop(image, defineSize(element), callback)
            })
          } else {
            timeout( fn = () => {
              cropImage
            }, 10)
          }
        }
        cropImage
      }*/



      def doChange(): Unit = {
        console.log("change")
        imagesToDraw = Seq.empty[ImageToDraw]
        setNewHeight(element.clientWidth * 0.4, element)
        if (images.length > 1) {
          index = 0
          if (!play) {
              play = true
              if (!isAlreadyLaunched) {
                next()
              }
          }
        } else if (images.length != 0) {
          val image = images.head
          val margin = image.split('?').length match {
            case 2 => image.split('?')(1).toDouble
            case _ => 0.0
          }
          imageContainer.src = image
          imageContainer.style.marginTop = margin + "%"
          //changeImage(imageContainer, slide, js.undefined)
        }
      }

      var timer: SetTimeoutHandle = setTimeout(30)(doChange())
      val changeHeight = (event: Event) => {
        clearTimeout(timer)
        timer = setTimeout(50)(doChange())
      }
      attrs.$observe("images", (newImages: String) => {
        if (newImages.length > 2) {
          images = newImages.replaceAll("\"", "").replace("[", "").replace("]", "").split(",").toSeq.toJSArray
          imagesLength = images.length
          doChange()
        }
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
