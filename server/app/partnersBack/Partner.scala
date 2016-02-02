package partnersBack

import Descentes.VersionedString

case class Partner(id: String, content: Seq[VersionedString], media: String, link: String)