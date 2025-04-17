package domainevent.command;

import java.util.List;

import javax.ejb.Local;
import javax.ejb.Stateless;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import domainevent.command.handler.BaseHandler;
import domainevent.command.handler.CommandPublisher;
import msa.commons.event.EventData;
import msa.commons.event.EventId;
import msa.commons.microservices.hotelbooking.commandevent.CreateHotelBookingCommand;
import msa.commons.microservices.hotelroom.commandevent.model.RoomInfo;
import msa.commons.microservices.hotelroom.qualifier.ValidateHotelRoomsByCreateHotelBookingQualifier;

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
            this.jmsEventPublisher.publish(EventId.CONFIRM_VALIDATE_HOTEL_ROOMS_BY_CREATE_HOTEL_BOOKING, eventData);
        }
    }

}
