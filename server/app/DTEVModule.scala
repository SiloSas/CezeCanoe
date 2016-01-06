import administration.UserActor
import com.google.inject.AbstractModule
import play.api.libs.concurrent.AkkaGuiceSupport

class DTEVModule extends AbstractModule with AkkaGuiceSupport {
  def configure() = {
    bindActor[UserActor]("user-actor")
  }
}