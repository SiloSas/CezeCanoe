import Admin.{heightDirective, AdminController, TariffFormDirective, TariffsImagesDirective}
import ArticleWithSlider.ArticleWithSliderDirective
import Booking.{BookingController, BookingServiceFactory}
import Contact.{ContactController, ContactModalController}
import Descentes.{DescenteController, DescenteDirective, DescenteServiceFactory, fullSliderDirective}
import Home._
import Lang.{LangDirective, LangServiceFactory}
import Occasions.{OccasionController, OccasionServiceFactory}
import Room.{RoomController, RoomMinDirective}
import ServicesPage.{ServicesController, ServicesServiceFactory}
import Size.SizeDirective
import Slider.{SliderContentDirective, SliderDirective}
import VideoPlayer.VideoPlayerDirective
import com.greencatsoft.angularjs._
import contact.ContactDirective
import example.RoomServiceFactory
import partner.{PartnerDirective, PartnersController, PartnersServiceFactory}
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
    .factory[PartnersServiceFactory]
    .controller[RoomController]
    .controller[HomeController]
    .controller[DescenteController]
    .controller[BookingController]
    .controller[AdminController]
    .controller[MainController]
    .controller[ServicesController]
    .controller[OccasionController]
    .controller[ContactController]
    .controller[ContactModalController]
    .controller[PartnersController]
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
      .directive[heightDirective]
      .directive[TariffsImagesDirective]
      .directive[TariffFormDirective]
      .directive[SizeDirective]
      .directive[fullSliderDirective]
      .directive[ArticleWithSliderDirective]
      .directive[PartnerDirective]
      .directive[ContactDirective]
    .config(RoutingConfig)
  }
}