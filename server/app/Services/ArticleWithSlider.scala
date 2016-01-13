package Services

import Descentes.VersionedString

case class ArticleWithSlider(id: String, content: Seq[VersionedString], images: Seq[String])