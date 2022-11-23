package uk.ac.newcastle.enterprisemiddleware.coursework.book;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import uk.ac.newcastle.enterprisemiddleware.coursework.customer.Customer;
import uk.ac.newcastle.enterprisemiddleware.coursework.customer.CustomerService;
import uk.ac.newcastle.enterprisemiddleware.coursework.flight.Flight;
import uk.ac.newcastle.enterprisemiddleware.coursework.flight.FlightService;
import uk.ac.newcastle.enterprisemiddleware.util.RestServiceException;

import javax.inject.Inject;
import javax.inject.Named;
import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.logging.Logger;

/**
 * @description 飞机预订接口类
 */

@Path("/api/flightBooking")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class FlightBookingRestService {

    @Inject
    @Named("logger")
    Logger log;

    @Inject
    FlightBookingService flightBookingService;

    @Inject
    FlightService flightService;

    @Inject
    CustomerService customerService;

    @GET
    @Path("/findAllBookings")
    @Operation(
            summary = "Fetch all flight bookings",
            description = "Returns a JSON array of all stored flight booking objects.")
    public Response findAllBooking() {
        List<FlightBooking> flightBookings = flightBookingService.findAll();
        return Response.ok(flightBookings).build();
    }

    @GET
    @Path("/findBookingByCustomerId/{customerId:[0-9]+}")
    @Operation(
            summary = "Fetch flight booking by customerId",
            description = "Returns a JSON array of all stored flight booking objects.")
    public Response findBookingByCustomerId(@PathParam("customerId") Long customerId) {
        if (customerId == null || customerId <= 0) {
            throw new RestServiceException("Bad Request", Response.Status.BAD_REQUEST);
        }
        List<FlightBooking> flightBookings = flightBookingService.findByCustomerId(customerId);
        return Response.ok(flightBookings).build();
    }

    @GET
    @Path("/findBookingById/{id:[0-9]+}")
    @Operation(
            summary = "Fetch flight booking by id",
            description = "Returns a JSON representation of the flight object with the provided id.")
    public Response findBookingById(@PathParam("id") Long id) {
        if (id == null || id <= 0) {
            throw new RestServiceException("Bad Request", Response.Status.BAD_REQUEST);
        }
        FlightBooking flightBooking = flightBookingService.findById(id);
        return Response.ok(flightBooking).build();
    }

    @POST
    @Path("/createFlightBooking")
    @Operation(
            summary = "post a new flight booking",
            description = "posts a new flight booking object to the database.")
    @APIResponses(value = {
            @APIResponse(responseCode = "201", description = "Flight booking created successfully."),
            @APIResponse(responseCode = "400", description = "Invalid flight booking object supplied in request body"),
            @APIResponse(responseCode = "409", description = "flight booking already exist"),
            @APIResponse(responseCode = "500", description = "An unexpected error occurred whilst processing the request")
    })
    @Transactional
    public Response createFlightBooking(@Valid FlightBooking flightBooking) {
        if (flightBooking == null) {
            throw new RestServiceException("Bad Request", Response.Status.BAD_REQUEST);
        }
        Flight flight = flightService.findFlightById(flightBooking.getFlightId());
        Customer customer = customerService.findById(flightBooking.getCustomerId());
        FlightBooking queryFlight = flightBookingService.findByFlightIdAndDate(flightBooking.getFlightId(),
                flightBooking.getBookingDate());
        if (flight == null) {
            throw new RestServiceException("No flight with id " + flightBooking.getFlightId() +
                    " was found", Response.Status.NOT_FOUND);
        } else if (customer == null) {
            throw new RestServiceException("No customer with id " + flightBooking.getCustomerId() +
                    " was found", Response.Status.NOT_FOUND);
        } else if (queryFlight != null) {
            throw new RestServiceException(flightBooking + " already exist.", Response.Status.CONFLICT);
        }
        flightBooking.setId(null);
        flightBooking.setFlight(flight);
        flightBooking.setCustomer(customer);
        try {
            flightBookingService.create(flightBooking);
        } catch (Exception e) {
            log.severe("FlightBookingRestService.createFlightBooking() -- create flight booking " +
                    "[" + flightBooking + "] error");
            e.printStackTrace();
            throw new RestServiceException(e.getMessage(), e);
        }
        return Response.ok(flightBooking).status(Response.Status.CREATED).build();
    }

    @DELETE
    @Path("/deleteFlightBooking/{id:[0-9]+}")
    @Operation(description = "Delete a flight booking object from the database")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "The flight booking has been successfully deleted"),
            @APIResponse(responseCode = "400", description = "Invalid flight booking id supplied"),
            @APIResponse(responseCode = "404", description = "Flight booking with id not found"),
            @APIResponse(responseCode = "500", description = "An unexpected error occurred whilst processing the request")
    })
    @Transactional
    public Response deleteFlightBooking(@PathParam("id") Long flightBookingId) {
        if (flightBookingId == null || flightBookingId <= 0) {
            throw new RestServiceException("Bad Request", Response.Status.BAD_REQUEST);
        }
        FlightBooking flightBooking = null;
        try {
            flightBooking = flightBookingService.findById(flightBookingId);
            if (flightBooking == null) {
                throw new RestServiceException("No flight booking with id " + flightBookingId +
                        " was found", Response.Status.NOT_FOUND);
            }
            flightBookingService.delete(flightBooking);
        } catch (Exception e) {
            log.severe("FlightBookingRestService.deleteFlightBooking() -- delete flight booking " +
                    "with id " + flightBookingId + " error");
            e.printStackTrace();
            throw new RestServiceException(e.getMessage(), e);
        }
        return Response.ok(flightBooking).build();
    }

}
