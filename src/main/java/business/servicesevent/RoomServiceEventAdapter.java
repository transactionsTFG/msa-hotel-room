package business.servicesevent;

import business.dto.GetRoomDTO;

public interface RoomServiceEventAdapter {
    boolean beginGetRoom(GetRoomDTO getRoomDTO);
}
