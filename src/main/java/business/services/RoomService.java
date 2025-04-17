package business.services;

import java.util.List;

import msa.commons.microservices.hotelroom.commandevent.model.RoomInfo;

import business.room.RoomDTO;

public interface RoomService {
    void updateSagaId(List<RoomInfo> roomIds, String sagaId);
    boolean validateRooms(List<RoomInfo> rooms);
    RoomDTO readRoomById(long roomId);
    List<RoomDTO> readAllRooms();
    List<RoomDTO> readRoomsByHotelAndCountry(String hotel, String country);
}
