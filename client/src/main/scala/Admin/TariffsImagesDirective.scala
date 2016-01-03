package Admin

import com.greencatsoft.angularjs.{Attributes, TemplatedDirective, ElementDirective, injectable}
import org.scalajs.dom.raw.Element

import org.scalajs.dom.console
import scala.scalajs.js
import scala.scalajs.js.UndefOr
import scala.scalajs.js.annotation.JSExport
import upickle.default._
import scala.scalajs.js.JSConverters.JSRichGenTraversableOnce

@JSExport
@injectable("tariffsImages")
class TariffsImagesDirective() extends ElementDirective with TemplatedDirective {
  override val templateUrl = "assets/templates/Admin/tariffsImages.html"

  @JSExport
  var medias: js.Array[String] = Seq.empty[String].toJSArray

  @JSExport
  def makeMedias(newMedias: Seq[String]): Unit = {
    medias = newMedias.toJSArray
  }

  override def link(scopeType: ScopeType, elements: Seq[Element], attributes: Attributes) = {
    elements.foreach { elem =>
      attributes.$observe("medias", (newMedias: String) => {
        makeMedias(read[Seq[String]](newMedias))
      })
    }
  }
}