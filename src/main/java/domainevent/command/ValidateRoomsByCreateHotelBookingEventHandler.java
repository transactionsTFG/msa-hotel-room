package domainevent.command;

import java.util.List;

import javax.ejb.Local;
import javax.ejb.Stateless;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import business.qualifier.ValidateHotelRoomsByCreateHotelBookingQualifier;
import business.room.RoomDTO;
import domainevent.command.handler.BaseHandler;
import domainevent.command.handler.CommandPublisher;
import msa.commons.commands.hotelbooking.CreateHotelBookingCommand;
import msa.commons.commands.hotelroom.model.RoomInfo;
import msa.commons.event.EventData;
import msa.commons.event.EventId;

@Stateless
@ValidateHotelRoomsByCreateHotelBookingQualifier
@Local(CommandPublisher.class)
public class ValidateRoomsByCreateHotelBookingEventHandler extends BaseHandler {

    private static final Logger LOGGER = LogManager.getLogger(ValidateRoomsByCreateHotelBookingEventHandler.class);

    @Override
    public void publishCommand(String json) {
        LOGGER.info("JSON recibido: {}", json);
        EventData eventData = EventData.fromJson(json, CreateHotelBookingCommand.class);
        CreateHotelBookingCommand command = (CreateHotelBookingCommand) eventData.getData();
        List<RoomInfo> roomIds = command.getRoomsInfo();

        boolean roomsValidated = this.roomService.validateRooms(roomIds);
        this.roomService.updateSagaId(roomIds, eventData.getSagaId());

        if (!roomsValidated) {
            this.jmsEventPublisher.publish(EventId.CANCEL_VALIDATE_HOTEL_ROOMS_BY_CREATE_HOTEL_BOOKING, eventData);
        } else {
            command.getRoomsInfo().forEach(r -> {
                RoomDTO roomDTO = this.roomService.readRoomById(Long.parseLong(r.getRoomId()));
                r.setDailyPrice(roomDTO.getDailyPrice());
            });
            this.jmsEventPublisher.publish(EventId.CONFIRM_VALIDATE_HOTEL_ROOMS_BY_CREATE_HOTEL_BOOKING, eventData);
        }
    }

}
