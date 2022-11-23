package uk.ac.newcastle.enterprisemiddleware.coursework.flight;

import com.fasterxml.jackson.annotation.JsonIgnore;
import uk.ac.newcastle.enterprisemiddleware.coursework.book.FlightBooking;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@Entity
@NamedQueries({
        @NamedQuery(name = Flight.FIND_ALL, query = "SELECT c FROM Flight c ORDER BY c.flightNumber ASC"),
        @NamedQuery(name = Flight.FIND_BY_FLIGHT_NUMBER, query = "SELECT c FROM Flight c WHERE c.flightNumber = :flightNumber")
})
@Table(name = "Flight", uniqueConstraints = @UniqueConstraint(columnNames = "flightNumber"))
public class Flight implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final String FIND_ALL = "Flight.findAll";
    public static final String FIND_BY_FLIGHT_NUMBER = "Flight.findByFlightNumber";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Pattern(regexp = "^([A-Za-z0-9]{5})$", message = "a non-empty alpha-numerical string which is 5 characters in length.")
    @Column(name = "flightNumber")
    private String flightNumber;

    @NotNull
    @Pattern(regexp = "^([A-Z]{3})$", message = "a non-empty alphabetical string, which is upper case, 3 characters in length.")
    @Column(name = "departure")
    private String departure;

    @NotNull
    @Pattern(regexp = "^([A-Z]{3})$", message = "a non-empty alphabetical string, which is upper case, 3 characters in length.")
    @Column(name = "destination")
    private String destination;

    // 用于级联删除，确保删除航班后，删除对应的航班预定
    @JsonIgnore
    @OneToMany(mappedBy = "flight", cascade = CascadeType.REMOVE)
    private List<FlightBooking> flightBookings;

    public List<FlightBooking> getFlightBookings() {
        return flightBookings;
    }

    public void setFlightBookings(List<FlightBooking> flightBookings) {
        this.flightBookings = flightBookings;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
    }

    public String getDeparture() {
        return departure;
    }

    public void setDeparture(String departure) {
        this.departure = departure;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        Flight flight = (Flight) o;

        return Objects.equals(flightNumber, flight.flightNumber);
    }

    @Override
    public int hashCode() {
        return flightNumber != null ? flightNumber.hashCode() : 0;
    }
}