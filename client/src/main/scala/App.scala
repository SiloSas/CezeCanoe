import Admin.{TariffFormDirective, TariffsImagesDirective, AdminController}
import ArticleWithSlider.ArticleWithSliderDirective
import Booking.{BookingServiceFactory, BookingController}
import Contact.ContactController
import Descentes.{fullSliderDirective, DescenteServiceFactory, DescenteController, DescenteDirective}
import Home._
import Lang.{LangDirective, LangServiceFactory}
import Occasions.{OccasionController, OccasionServiceFactory}
import Room.{RoomController, RoomMinDirective}
import ServicesPage.{ServicesServiceFactory, ServicesController, ServicesService}
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
    val module = Angular.module("app", Seq("ngAnimate", "ngAria", "ngMaterial", "mm.foundation", "ngRoute", "ngMap",
      "smartCrop", "uploader", "textAngular"))

    module
    .factory[RoomServiceFactory]
    .factory[DescenteServiceFactory]
    .factory[LangServiceFactory]
    .factory[HomeServiceFactory]
    .factory[BookingServiceFactory]
    .factory[ServicesServiceFactory]
    .factory[OccasionServiceFactory]
    .controller[RoomController]
    .controller[HomeController]
    .controller[DescenteController]
    .controller[BookingController]
    .controller[AdminController]
    .controller[MainController]
    .controller[ServicesController]
    .controller[OccasionController]
    .controller[ContactController]
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
      .directive[ArticleWithSliderDirective]
    .config(RoutingConfig)
  }
}