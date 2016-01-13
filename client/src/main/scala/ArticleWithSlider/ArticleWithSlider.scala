package ArticleWithSlider

import Admin.{VersionedString, VersionedStringToBindScope}

import scala.scalajs.js
import scala.scalajs.js.annotation.JSExportAll

@JSExportAll
case class ArticleWithSlider(id: String, content: js.Array[VersionedStringToBindScope], images: js.Array[String])
@JSExportAll
case class ArticleWithSliderForBack(id: String, content: Seq[VersionedString], images: Seq[String])