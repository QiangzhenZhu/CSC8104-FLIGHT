package uk.ac.newcastle.enterprisemiddleware.coursework.book;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import uk.ac.newcastle.enterprisemiddleware.coursework.customer.Customer;
import uk.ac.newcastle.enterprisemiddleware.coursework.flight.Flight;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

/**
 * @description 航班预订
 */

@Entity
@NamedQueries({
        @NamedQuery(name = FlightBooking.FIND_ALL, query = "SELECT c FROM FlightBooking c"),
        @NamedQuery(name = FlightBooking.FIND_BY_CUSTOMER_ID,
                query = "SELECT c FROM FlightBooking c WHERE c.customerId = :customerId"),
        @NamedQuery(name = FlightBooking.FIND_BY_FLIGHT_ID_AND_DATE,
                query = "SELECT c FROM FlightBooking c WHERE c.flightId = :flightId AND c.bookingDate = :bookingDate")
})
@Table(name = "FlightBooking", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"flightId", "bookingDate" }), // 组合索引，确定预订的航班和预订的日期的组合应该是唯一的
})
public class FlightBooking implements Serializable {

    private static final long serialVersionUID = 1L;
    public static final String FIND_ALL = "FlightBooking.findAll";
    public static final String FIND_BY_CUSTOMER_ID = "FlightBooking.findByCustomerId";
    public static final String FIND_BY_FLIGHT_ID_AND_DATE = "FlightBooking.findByFlightIdAndDate";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "customerId")
    private Long customerId;

    @NotNull
    @Column(name = "flightId")
    private Long flightId;

    @NotNull
    @Column(name = "bookingDate")
    private Date bookingDate;

    @JsonIgnore
    @JoinColumn(name = "customerId", insertable = false, updatable = false)
    @ManyToOne(cascade = CascadeType.REMOVE)
    private Customer customer;

    @JsonIgnore
    @JoinColumn(name = "flightId", insertable = false, updatable = false)
    @ManyToOne(cascade = CascadeType.REMOVE)
    private Flight flight;

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

    public Long getFlightId() {
        return flightId;
    }

    public void setFlightId(Long flightId) {
        this.flightId = flightId;
    }

    public Date getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(Date bookingDate) {
        this.bookingDate = bookingDate;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Flight getFlight() {
        return flight;
    }

    public void setFlight(Flight flight) {
        this.flight = flight;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        FlightBooking that = (FlightBooking) o;

        if (!Objects.equals(flightId, that.flightId))
            return false;
        return Objects.equals(bookingDate, that.bookingDate);
    }

    @Override
    public int hashCode() {
        int result = flightId != null ? flightId.hashCode() : 0;
        result = 31 * result + (bookingDate != null ? bookingDate.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("FlightBooking{");
        sb.append("id=").append(id);
        sb.append(", customerId=").append(customerId);
        sb.append(", flightId=").append(flightId);
        sb.append(", bookingDate=").append(bookingDate);
        sb.append(", customer=").append(customer);
        sb.append(", flight=").append(flight);
        sb.append('}');
        return sb.toString();
    }
}
