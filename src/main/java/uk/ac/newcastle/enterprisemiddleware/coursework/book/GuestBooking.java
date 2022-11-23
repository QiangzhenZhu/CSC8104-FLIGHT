package uk.ac.newcastle.enterprisemiddleware.coursework.book;

import uk.ac.newcastle.enterprisemiddleware.coursework.customer.Customer;

import javax.validation.constraints.NotNull;

/**
 * @description 交易端点类
 */

public class GuestBooking {
    @NotNull
    private Customer customer;
    @NotNull
    private FlightBooking flightBooking;

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public FlightBooking getFlightBooking() {
        return flightBooking;
    }

    public void setFlightBooking(FlightBooking flightBooking) {
        this.flightBooking = flightBooking;
    }
}
