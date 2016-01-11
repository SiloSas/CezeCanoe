package Home

import com.greencatsoft.angularjs.core.Scope
import shared.Article

import scala.scalajs.js

trait HomeScope extends Scope {
  var articles: js.Array[Article] = js.native
  var images: js.Array[String] = js.native
}