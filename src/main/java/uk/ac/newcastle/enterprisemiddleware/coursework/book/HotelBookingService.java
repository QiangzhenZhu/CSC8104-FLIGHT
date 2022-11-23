package uk.ac.newcastle.enterprisemiddleware.coursework.book;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.ws.rs.*;
import java.util.List;

/**
 * @description 酒店预订服务接口
 */

@Path("/hotel_booking")
@RegisterRestClient(configKey = "hotel-booking-api")
public interface HotelBookingService {
    @GET
    List<HotelBooking> findAllHotelBookings();

    @POST
    HotelBooking createHotelBooking(HotelBooking hotelbooking);

    @GET
    @Path("/email/{email:[0-9]+}")
    List<HotelBooking> getHotelBookingsByEmail(@PathParam("email") String email);

    @GET
    @Path("/{id:[0-9]+}")
    HotelBooking findHotelBookingById(@PathParam("id") Long id);

    @PUT
    @Path("/{id:[0-9]+}")
    HotelBooking updateHotelBookingById(@PathParam("id") Long id);

    @DELETE
    @Path("/{id:[0-9]+}")
    HotelBooking deleteHotelBooking(@PathParam("id") Long id);
}
