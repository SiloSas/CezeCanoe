package Descentes



case class Descente(id: String, name: Seq[VersionedString], presentation: Seq[VersionedString], tour: Seq[VersionedString],
                    images: Seq[String], distance: Seq[VersionedString], time: Seq[VersionedString])

case class Price(id: String, name: Array[VersionedString], price: Double, isBookable: Boolean, medias: Array[String], isSupplement: Boolean)

case class DescentePriceRelation(descenteId: String, priceId: String)

case class DescenteWithPrice(id: String, name: Seq[VersionedString], presentation: Seq[VersionedString], tour: Seq[VersionedString],
                             images: Seq[String], distance: Seq[VersionedString], prices: Seq[Price], time: Seq[VersionedString])


case class VersionedString(lang: String, presentation: String)