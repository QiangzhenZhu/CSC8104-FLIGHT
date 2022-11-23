package uk.ac.newcastle.enterprisemiddleware.coursework.agent;

import uk.ac.newcastle.enterprisemiddleware.coursework.book.FlightBooking;
import uk.ac.newcastle.enterprisemiddleware.coursework.book.HotelBooking;
import uk.ac.newcastle.enterprisemiddleware.coursework.book.TaxiBooking;
import uk.ac.newcastle.enterprisemiddleware.coursework.customer.Customer;

import javax.validation.constraints.NotNull;

/**
 * @description 旅游社实体类
 */

public class TravelAgent {
    private static final long serialVersionUID = 145672386778L;

    @NotNull
    private Customer customer;

    @NotNull
    private TaxiBooking taxiBooking;

    @NotNull
    private FlightBooking flightBooking;

    @NotNull
    private HotelBooking hotelBooking;

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public TaxiBooking getTaxiBooking() {
        return taxiBooking;
    }

    public void setTaxiBooking(TaxiBooking taxiBooking) {
        this.taxiBooking = taxiBooking;
    }

    public FlightBooking getFlightBooking() {
        return flightBooking;
    }

    public void setFlightBooking(FlightBooking flightBooking) {
        this.flightBooking = flightBooking;
    }

    public HotelBooking getHotelBooking() {
        return hotelBooking;
    }

    public void setHotelBooking(HotelBooking hotelBooking) {
        this.hotelBooking = hotelBooking;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("TravelAgent{");
        sb.append("customer=").append(customer);
        sb.append(", taxiBooking=").append(taxiBooking);
        sb.append(", flightBooking=").append(flightBooking);
        sb.append(", hotelBooking=").append(hotelBooking);
        sb.append('}');
        return sb.toString();
    }
}
