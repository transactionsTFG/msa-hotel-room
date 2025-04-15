package domainevent.command;

import java.util.List;

import javax.ejb.Local;
import javax.ejb.Stateless;

import business.room.RoomDTO;
import domainevent.command.handler.BaseHandler;
import domainevent.command.handler.CommandPublisher;
import msa.commons.event.EventData;
import msa.commons.event.EventId;
import msa.commons.microservices.hotelbooking.commandevent.CreateHotelBookingCommand;
import msa.commons.microservices.hotelroom.commandevent.model.RoomInfo;
import msa.commons.microservices.hotelroom.qualifier.ValidateRoomsByEventCreateBookingQualifier;

@Stateless
@ValidateRoomsByEventCreateBookingQualifier
@Local(CommandPublisher.class)
public class ValidateRoomsByEventCreateBookingHandler extends BaseHandler {

    @Override
    public void publishCommand(String json) {
        EventData eventData = EventData.fromJson(json, CreateHotelBookingCommand.class);
        CreateHotelBookingCommand command = (CreateHotelBookingCommand) eventData.getData();
        List<RoomInfo> roomIds = command.getRoomsInfo();

        List<RoomDTO> roomDTOs = this.roomService.readAllRooms();
        this.roomService.updateSagaId(roomIds, eventData.getSagaId());

        if (roomDTOs.isEmpty()) {
            this.jmsEventPublisher.publish(EventId.ROLLBACK_CREATE_HOTEL_BOOKING, eventData);
        } else {
            this.jmsEventPublisher.publish(EventId.COMMIT_CREATE_HOTEL_BOOKING, eventData);
        }
    }

}
