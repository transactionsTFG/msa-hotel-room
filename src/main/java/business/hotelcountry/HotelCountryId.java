package business.hotelcountry;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HotelCountryId implements Serializable{
    @Column(name = "hotel_id")
    private Long hotelId;
    @Column(name = "country_id", insertable = false, updatable = false)
    private Long countryId;
}