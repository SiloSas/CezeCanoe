package Admin

import com.greencatsoft.angularjs.core.Scope
import shared.Descente

import scala.scalajs.js

trait AdminScope extends Scope {
  var formTemplate: String = js.native
  var descente : DescenteMutable = js.native
  var validate: js.Function = js.native
  var setDescente: js.Function = js.native
  var setNewDescente: js.Function = js.native
  var descentes: js.Array[DescenteMutable] = js.native
  var prices: js.Array[Price] = js.native
  var newImage: String = js.native
}

trait DescenteMutable extends js.Object {
  var id: String = js.native
  var name: js.Array[VersionedString] = js.native
  var presentations: js.Array[VersionedString] = js.native
  var tour: js.Array[VersionedString] = js.native
  var images: js.Array[String] = js.native
  var distance: js.Array[VersionedString] = js.native
  var prices: js.Array[Price] = js.native
  var time: js.Array[VersionedString] = js.native
}

trait Price extends js.Object {
  var name: js.Array[VersionedString] = js.native
  var price: Double = js.native
  var isBookable: Boolean = js.native
  var medias: js.Array[String] = js.native
  var isSupplement: Boolean = js.native
}

trait VersionedString extends js.Object {
  var lang: String = js.native
  var presentation: js.Any = js.native
}