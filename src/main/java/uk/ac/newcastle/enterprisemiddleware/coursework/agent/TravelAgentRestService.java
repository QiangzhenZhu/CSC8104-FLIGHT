package uk.ac.newcastle.enterprisemiddleware.coursework.agent;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import uk.ac.newcastle.enterprisemiddleware.coursework.book.*;
import uk.ac.newcastle.enterprisemiddleware.coursework.customer.Customer;
import uk.ac.newcastle.enterprisemiddleware.coursework.customer.CustomerService;
import uk.ac.newcastle.enterprisemiddleware.util.RestServiceException;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.logging.Logger;

/**
 * @description 旅行社接口服务类
 */

@Path("/api/travelAgent")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class TravelAgentRestService {

    @Inject
    Logger log;

    @Inject
    FlightBookingService flightBookingService;

    @Inject
    CustomerService customerservice;

    @Inject
    TravelAgentBookingService travelAgentBookingService;

    // @Inject
    // TravelAgentBookingRepository travelAgentBookingRepository;

    @RestClient
    TaxiBookingService taxiBookingService;

    @RestClient
    HotelBookingService hotelBookingService;

    @GET
    @Path("/findAllBookings")
    @Operation(
            summary = "Fetch all TravelAgentBooking",
            description = "Returns a JSON array of all stored TravelAgentBooking objects.")
    public Response findAllBookings() {
        List<TravelAgentBooking> travelAgentBookings = travelAgentBookingService.findAllBookings();
        return Response.ok(travelAgentBookings).build();
    }

    @GET
    @Path("/findByCustomerId/{id:[0-9]+}")
    @Operation(summary = "Fetch all TravelAgent", description = "Returns a JSON array of all stored TravelAgent objects.")
    public Response findBookingsByCustomerId(
            @PathParam("id")
            Long customerId) {

        if (customerId == null || customerId <= 0) {
            throw new RestServiceException("Bad Request", Response.Status.BAD_REQUEST);
        }
        Customer customer = customerservice.findById(customerId);
        if (customer == null) {
            throw new RestServiceException("No Customer with the customerId " + customerId + " was found!", Response.Status.NOT_FOUND);
        }

        List<TravelAgentBooking> travelAgentBookings =
                travelAgentBookingService.findAllByCustomerId(customerId);

        return Response.ok(travelAgentBookings).build();
    }

    @DELETE
    @Path("/deleteBookingById/{id:[0-9]+}")
    @Operation(description = "Delete TravelAgentBooking to the database")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "TravelAgent delete successfully.")
    })
    @Transactional
    public Response deleteTravelAgentBooking(@PathParam("id") Long id) {
        TravelAgentBooking travelAgentBooking = travelAgentBookingService.findById(id);
        Customer customer = customerservice.findById(travelAgentBooking.getCustomerId());
        if (customer == null) {
            throw new RestServiceException("TravelAgent not exsit", Response.Status.BAD_REQUEST);
        }
        Response response;

        try {
            taxiBookingService.deleteTaxiBooking(travelAgentBooking.getTaxiBookingId());
            hotelBookingService.deleteHotelBooking(travelAgentBooking.getHotelBookingId());
            FlightBooking flightBooking =
                    flightBookingService.findById(travelAgentBooking.getFlightBookingId());
            flightBookingService.delete(flightBooking);
            travelAgentBookingService.deleteTravelAgentBooking(travelAgentBooking);

            response = Response.ok(travelAgentBooking).build();

        } catch (Exception e) {
            //Handle bean validation issues
            log.severe(e.getMessage());
            throw new RestServiceException(e.getMessage(), e);
        }
        return response;
    }

    @POST
    @Path("/createBooking")
    @Operation(description = "Add a new TravelAgent to the database")
    @Transactional
    public Response createTravelAgentBooking(
            @Valid
            TravelAgent travelAgent) {

        if (travelAgent == null) {
            throw new RestServiceException("Bad Request", Response.Status.BAD_REQUEST);
        }

        Response resp;
        boolean isTaxiBookingSuccess = false;
        boolean isHotelBookingSuccess = false;
        TaxiBooking taxiBooking = null;
        HotelBooking hotelBooking = null;

        try {
            log.info(travelAgent.toString());

            // 预约航班
            // 没必要记录航班的申请情况，因为失败了会自动回滚
            FlightBooking flightBooking = travelAgent.getFlightBooking();
            flightBooking.setId(null);
            flightBookingService.create(flightBooking);
            log.info("------------------ flight booking successful ------------------");

            // 预约出租车
            taxiBooking = taxiBookingService.createTaxiBooking(travelAgent.getTaxiBooking());
            if (taxiBooking == null) {
                throw new RestServiceException("taxi booking error");
            }
            isTaxiBookingSuccess = true;
            log.info("------------------- taxi booking successful -------------------");

            // 预约酒店
            hotelBooking = hotelBookingService.createHotelBooking(travelAgent.getHotelBooking());
            if (hotelBooking == null) {
                throw new RestServiceException("hotel booking error");
            }
            isHotelBookingSuccess = true;
            log.info("------------------- hotel booking successful ------------------");

            // 将旅游预约写入数据库
            TravelAgentBooking travelAgentBooking = new TravelAgentBooking();
            travelAgentBooking.setCustomerId(travelAgent.getCustomer().getId());
            travelAgentBooking.setFlightBookingId(flightBooking.getId());
            travelAgentBooking.setHotelBookingId(hotelBooking.getBookingId());
            travelAgentBooking.setTaxiBookingId(taxiBooking.getId());
            travelAgentBookingService.createTravelAgentBooking(travelAgentBooking);

            resp = Response.status(Response.Status.CREATED).entity(travelAgentBooking).build();

        } catch (Exception e) {
            e.printStackTrace();
            if (isHotelBookingSuccess) {
                // 回滚酒店数据
                log.info("delete hotel booking " + hotelBooking);
                hotelBookingService.deleteHotelBooking(hotelBooking.getBookingId());
            }
            if (isTaxiBookingSuccess) {
                // 回滚出租数据
                log.info("delete taxi booking " + taxiBooking);
                taxiBookingService.deleteTaxiBooking(taxiBooking.getId());
            }
            throw new RestServiceException(e.getMessage());
        }
        return resp;
    }
}
