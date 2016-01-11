import Admin.{TariffFormDirective, TariffsImagesDirective, AdminController}
import Booking.BookingController
import Descentes.{fullSliderDirective, DescenteServiceFactory, DescenteController, DescenteDirective}
import Home._
import Lang.{LangDirective, LangServiceFactory}
import Room.{RoomController, RoomMinDirective}
import Size.SizeDirective
import Slider.{SliderContentDirective, SliderDirective}
import VideoPlayer.VideoPlayerDirective
import com.greencatsoft.angularjs._
import example.RoomServiceFactory
import smartcrop.SmartCropDirective
import scala.scalajs.js.JSApp
import scala.scalajs.js.annotation.JSExport

@JSExport
object App extends JSApp {

  override def main() {
    val module = Angular.module("app", Seq("ngAnimate", "ngAria", "ngMaterial", "mm.foundation", "ngRoute", "ngMap", "smartCrop", "uploader"))

    module
    .factory[RoomServiceFactory]
    .factory[DescenteServiceFactory]
    .factory[LangServiceFactory]
    .factory[HomeServiceFactory]
    .controller[RoomController]
    .controller[HomeController]
    .controller[DescenteController]
    .controller[BookingController]
    .controller[AdminController]
    .controller[MainController]
    .directive[RoomMinDirective]
      .directive[SmartCropDirective]
      .directive[SliderDirective]
      .directive[SliderContentDirective]
      .directive[HomeArticleDirective]
      .directive[VideoPlayerDirective]
      .directive[TopBarDirective]
      .directive[TopBarContentDirective]
      .directive[DescenteDirective]
      .directive[LangDirective]
      .directive[TariffsImagesDirective]
      .directive[TariffFormDirective]
      .directive[SizeDirective]
      .directive[fullSliderDirective]
    .config(RoutingConfig)
  }
}