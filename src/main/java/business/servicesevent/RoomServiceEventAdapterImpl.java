package business.servicesevent;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;

import business.dto.GetRoomDTO;
import business.room.RoomDTO;
import business.services.RoomService;
import domainevent.publisher.IJMSEventPublisher;
import msa.commons.event.EventId;
import msa.commons.microservices.hotelroom.commandevent.GetHotelRoomCommand;
import msa.commons.parser.NumberParser;

@Stateless
public class RoomServiceEventAdapterImpl implements RoomServiceEventAdapter {

    private RoomService roomService;
    private IJMSEventPublisher jmsEventPublisher;

    @EJB
    public void setRoomService(RoomService roomService) {
        this.roomService = roomService;
    }

    @Inject
    public void setJmsEventDispatcher(IJMSEventPublisher jmsEventPublisher) {
        this.jmsEventPublisher = jmsEventPublisher;
    }

    @Override
    public boolean beginGetRoom(GetRoomDTO getRoomDTO) {
        long roomId = NumberParser.toLong(getRoomDTO.getRoomId());
        RoomDTO roomDTO = this.roomService.beginGetRoom(roomId);

        if (roomDTO == null)
            return false;

        this.jmsEventPublisher.publish(EventId.GET_ROOM, new GetHotelRoomCommand(getRoomDTO.getRoomId()));
        return true;
    }

}
