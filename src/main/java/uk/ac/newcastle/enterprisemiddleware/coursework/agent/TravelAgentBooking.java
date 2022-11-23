package uk.ac.newcastle.enterprisemiddleware.coursework.agent;

import com.fasterxml.jackson.annotation.JsonIgnore;
import uk.ac.newcastle.enterprisemiddleware.coursework.book.FlightBooking;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @description 旅游社预定实体类
 */

@Entity
@NamedQueries({
        @NamedQuery(name = TravelAgentBooking.FIND_ALL, query = "SELECT c FROM TravelAgentBooking c ORDER BY c.customerId"),
        @NamedQuery(name = TravelAgentBooking.FIND_BY_CUSTOMER_ID, query = "SELECT c FROM TravelAgentBooking c WHERE c.customerId = :customerId")
})
@XmlRootElement
@Table(name = "TravelAgentBooking")
public class TravelAgentBooking {

    private static final long serialVersionUID = 1462154887L;

    public static final String FIND_ALL = "TravelAgentBooking.findAll";
    public static final String FIND_BY_CUSTOMER_ID = "TravelBooking.findByCustomerId";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "customerId")
    private Long customerId;

    @Column(name = "hotelBookingId")
    private Long hotelBookingId;

    @Column(name = "taxiBookingId")
    private Long taxiBookingId;

    @Column(name = "flightBookingId")
    private Long flightBookingId;

    @JsonIgnore
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn
    private FlightBooking flightBooking;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Long getHotelBookingId() {
        return hotelBookingId;
    }

    public void setHotelBookingId(Long hotelBookingId) {
        this.hotelBookingId = hotelBookingId;
    }

    public Long getTaxiBookingId() {
        return taxiBookingId;
    }

    public void setTaxiBookingId(Long taxiBookingId) {
        this.taxiBookingId = taxiBookingId;
    }

    public Long getFlightBookingId() {
        return flightBookingId;
    }

    public void setFlightBookingId(Long flightBookingId) {
        this.flightBookingId = flightBookingId;
    }

    public FlightBooking getFlightBooking() {
        return flightBooking;
    }

    public void setFlightBooking(FlightBooking flightBooking) {
        this.flightBooking = flightBooking;
    }
}
