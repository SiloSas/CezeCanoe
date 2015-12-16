package Slider

import com.greencatsoft.angularjs.core.{Location, RouteParams, Timeout}
import com.greencatsoft.angularjs.{AbstractController, injectable}
import org.scalajs.dom.console
import scala.scalajs.js
import scala.scalajs.js.Array
import scala.scalajs.js.JSConverters.JSRichGenTraversableOnce
import scala.concurrent.ExecutionContext.Implicits.global

import scala.scalajs.js.annotation.{JSExport, JSExportAll}

@JSExportAll
case class ActiveImage(step: Int, url: String, url1: String)
@injectable("sliderController")
class SliderController(sliderScope: SliderScope, timeout: Timeout, location: Location, $routeParams: RouteParams)
  extends AbstractController[SliderScope](sliderScope) {

  /*val img = "assets/images/img1.jpg"
  val img1 = "assets/images/img2.jpg"
  val img2 = "assets/images/img3.jpg"
  val images = Seq(img, img1, img2)
  sliderScope.images = images.toJSArray*/
  @JSExport
  def makeScope(images: Array[String]): Boolean = {
    console.log(images)
    sliderScope.images = images
    startSlider
    true
  }


  def startSlider: Any = {
    timeout(fn = () => {
      sliderScope.activeImage = ActiveImage(step = 0, url = sliderScope.images.head, url1 = sliderScope.images.head)
    }, 0, true)
    if (sliderScope.images.length > 1) {
      timeout(fn = () => {
        changeActiveImage(sliderScope.images.tail)
      },
        delay = 10000,
        invokeApply = true
      )
    } else {
      println("one image")
    }
  }

  def changeActiveImage(images: Seq[String]): Any = {
    images.headOption match {
      case Some(image) =>
        val step =
          if (sliderScope.activeImage.step == 0) 1
          else 0

        val image0 =
          if (step == 0) image
          else images.tail.headOption match {
            case Some(nextImage) =>
              nextImage
            case _ =>
              sliderScope.images.head
          }

        val image1 =
          if (step == 1) image
          else images.tail.headOption match {
            case Some(nextImage) =>
              nextImage
            case _ =>
              sliderScope.images.head
          }
        timeout(fn = () => {
          if (step == 0) sliderScope.activeImage = sliderScope.activeImage.copy(url = image0)
          else sliderScope.activeImage = sliderScope.activeImage.copy(url1 = image1)
          timeout(fn = () => {
            sliderScope.activeImage = sliderScope.activeImage.copy(step = step)
            timeout(fn = () => {
              sliderScope.activeImage = sliderScope.activeImage.copy(step = step, url = image0, url1 = image1)
            }, 10)
          }, 20)
        }, 0, true)

        timeout(fn = () => {
          changeActiveImage(images.tail)
        },
          delay = 10000,
          invokeApply = true
        )
      case _ =>
        changeActiveImage(sliderScope.images)
    }
  }




  /*def findAll(): Any = {
    roomService.findAll() onComplete {
      case Success(rooms) =>
        println(rooms)
        scope.$apply {
          sliderScope.images = rooms.map(_.images).toJSArray
          scope.activeImage = ActiveImage(step = 0, url = scope.images.head, url1 = scope.images.head)
          if(sliderScope.images.length > 1) {
            timeout(fn = () => {
              changeActiveImage(scope.images.tail)
            },
              delay = 10000,
              invokeApply = true
            )
          } else {
            println("one image")
          }
        }
      case Failure(t) => handleError(t)
    }
  }
*/

  private def handleError(t: Throwable) {
    console.error(s"An error has occured: $t")
  }
}