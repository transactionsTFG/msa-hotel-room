package business.services;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.LockModeType;

import org.eclipse.persistence.internal.sessions.DirectCollectionChangeRecord.NULL;

import business.external.country.CountryClient;
import business.external.country.CountryDTO;
import business.mapper.RoomMapper;
import business.room.Room;
import business.room.RoomDTO;
import business.room.RoomInfoDTO;
import msa.commons.commands.hotelroom.model.RoomInfo;
import weblogic.informix.jdbc.base.ge;

@Stateless
public class RoomServiceImpl implements RoomService {

    private EntityManager entityManager;
    private CountryClient countryClient;

    @Override
    public void updateSagaId(List<RoomInfo> roomsInfo, String sagaId) {
        if (roomsInfo == null)
            return;
        roomsInfo.forEach(roomInfo -> {
            Room room = entityManager.find(Room.class, Long.parseLong(roomInfo.getRoomId()));

            if (room == null || !room.isAvailable()) {
                return;
                // throw new RoomException("Room with id" + roomInfo + " is either null or is not available.");
            }

            room.setSagaId(sagaId);
            this.entityManager.merge(room);
        });

    }

    @Override
    public boolean validateRooms(List<RoomInfo> rooms) {
        if (rooms == null)
            return false;
        for (RoomInfo roomInfo : rooms) {

            Room room = this.entityManager.find(Room.class, Long.parseLong(roomInfo.getRoomId()),
                    LockModeType.OPTIMISTIC);

            if (room == null || !room.isAvailable()) {
                return false;
            }

        }

        return true;
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
    public List<RoomInfoDTO> readRoomsByHotelAndCountry(String hotel, final String country) {
        CountryDTO countryDTO = null;
        if (country != null && !country.isBlank()) {
            countryDTO = this.countryClient.getCountryName(country);
            if (countryDTO == null) {
                countryDTO = new CountryDTO();
                countryDTO.setName(country);
                countryDTO.setId(-1L);
            }
        }
           

        List<Room> roomInfoDTOs = this.entityManager.createNamedQuery("business.room.getRoomsByHotelAndCountry", Room.class)
                .setParameter("hotelName", hotel)
                .setParameter("countryId", countryDTO == null ? null : countryDTO.getId())
                .getResultList();

        List<RoomInfoDTO> result = new ArrayList<>();
        for (Room entity : roomInfoDTOs) {
            RoomDTO r = RoomMapper.INSTANCE.entityToDTO(entity);
            RoomInfoDTO roomInfoDTO = new RoomInfoDTO();
            roomInfoDTO.setHotelName(entity.getHotel().getName());
            if (countryDTO != null) 
                roomInfoDTO.setCountry(countryDTO.getName());
            else 
                roomInfoDTO.setCountry(this.countryClient.getCountryById(entity.getHotel().getCountries().iterator().next().getCountryId()).getName());
            roomInfoDTO.setId(r.getId());
            roomInfoDTO.setHotelId(r.getHotelId());
            roomInfoDTO.setNumber(r.getNumber());
            roomInfoDTO.setSingleBed(r.isSingleBed());
            roomInfoDTO.setAvailable(r.isAvailable());
            roomInfoDTO.setPeopleNumber(r.getPeopleNumber());
            roomInfoDTO.setDailyPrice(r.getDailyPrice());
            result.add(roomInfoDTO);
        }

        return result;
    }

    @Inject
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
    @Inject
    public void setCountryClient(CountryClient countryClient) {
        this.countryClient = countryClient;
    }
}
