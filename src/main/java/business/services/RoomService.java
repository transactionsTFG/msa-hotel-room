package business.services;

import java.util.List;

import business.room.RoomDTO;
import business.room.RoomInfoDTO;
import msa.commons.commands.hotelroom.model.RoomInfo;

public interface RoomService {
    void updateSagaId(List<RoomInfo> roomIds, String sagaId);

    boolean validateRooms(List<RoomInfo> rooms);

    RoomDTO readRoomById(long roomId);

    List<RoomDTO> readAllRooms();

    List<RoomInfoDTO> readRoomsByHotelAndCountry(String hotel, String country);
}
