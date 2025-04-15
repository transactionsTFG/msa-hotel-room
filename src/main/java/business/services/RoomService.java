package business.services;

import java.util.List;

import business.room.RoomDTO;

public interface RoomService {
    void updateSagaId(List<String> roomIds, String sagaId);
    RoomDTO readRoomById(long roomId);
    List<RoomDTO> readAllRooms();
    List<RoomDTO> readRoomsByHotelAndCountry(String hotel, String country);
}
