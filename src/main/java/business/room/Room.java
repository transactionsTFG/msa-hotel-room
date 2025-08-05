package business.room;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Version;

import business.hotel.Hotel;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@NamedQueries({
        @NamedQuery(name = "business.room.getByRoomNumber", query = "SELECT r FROM Room r WHERE r.number = :number"),
        @NamedQuery(name = "business.room.getAllRoomsByHotel",query = "SELECT r FROM Room r WHERE (:hotelId IS NULL OR r.hotel.id = :hotelId)"),
        @NamedQuery(name = "business.room.getAllRooms", query = "SELECT r FROM Room r"),
        @NamedQuery(name = "business.room.getRoomsByHotelAndCountry",
                query = "SELECT r FROM Room r JOIN r.hotel h JOIN h.countries c WHERE (:hotelName IS NULL OR h.name = :hotelName) AND (:countryId IS NULL OR c.id = :countryId)")
})
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Version
    private int version;

    @ManyToOne
    @JoinColumn(name = "hotel_id")
    private Hotel hotel;

    private int number;

    private boolean singleBed;

    private boolean available;

    private int peopleNumber;

    private double dailyPrice;

    private String sagaId;

}