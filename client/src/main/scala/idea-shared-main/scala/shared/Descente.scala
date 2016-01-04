package shared

import Admin.{VersionedStringToBindScope, VersionedStringScope, VersionedStringToBind, VersionedString}

import scala.scalajs.js
import scala.scalajs.js.annotation.JSExportAll

@JSExportAll
case class Descente(id: String, name: js.Array[VersionedStringScope], presentation: js.Array[VersionedStringToBindScope], tour: js.Array[VersionedStringScope],
                    images: js.Array[String], distance: js.Array[VersionedStringScope], prices: js.Array[Price], time: js.Array[VersionedStringScope])

@JSExportAll
case class DescenteForBack(id: String, name: js.Array[VersionedString], presentation: js.Array[VersionedString], tour: js.Array[VersionedString],
                    images: js.Array[String], distance: js.Array[VersionedString], prices: js.Array[PriceForBack], time: js.Array[VersionedString])

@JSExportAll
case class Price(id: String, name: js.Array[VersionedStringScope], price: Double, isBookable: Boolean, medias: js.Array[String], isSupplement: Boolean)

@JSExportAll
case class PriceForBack(id: String, name: js.Array[VersionedString], price: Double, isBookable: Boolean, medias: js.Array[String], isSupplement: Boolean)
