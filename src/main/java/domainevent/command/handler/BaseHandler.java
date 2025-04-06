package domainevent.command.handler;

import javax.ejb.EJB;
import javax.inject.Inject;

import business.services.RoomService;
import domainevent.publisher.IJMSEventPublisher;

public abstract class BaseHandler implements EventHandler {
    protected RoomService roomService;
    protected IJMSEventPublisher jmsEventDispatcher;
    @EJB
    public void setTypeRoomServices(RoomService roomService) {
        this.roomService = roomService;
    }
    @Inject
    public void setJmsEventDispatcher(IJMSEventPublisher jmsEventDispatcher) {
        this.jmsEventDispatcher = jmsEventDispatcher;
    }
}