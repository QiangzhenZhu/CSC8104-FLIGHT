package uk.ac.newcastle.enterprisemiddleware.coursework.book;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.jboss.resteasy.reactive.RestQuery;

import javax.ws.rs.*;
import java.util.List;

@Path("/bookings")
@RegisterRestClient(configKey = "taxi-booking-api")
public interface TaxiBookingService {

    @POST
    TaxiBooking createTaxiBooking(TaxiBooking taxiBooking);

    @DELETE
    @Path("/{id:[0-9]+}")
    TaxiBooking deleteTaxiBooking(@PathParam("id") Long id);

    @GET
    List<TaxiBooking> getTaxiBookings(@RestQuery("id") Long id);


    @PUT
    @Path("/{id:[0-9]+}")
    TaxiBooking updateTaxiBookingById(TaxiBooking taxiBooking);

}
