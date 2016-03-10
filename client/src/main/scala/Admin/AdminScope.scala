package Admin

import ArticleWithSlider.ArticleWithSlider
import com.greencatsoft.angularjs.core.Scope
import partner.Partner

import scala.scalajs.js
import scala.scalajs.js.annotation.JSExport


trait AdminScope extends Scope {
  var formTemplate: String = js.native
  var descente : DescenteMutable = js.native
  var validate: js.Function = js.native
  var setDescente: js.Function = js.native
  var setNewDescente: js.Function = js.native
  var descentes: js.Array[DescenteMutable] = js.native
  var prices: js.Array[Price] = js.native
  var newImage: String = js.native
  var articles: js.Array[ArticleMutable] = js.native
  var article: ArticleMutable = js.native
  var images: js.Array[String] = js.native
  var bookings: js.Any = js.native
  var services: js.Array[ArticleWithSlider] = js.native
  var service: ArticleWithSliderMutable = js.native
  var occasions: js.Array[ArticleWithSlider] = js.native
  var occasion: ArticleWithSliderMutable = js.native
  var partners: js.Array[Partner] = js.native
  var partner: PartnerMutable = js.native
}


trait ArticleMutable extends js.Object {
  var id: String = js.native
  var content: js.Array[VersionedStringToBindScope] = js.native
  var media: String = js.native
  var yellowThing: js.Array[VersionedStringScope] = js.native
}
trait PartnerMutable extends js.Object {
  var id: String = js.native
  var content: js.Array[VersionedStringToBindScope] = js.native
  var media: String = js.native
  var link: String = js.native
}
trait ArticleWithSliderMutable extends js.Object {
  var id: String = js.native
  var content: js.Array[VersionedStringToBindScope] = js.native
  var images: js.Array[String] = js.native
}

trait DescenteMutable extends js.Object {
  var id: String = js.native
  var name: js.Array[VersionedStringScope] = js.native
  var presentations: js.Array[VersionedStringScope] = js.native
  var tour: js.Array[VersionedStringScope] = js.native
  var images: js.Array[String] = js.native
  var distance: js.Array[VersionedStringScope] = js.native
  var prices: js.Array[Price] = js.native
  var time: js.Array[VersionedStringScope] = js.native
  var isVisible: Boolean = js.native
  var groupReduction: Double = js.native
}

trait Price extends js.Object {
  var id: String = js.native
  var name: js.Array[VersionedStringScope] = js.native
  var price: Double = js.native
  var isBookable: Boolean = js.native
  var medias: js.Array[String] = js.native
  var isSupplement: Boolean = js.native
}

trait VersionedStringScope extends js.Object {
  var lang: String = js.native
  var presentation: String = js.native
}

trait VersionedStringToBindScope extends js.Object {
  var lang: String = js.native
  var presentation: js.Any = js.native
}

@JSExport
case class VersionedString (
  var lang: String,
  var presentation: String
)

@JSExport
case class VersionedStringToBind (
  var lang: String,
  var presentation: js.Any
)