package domainevent.consumer;

import javax.ejb.EJB;
import javax.ejb.MessageDriven;
import javax.inject.Inject;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import com.google.gson.Gson;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import domainevent.command.handler.EventHandler;
import domainevent.registry.EventHandlerRegistry;
import msa.commons.consts.JMSQueueNames;
import msa.commons.event.Event;

@MessageDriven(mappedName = JMSQueueNames.AGENCY_HOTEL_ROOM_SERVICE_QUEUE)
public class DomainEventConsumerRoomService implements MessageListener {

    private Gson gson;
    private EventHandlerRegistry eventHandlerRegistry;
    private static final Logger LOGGER = LogManager.getLogger(DomainEventConsumerRoomService.class);

    @Inject
    public void setGson(Gson gson) {
        this.gson = gson;
    }

    @EJB
    public void setCommandHandlerRegistry(EventHandlerRegistry commandHandlerRegistry) {
        this.eventHandlerRegistry = commandHandlerRegistry;
    }

    @Override
    public void onMessage(Message msg) {
        try {
            if (msg instanceof TextMessage m) {
                Event event = this.gson.fromJson(m.getText(), Event.class);
                LOGGER.info("Recibido en cola {}, Evento Id: {}, Mensaje: {}", JMSQueueNames.AGENCY_HOTEL_ROOM_SERVICE_QUEUE,
                        event.getEventId(), event.getData().toString());
                EventHandler handler = this.eventHandlerRegistry.getHandler(event.getEventId());
                if (handler != null) 
                    handler.handleCommand(event.getData());
            }
        } catch (Exception e) {
            LOGGER.error("Error al recibir el mensaje: {}", e.getMessage());
        }
    }

}
