package Descentes

import com.greencatsoft.angularjs.core.Scope
import shared.{Price, Descente}

import scala.scalajs.js

trait DescenteScope extends Scope {

  var descentes: js.Array[Descente] = js.native
  var informations: String = js.native
  var tariffs: js.Array[Price] = js.native

}