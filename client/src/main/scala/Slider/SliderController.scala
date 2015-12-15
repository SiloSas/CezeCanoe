package Slider

import com.greencatsoft.angularjs.core.{Location, RouteParams, Timeout}
import com.greencatsoft.angularjs.{AbstractController, injectable}
import org.scalajs.dom.console
import scala.scalajs.js.JSConverters.JSRichGenTraversableOnce
import scala.concurrent.ExecutionContext.Implicits.global

import scala.scalajs.js.annotation.JSExportAll

@JSExportAll
case class ActiveImage(step: Int, url: String, url1: String)
@injectable("sliderController")
class SliderController(sliderScope: SliderScope, timeout: Timeout, location: Location, $routeParams: RouteParams)
  extends AbstractController[SliderScope](sliderScope) {

  val img = "assets/images/img1.jpg"
  val img1 = "assets/images/img2.jpg"
  val img2 = "assets/images/img3.jpg"
  val images = Seq(img, img1, img2)
  sliderScope.images = images.toJSArray
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

  def changeActiveImage(images: Seq[String]): Any = {
    images.headOption match {
      case Some(image) =>
        val step =
          if (scope.activeImage.step == 0) 1
          else 0

        val image0 =
          if (step == 0) image
          else images.tail.headOption match {
            case Some(nextImage) =>
              nextImage
            case _ =>
              scope.images.head
          }

        val image1 =
          if (step == 1) image
          else images.tail.headOption match {
            case Some(nextImage) =>
              nextImage
            case _ =>
              scope.images.head
          }
        timeout(fn = () => {
          if (step == 0) scope.activeImage = scope.activeImage.copy(url = image0)
          else scope.activeImage = scope.activeImage.copy(url1 = image1)
          timeout(fn = () => {
            scope.activeImage = scope.activeImage.copy(step = step)
            timeout(fn = () => {
              scope.activeImage = scope.activeImage.copy(step = step, url = image0, url1 = image1)
            }, 20)
          }, 20)
        }, 0, true)
        console.log(image0, image1)

        timeout(fn = () => {
          changeActiveImage(images.tail)
        },
          delay = 10000,
          invokeApply = true
        )
      case _ =>
        changeActiveImage(scope.images)
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