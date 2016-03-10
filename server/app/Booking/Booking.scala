package BookingBack

case class BookingForm(id: String, descentId: String, bookingFormClient: BookingFormClient, details: Seq[BookingDetail], isGroup: Boolean)

case class BookingDetail(priceId: String, number: Int)

case class BookingFormClient(startTime: String, date: String, name: String, firstName: String, address: String, phoneNumber: String, email: String, isGroup: Boolean)

case class CreditCard(number: String, card_type: String, expire_month: Int, expire_year: Int, cvv2: Int, first_name: String, last_name: String)

case class Address(line1: String, city: String, state: String, postal_code: String, country_code: String)

case class BookingFormWithCreditCard(bookingForm: BookingForm, creditCard: CreditCard)
