import Room.{RoomController, RoomMinDirective}
import Slider.{SliderContentDirective, SliderDirective, SliderController}
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
    .directive[RoomMinDirective]
      .directive[SmartCropDirective]
      .directive[SliderDirective]
      .directive[SliderContentDirective]
    .config(RoutingConfig)
  }
}