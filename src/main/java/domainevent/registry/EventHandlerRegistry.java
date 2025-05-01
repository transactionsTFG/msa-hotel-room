package domainevent.registry;

import java.util.EnumMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;

import business.qualifier.ValidateHotelRoomsByCreateHotelBookingQualifier;
import business.qualifier.ValidateHotelRoomsByUpdateHotelBookingEventQualifier;
import domainevent.command.handler.CommandPublisher;

import msa.commons.event.EventId;

@Singleton
@Startup
public class EventHandlerRegistry {
    private Map<EventId, CommandPublisher> handlers = new EnumMap<>(EventId.class);

    private CommandPublisher validateHotelRoomsByCreateHotelBooking;
    private CommandPublisher validateHotelRoomsByUpdateHotelBooking;

    @PostConstruct
    public void init() {
        this.handlers.put(EventId.VALIDATE_HOTEL_ROOMS_BY_CREATE_HOTEL_BOOKING, validateHotelRoomsByCreateHotelBooking);
        this.handlers.put(EventId.VALIDATE_HOTEL_ROOMS_BY_UPDATE_HOTEL_BOOKING, validateHotelRoomsByUpdateHotelBooking);
    }

    public CommandPublisher getHandler(EventId eventId) {
        return this.handlers.get(eventId);
    }

    @Inject
    public void setValidateHotelRoomsByCreateHotelBooking(
            @ValidateHotelRoomsByCreateHotelBookingQualifier CommandPublisher validateHotelRoomsByCreateHotelBookingQualifier) {
        this.validateHotelRoomsByCreateHotelBooking = validateHotelRoomsByCreateHotelBookingQualifier;
    }

    @Inject
    public void setValidateHotelRoomsByUpdateHotelBooking(
            @ValidateHotelRoomsByUpdateHotelBookingEventQualifier CommandPublisher validateHotelRoomsByUpdateHotelBookingQualifier) {
        this.validateHotelRoomsByUpdateHotelBooking = validateHotelRoomsByUpdateHotelBookingQualifier;
    }

}