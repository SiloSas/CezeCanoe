package contact

import com.greencatsoft.angularjs.extensions.{ModalOptions, ModalService}
import com.greencatsoft.angularjs.{ClassDirective, TemplatedDirective, injectable}

import scala.scalajs.js
import scala.scalajs.js.Object
import scala.scalajs.js.annotation.JSExport

@JSExport
@injectable("contact")
class ContactDirective(modalService: ModalService) extends ClassDirective {
  @JSExport
  def openContact(): Unit = {
    val options = new Object().asInstanceOf[ModalOptions]
    options.templateUrl = "assets/templates/Contact/contactForm.html"
    options.controller = "contactModalController"
    options.windowClass = "contactModal"
    modalService.open(options)
  }
}
