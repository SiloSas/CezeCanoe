package Descentes

import com.greencatsoft.angularjs.{ElementDirective, TemplatedDirective, injectable}

import scala.scalajs.js.annotation.JSExport

@JSExport
@injectable("descente")
class DescenteDirective() extends ElementDirective with TemplatedDirective {
  override val templateUrl = "assets/templates/Descentes/descente.html"
}