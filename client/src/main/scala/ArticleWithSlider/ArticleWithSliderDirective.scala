package ArticleWithSlider

import com.greencatsoft.angularjs.{ElementDirective, TemplatedDirective, injectable}

import scala.scalajs.js.annotation.JSExport

@JSExport
@injectable("articleWithSlider")
class ArticleWithSliderDirective() extends ElementDirective with TemplatedDirective {
  override val templateUrl = "assets/templates/ArticleWithSlider/articleWithSlider.html"
}
