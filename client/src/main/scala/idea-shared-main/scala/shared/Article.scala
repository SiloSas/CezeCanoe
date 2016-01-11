package shared

import Admin.{VersionedStringScope, VersionedStringToBind, VersionedString, VersionedStringToBindScope}

import scala.scalajs.js
import scala.scalajs.js.annotation.{JSExportAll, JSExport}

@JSExportAll
case class Article(id: String, content: js.Array[VersionedStringToBindScope], media: String, yellowThing: js.Array[VersionedStringScope])
@JSExportAll
case class ArticleForBack(id: String, content: Seq[VersionedString], media: String, yellowThing: Seq[VersionedString])