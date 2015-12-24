
// @GENERATOR:play-routes-compiler
// @SOURCE:/home/loann/Desktop/CezeCanoe/server/conf/routes
// @DATE:Tue Dec 22 15:06:39 CET 2015

package Room;

import router.RoutesPrefix;

public class routes {
  
  public static final Room.ReverseRoomController RoomController = new Room.ReverseRoomController(RoutesPrefix.byNamePrefix());

  public static class javascript {
    
    public static final Room.javascript.ReverseRoomController RoomController = new Room.javascript.ReverseRoomController(RoutesPrefix.byNamePrefix());
  }

}
