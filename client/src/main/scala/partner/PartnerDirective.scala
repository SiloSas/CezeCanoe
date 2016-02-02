package partner

import com.greencatsoft.angularjs.{ElementDirective, TemplatedDirective, injectable}

import scala.scalajs.js.annotation.JSExport

@JSExport
@injectable("partner")
class PartnerDirective() extends ElementDirective with TemplatedDirective {
  override val templateUrl = "assets/templates/partner/partner.html"
}
