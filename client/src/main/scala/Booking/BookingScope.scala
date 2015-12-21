package Booking

import com.greencatsoft.angularjs.core.Scope
import shared.{Price, Descente}

import scala.scalajs.js
import scala.scalajs.js.annotation.JSExport

trait BookingScope extends Scope {
  var descente: Descente = js.native
  var total: Double = js.native
  var computeTotalPrice: js.Function = js.native
  var tariffs: js.Array[Price] = js.native
}