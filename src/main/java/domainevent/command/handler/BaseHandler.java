package domainevent.command.handler;

import javax.ejb.EJB;
import javax.inject.Inject;

import com.google.gson.Gson;

import business.services.RoomService;
import domainevent.publisher.IJMSEventPublisher;

public abstract class BaseHandler implements CommandPublisher {
    protected RoomService roomService;
    protected IJMSEventPublisher jmsEventPublisher;
    protected Gson gson;

    @EJB
    public void setTypeRoomServices(RoomService roomService) {
        this.roomService = roomService;
    }

    @EJB
    public void setJmsEventPublisher(IJMSEventPublisher jmsEventDispatcher) {
        this.jmsEventPublisher = jmsEventDispatcher;
    }

    @Inject
    public void setGson(Gson gson) {
        this.gson = gson;
    }
}