package Descentes

import com.greencatsoft.angularjs.core.Scope
import shared.Descente

import scala.scalajs.js

trait DescenteScope extends Scope {

  var descentes: js.Array[Descente] = js.native

}