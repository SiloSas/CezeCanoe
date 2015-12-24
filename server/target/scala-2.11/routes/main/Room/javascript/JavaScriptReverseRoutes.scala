
// @GENERATOR:play-routes-compiler
// @SOURCE:/home/loann/Desktop/CezeCanoe/server/conf/routes
// @DATE:Tue Dec 22 15:06:39 CET 2015

import play.api.routing.JavaScriptReverseRoute
import play.api.mvc.{ QueryStringBindable, PathBindable, Call, JavascriptLiteral }
import play.core.routing.{ HandlerDef, ReverseRouteContext, queryString, dynamicString }


import _root_.controllers.Assets.Asset

// @LINE:8
package Room.javascript {
  import ReverseRouteContext.empty

  // @LINE:8
  class ReverseRoomController(_prefix: => String) {

    def _defaultPrefix: String = {
      if (_prefix.endsWith("/")) "" else "/"
    }

  
    // @LINE:8
    def findAll: JavaScriptReverseRoute = JavaScriptReverseRoute(
      "Room.RoomController.findAll",
      """
        function() {
          return _wA({method:"GET", url:"""" + _prefix + { _defaultPrefix } + """" + "rooms"})
        }
      """
    )
  
  }


}