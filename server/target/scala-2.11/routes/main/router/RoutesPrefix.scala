
// @GENERATOR:play-routes-compiler
// @SOURCE:/home/loann/Desktop/CezeCanoe/server/conf/routes
// @DATE:Tue Dec 22 15:06:39 CET 2015


package router {
  object RoutesPrefix {
    private var _prefix: String = "/"
    def setPrefix(p: String): Unit = {
      _prefix = p
    }
    def prefix: String = _prefix
    val byNamePrefix: Function0[String] = { () => prefix }
  }
}
