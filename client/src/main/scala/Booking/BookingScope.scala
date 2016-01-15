package Booking

import com.greencatsoft.angularjs.core.Scope
import DescentsClient.{Price, Descente}

import scala.scalajs.js
import scala.scalajs.js.annotation.{JSExportAll, JSExport}

trait BookingScope extends Scope {
  var descente: Descente = js.native
  var total: Double = js.native
  var computeTotalPrice: js.Function = js.native
  var tariffs: js.Array[Price] = js.native
  var bookingForm: BookingForm = js.native
}

@JSExportAll
case class BookingFormBack(id: String, descentId: String, bookingFormClient: BookingFormClient, details: Seq[BookingDetail])

@JSExportAll
case class BookingForm(id: String, descentId: String, bookingFormClient: BookingFormClient, details: js.Array[BookingDetail])

@JSExportAll
case class BookingDetail (priceId: String, number: Int)


@JSExportAll
case class BookingFormClient(startTime: String, date: String, name: String, firstName: String, address: String, phoneNumber: String, email: String)