package Admin

import com.greencatsoft.angularjs.core.Scope
import shared.Descente

import scala.scalajs.js

trait AdminScope extends Scope {
  var formTemplate: String = js.native
  var descente : DescenteMutable = js.native
  var setDescente: js.Function = js.native
  var descentes: js.Array[Descente] = js.native
}

trait DescenteMutable extends js.Object {
  var id: String = js.native
  var name: String = js.native
  var presentations: js.Array[Presentation] = js.native
  var tour: String = js.native
  var images: js.Array[String] = js.native
  var distance: String = js.native
  var prices: js.Array[Price] = js.native
  var time: String = js.native
}

trait Price extends js.Object {
  var name: String = js.native
  var price: Double = js.native
  var isBookable: Boolean = js.native
  var medias: js.Array[String] = js.native
  var isSupplement: Boolean = js.native
}

trait Presentation extends js.Object {
  var lang: String = js.native
  var presentation: js.Any = js.native
}