import Descentes.{DescenteController, DescenteDirective}
import Home.{TopBarContentDirective, TopBarDirective, HomeController, HomeArticleDirective}
import Room.{RoomController, RoomMinDirective}
import Slider.{SliderContentDirective, SliderDirective, SliderController}
import VideoPlayer.VideoPlayerDirective
import com.greencatsoft.angularjs._
import example.RoomServiceFactory
import smartcrop.SmartCropDirective
import scala.scalajs.js.JSApp
import scala.scalajs.js.annotation.JSExport

@JSExport
object App extends JSApp {

  override def main() {
    val module = Angular.module("app", Seq("ngAnimate", "ngAria", "ngMaterial", "mm.foundation", "ngRoute", "ngMap", "smartCrop"))

    module
    .factory[RoomServiceFactory]
    .controller[RoomController]
    .controller[SliderController]
    .controller[HomeController]
    .controller[DescenteController]
    .directive[RoomMinDirective]
      .directive[SmartCropDirective]
      .directive[SliderDirective]
      .directive[SliderContentDirective]
      .directive[HomeArticleDirective]
      .directive[VideoPlayerDirective]
      .directive[TopBarDirective]
      .directive[TopBarContentDirective]
      .directive[DescenteDirective]
    .config(RoutingConfig)
  }
}