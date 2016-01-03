package Admin

import com.greencatsoft.angularjs.{ClassDirective, ElementDirective, TemplatedDirective, injectable}

import scala.scalajs.js.annotation.JSExport

@JSExport
@injectable("tariffForm")
class TariffFormDirective() extends ClassDirective with TemplatedDirective {
  override val templateUrl = "assets/templates/Admin/tariffForm.html"

}