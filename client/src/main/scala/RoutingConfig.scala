import com.greencatsoft.angularjs.core.{Route, RouteProvider}
import com.greencatsoft.angularjs.{inject, Config}


object RoutingConfig extends Config {

  @inject
  var routeProvider: RouteProvider = _

  override def initialize() {

    routeProvider
      .when(path = "/",
        route = Route(templateUrl = "/assets/templates/main.html", title = "Main"))
      .when(path = "/conditionGeneral",
        route = Route(templateUrl = "/assets/templates/conditionsGenerales.html", title = "Main"))
      .when(
        path = "/descentes",
        route = Route(
          templateUrl = "/assets/templates/Descentes/descentes.html",
          controller = "descenteController",
          title = "Descentes"))
      .when(
        path = "/services",
        route = Route(
          templateUrl = "/assets/templates/ServicesPage/servicePage.html",
          controller = "servicesController",
          title = "Services"))
      .when(
        path = "/occasions",
        route = Route(
          templateUrl = "/assets/templates/Occasions/occasions.html",
          controller = "occasionController",
          title = "Occasions"))
      .when(
        path = "/groups",
        route = Route(
          templateUrl = "/assets/templates/groups/groups.html",
          controller = "groupsController",
          title = "Groups"))

      .when(
        path = "/booking/:id",
        route = Route(
          templateUrl = "/assets/templates/Booking/booking.html",
          controller = "bookingController",
          title = "Booking"))
      .when(
        path = "/contact",
        route = Route(
          templateUrl = "/assets/templates/Contact/contact.html",
          controller = "contactController",
          title = "Contact"))
      .when(
        path = "/partners",
        route = Route(
          templateUrl = "/assets/templates/partner/partners.html",
          controller = "partnersController",
          title = "Partner"))

  }
}
