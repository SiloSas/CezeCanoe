package Home

import com.greencatsoft.angularjs.{ElementDirective, TemplatedDirective, injectable}

import scala.scalajs.js.annotation.JSExport

@JSExport
@injectable("homeArticle")
class HomeArticleDirective() extends ElementDirective with TemplatedDirective {
  override val templateUrl = "assets/templates/Home/article.html"
}
