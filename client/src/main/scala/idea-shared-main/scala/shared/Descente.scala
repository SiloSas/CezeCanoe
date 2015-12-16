package shared

import scala.scalajs.js
import scala.scalajs.js.annotation.JSExportAll

@JSExportAll
case class Descente(id: String, name: String, presentation: String, tour: String, images: js.Array[String], distance: String, prices: js.Array[Price], time: String)

case class Price(name: String, price: Double)