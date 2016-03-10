package Booking

import DescentsClient.{Descente, Price}
import com.greencatsoft.angularjs.core.Scope

import scala.scalajs.js
import scala.scalajs.js.annotation.JSExportAll

trait BookingScope extends Scope {
  var descente: Descente = js.native
  var total: Double = js.native
  var computeTotalPrice: js.Function = js.native
  var tariffs: js.Array[Price] = js.native
  var bookingForm: BookingForm = js.native
}

@JSExportAll
case class BookingFormBack(id: String, descentId: String, bookingFormClient: BookingFormClient, details: Seq[BookingDetail], isGroup: Boolean)

@JSExportAll
case class BookingForm(id: String, descentId: String, bookingFormClient: BookingFormClient, details: js.Array[BookingDetail], isGroup: Boolean)

@JSExportAll
case class BookingDetail (priceId: String, number: Int)


@JSExportAll
case class BookingFormClient(startTime: String, date: String, name: String, firstName: String, address: String, phoneNumber: String, email: String, isGroup: Boolean)


case class CreditCard(number: String, card_type: String, expire_month: Int, expire_year: Int, cvv2: Int, first_name: String, last_name: String)

case class Address(line1: String, city: String, state: String, postal_code: String, country_code: String)

case class BookingFormWithCreditCard(bookingForm: BookingFormBack, creditCard: CreditCard)