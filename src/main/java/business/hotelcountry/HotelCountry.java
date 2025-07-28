package business.hotelcountry;


import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;


import business.hotel.Hotel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HotelCountry {
    
    @EmbeddedId
    private HotelCountryId id;

    @ManyToOne(optional = false)
    @MapsId("hotelId")
    @JoinColumn(name = "hotel_id", referencedColumnName = "id")
    private Hotel hotel;

    @Column(nullable = false, name = "country_id")
    @MapsId("countryId")
    private long countryId;

}
