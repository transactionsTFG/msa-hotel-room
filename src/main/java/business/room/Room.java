package business.room;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Version;
import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@NamedQueries({
        @NamedQuery(name = "business.room.getByRoomNumber", query = "SELECT r FROM Room r WHERE r.number = :number"),
        @NamedQuery(name = "business.room.getAllRoomsByHotel", query = "SELECT r, r.hotelId FROM Room r WHERE (:hotelId is NULL OR r.hotelId = :hotelId)"),
})
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Room implements Serializable {

    private static final long serialVersionUID = 0;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Version
    private int version;

    private String hotelId;

    private int number;

    private boolean singleBed;

    private boolean available;

    private int peopleNumber;

    private double dailyPrice;

}