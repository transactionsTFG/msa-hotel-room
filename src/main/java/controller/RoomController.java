package controller;

import java.util.List;

import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import business.room.RoomDTO;
import business.room.RoomInfoDTO;
import business.services.RoomService;

@Path("/room")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RoomController {
    
    private static final Logger LOGGER = LogManager.getLogger(RoomController.class);
    private RoomService roomService;

    @GET
    @Path("/{id}")
    public RoomDTO getRoomById(@PathParam("id") long id) {
        LOGGER.info("Fetching room with ID: {}", id);
        return this.roomService.readRoomById(id);
    }

    @GET
    @Path("/params")
    public List<RoomInfoDTO> getRoomsByParams(@QueryParam("hotelName") String hotelName, @QueryParam("country") String country) {
        LOGGER.info("Fetching rooms with hotelName: {} and country: {}", hotelName, country);
        return this.roomService.readRoomsByHotelAndCountry(hotelName, country);
    }

    @EJB
    public void setRoomService(RoomService roomService) {
        this.roomService = roomService;
    }
}
