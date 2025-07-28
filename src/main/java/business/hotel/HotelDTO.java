package business.hotel;

import java.util.List;

import business.room.RoomDTO;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class HotelDTO {
    private long id;
    private String name;
    private int version;
    private List<RoomDTO> rooms;
    private List<Long> countries;
}
