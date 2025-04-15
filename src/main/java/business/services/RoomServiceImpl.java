package business.services;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.LockModeType;

import business.exceptions.RoomException;
import business.mapper.RoomMapper;
import business.room.Room;
import business.room.RoomDTO;

@Stateless
public class RoomServiceImpl implements RoomService {

    private EntityManager entityManager;

    public RoomServiceImpl() {
    }

    @Inject
    public RoomServiceImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public void updateSagaId(List<String> roomIds, String sagaId) {

        roomIds.forEach(id -> {
            Room room = entityManager.find(Room.class, Long.parseLong(id), LockModeType.OPTIMISTIC);

            if (room == null || !room.isAvailable())
                throw new RoomException("Room with id" + id + " is either null or is not available.");

            room.setSagaId(sagaId);
            this.entityManager.merge(room);
        });

    }

    @Override
    public RoomDTO readRoomById(long roomId) {
        Room room = entityManager.find(Room.class, roomId, LockModeType.OPTIMISTIC);

        if (room == null)
            return null;

        return RoomMapper.INSTANCE.entityToDTO(room);
    }

    @Override
    public List<RoomDTO> readAllRooms() {
        return this.entityManager.createNamedQuery("business.room.getAllRooms", Room.class).getResultList().stream()
                .map(RoomMapper.INSTANCE::entityToDTO).toList();
    }

    @Override
    public List<RoomDTO> readRoomsByHotelAndCountry(String hotel, String country) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'readRoomsByHotelAndCountry'");
    }

}
