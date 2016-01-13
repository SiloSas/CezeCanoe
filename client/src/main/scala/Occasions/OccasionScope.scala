package Occasions

import ArticleWithSlider.ArticleWithSlider
import com.greencatsoft.angularjs.core.Scope

import scala.scalajs.js

trait OccasionScope extends Scope {
  var occasions: js.Array[ArticleWithSlider] = js.native
}