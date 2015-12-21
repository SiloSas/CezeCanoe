import com.greencatsoft.angularjs.core.{Route, RouteProvider}
import com.greencatsoft.angularjs.{inject, Config}


object RoutingConfig extends Config {

  @inject
  var routeProvider: RouteProvider = _

  override def initialize() {

    routeProvider
      .when(path = "/",
        route = Route(templateUrl = "/assets/templates/main.html", title = "Main"))
      .when(
        path = "/descentes",
        route = Route(
          templateUrl = "/assets/templates/Descentes/descentes.html",
          controller = "descenteController",
          title = "Room"))

      .when(
        path = "/booking/:id",
        route = Route(
          templateUrl = "/assets/templates/Booking/booking.html",
          controller = "bookingController",
          title = "Room"))

  }
}
