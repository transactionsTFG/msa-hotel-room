package domainevent.command;

import java.util.List;

import javax.ejb.Local;
import javax.ejb.Stateless;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import business.qualifier.ValidateHotelRoomsByUpdateHotelBookingEventQualifier;
import business.room.RoomDTO;
import domainevent.command.handler.BaseHandler;
import domainevent.command.handler.CommandPublisher;
import msa.commons.commands.hotelbooking.UpdateHotelBookingCommand;
import msa.commons.commands.hotelroom.model.RoomInfo;
import msa.commons.event.EventData;
import msa.commons.event.EventId;

@Stateless
@ValidateHotelRoomsByUpdateHotelBookingEventQualifier
@Local(CommandPublisher.class)
public class ValidateRoomsByUpdateHotelBookingEvent extends BaseHandler {

    private static final Logger LOGGER = LogManager.getLogger(ValidateRoomsByUpdateHotelBookingEvent.class);

    @Override
    public void publishCommand(String json) {

        LOGGER.info("JSON recibido: {}", json);

        EventData eventData = EventData.fromJson(json, UpdateHotelBookingCommand.class);
        UpdateHotelBookingCommand command = (UpdateHotelBookingCommand) eventData.getData();
        List<RoomInfo> roomIds = command.getRoomsInfo();
        
        boolean roomsValidated = this.roomService.validateRooms(roomIds);
        this.roomService.updateSagaId(roomIds, eventData.getSagaId());
        
        if (!roomsValidated) {
            for (RoomInfo roomInfo : roomIds) {
                RoomDTO r = this.roomService.readRoomById(Long.parseLong(roomInfo.getRoomId()));
                roomInfo.setDailyPrice(r.getDailyPrice());
            }
            this.jmsEventPublisher.publish(EventId.CANCEL_VALIDATE_HOTEL_ROOMS_BY_UPDATE_HOTEL_BOOKING, eventData);
        } else {
            this.jmsEventPublisher.publish(EventId.CONFIRM_VALIDATE_HOTEL_ROOMS_BY_UPDATE_HOTEL_BOOKING, eventData);
        }
    }

}
