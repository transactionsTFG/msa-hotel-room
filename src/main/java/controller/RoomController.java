package controller;

import javax.ejb.EJB;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import business.dto.GetRoomDTO;
import business.servicesevent.RoomServiceEventAdapter;

@Path("/rooms")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RoomController {
    private static final Logger LOGGER = LogManager.getLogger(RoomController.class);
    private RoomServiceEventAdapter roomServiceEventAdapter;

    @EJB
    public void setRoomService(RoomServiceEventAdapter roomServiceEventAdapter) {
        this.roomServiceEventAdapter = roomServiceEventAdapter;
    }

    @POST
    @Transactional
    public Response getRoom(GetRoomDTO getRoomDTO) {
        LOGGER.info("Buscando habitación: {}", getRoomDTO);
        boolean isFound = roomServiceEventAdapter.beginGetRoom(getRoomDTO);
        if (!isFound)
            return Response.status(Response.Status.CONFLICT).entity("Habitación no encontrada").build();
        return Response.status(Response.Status.CREATED).entity("Habitación encontrada").build();
    }

}
