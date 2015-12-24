package Slider

import com.greencatsoft.angularjs.core.Scope

import scala.scalajs.js

trait SliderScope extends Scope {
  var activeImage: ActiveImage = js.native
  var images: js.Array[String] = js.native
}