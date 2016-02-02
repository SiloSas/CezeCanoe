package partner

import Admin.{VersionedString, VersionedStringToBindScope}

import scala.scalajs.js
import scala.scalajs.js.annotation.JSExportAll


@JSExportAll
case class Partner(id: String, content: js.Array[VersionedStringToBindScope], media: String, link: String)
@JSExportAll
case class PartnerForBack(id: String, content: Seq[VersionedString], media: String, link: String)