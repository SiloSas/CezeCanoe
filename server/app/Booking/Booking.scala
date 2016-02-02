package BookingBack

case class BookingForm(id: String, descentId: String, bookingFormClient: BookingFormClient, details: Seq[BookingDetail], isGroup: Boolean)

case class BookingDetail(priceId: String, number: Int)

case class BookingFormClient(startTime: String, date: String, name: String, firstName: String, address: String, phoneNumber: String, email: String, isGroup: Boolean)