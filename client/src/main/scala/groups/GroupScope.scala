package groups

import ArticleWithSlider.ArticleWithSlider
import com.greencatsoft.angularjs.core.Scope

import scala.scalajs.js

trait GroupScope extends Scope {
  var groups: js.Array[ArticleWithSlider] = js.native
}