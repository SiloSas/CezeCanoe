package shared

import scala.scalajs.js
import scala.scalajs.js.annotation.JSExportAll

@JSExportAll
case class Descente(id: String, name: String, presentation: js.Any, tour: String, images: js.Array[String], distance: String, prices: js.Array[Price], time: String)

@JSExportAll
case class Price(name: String, price: Double, isBookable: Boolean, medias: js.Array[String], isSupplement: Boolean)
