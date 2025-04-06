package business.services;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.LockModeType;

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
    public RoomDTO beginGetRoom(long roomId) {
        Room room = entityManager.find(Room.class, roomId, LockModeType.OPTIMISTIC);

        if (room == null)
            return null;

        return RoomMapper.INSTANCE.entityToDTO(room);
    }

}
