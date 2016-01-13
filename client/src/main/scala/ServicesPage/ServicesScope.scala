package ServicesPage

import ArticleWithSlider.ArticleWithSlider
import com.greencatsoft.angularjs.core.Scope

import scala.scalajs.js

trait ServicesScope extends Scope {
  var articles: js.Array[ArticleWithSlider] = js.native
}