package business.services;

import java.util.List;

import business.room.RoomDTO;
import msa.commons.commands.hotelroom.model.RoomInfo;

public interface RoomService {
    void updateSagaId(List<RoomInfo> roomIds, String sagaId);

    boolean validateRooms(List<RoomInfo> rooms);

    RoomDTO readRoomById(long roomId);

    List<RoomDTO> readAllRooms();

    List<RoomDTO> readRoomsByHotelAndCountry(String hotel, String country);
}
