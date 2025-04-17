package domainevent.registry;

import java.util.EnumMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;

import domainevent.command.handler.CommandPublisher;

import msa.commons.event.EventId;
import msa.commons.microservices.hotelroom.qualifier.ValidateHotelRoomsByCreateHotelBookingQualifier;

@Singleton
@Startup
public class EventHandlerRegistry {
    private Map<EventId, CommandPublisher> handlers = new EnumMap<>(EventId.class);

    private CommandPublisher validateHotelRoomsByCreateHotelBooking;

    @PostConstruct
    public void init() {
        this.handlers.put(EventId.VALIDATE_HOTEL_ROOMS_BY_CREATE_HOTEL_BOOKING, validateHotelRoomsByCreateHotelBooking);
    }

    public CommandPublisher getHandler(EventId eventId) {
        return this.handlers.get(eventId);
    }

    @Inject
    public void setValidateHotelRoomsByCreateHotelBooking(
            @ValidateHotelRoomsByCreateHotelBookingQualifier CommandPublisher validateHotelRoomsByCreateHotelBookingQualifier) {
        this.validateHotelRoomsByCreateHotelBooking = validateHotelRoomsByCreateHotelBookingQualifier;
    }

}