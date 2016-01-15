package DescentsClient

import Admin.{VersionedStringToBindScope, VersionedStringScope, VersionedStringToBind, VersionedString}

import scala.scalajs.js
import scala.scalajs.js.annotation.JSExportAll

@JSExportAll
case class Descente(id: String, name: js.Array[VersionedStringScope], presentation: js.Array[VersionedStringToBindScope], tour: js.Array[VersionedStringScope],
                    images: js.Array[String], distance: js.Array[VersionedStringScope], prices: js.Array[Price], time: js.Array[VersionedStringScope])

@JSExportAll
case class DescenteForBack(id: String, name: Seq[VersionedString], presentation: Seq[VersionedString], tour: Seq[VersionedString],
                           images: Seq[String], distance: Seq[VersionedString], prices: Seq[PriceForBack], time: Seq[VersionedString])

@JSExportAll
case class Price(id: String, name: js.Array[VersionedStringScope], price: Double, isBookable: Boolean, medias: js.Array[String], isSupplement: Boolean)

@JSExportAll
case class PriceForBack(id: String, name: Seq[VersionedString], price: Double, isBookable: Boolean, medias: Seq[String], isSupplement: Boolean)

@JSExportAll
case class  Information(id: String, information: js.Array[VersionedStringToBindScope])

@JSExportAll
case class  InformationForBack(id: String, information: Seq[VersionedString])
