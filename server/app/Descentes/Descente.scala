package Descentes


case class Descente(id: String, name: Seq[VersionedString], presentation: Seq[VersionedString], tour: Seq[VersionedString],
                    images: Seq[String], distance: Seq[VersionedString], time: Seq[VersionedString])

case class Price(id: String, name: Seq[VersionedString], price: Double, isBookable: Boolean, medias: Seq[String], isSupplement: Boolean)

case class DescentePriceRelation(descenteId: String, priceId: String)

case class DescenteWithPrice(id: String, name: Seq[VersionedString], presentation: Seq[VersionedString], tour: Seq[VersionedString],
                             images: Seq[String], distance: Seq[VersionedString], prices: Seq[Price], time: Seq[VersionedString])

case class Information(id: String, information: Seq[VersionedString])

case class VersionedString(lang: String, presentation: String)

case class ArticleForBack(id: String, content: Seq[VersionedString], media: String, yellowThing: Seq[VersionedString])