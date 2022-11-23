package uk.ac.newcastle.enterprisemiddleware.coursework.flight;


import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.jboss.resteasy.reactive.Cache;
import uk.ac.newcastle.enterprisemiddleware.util.RestServiceException;

import javax.inject.Inject;
import javax.inject.Named;
import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;


@Path("/api/flight")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class FlightRestService {
    @Inject
    @Named("logger")
    Logger log;

    @Inject
    FlightService service;

    @GET
    @Path("/findAllFlights")
    @Operation(summary = "Fetch all flights", description = "Returns a JSON array of all stored flight objects.")
    public Response findAllFlights() {
        List<Flight> flights;
        flights = service.findAllFlights();
        return Response.ok(flights).build();
    }

    @POST
    @Path("/createFlight")
    @Operation(
            summary = "post a new flight",
            description = "posts a new flight object to the database.")
    @APIResponses(value = {
            @APIResponse(responseCode = "201", description = "Flight created successfully."),
            @APIResponse(responseCode = "400", description = "Invalid flight object supplied in request body"),
            @APIResponse(responseCode = "409", description = "flight object already exist in databases"),
            @APIResponse(responseCode = "500", description = "An unexpected error occurred whilst processing the request")
    })
    @Transactional
    public Response createFlight(
            @Valid // 用于参数校验的注解
            Flight flight) {
        if (flight == null) {
            throw new RestServiceException("Bad Request", Response.Status.BAD_REQUEST);
        }
        if (service.findByFlightNumber(flight.getFlightNumber()) != null) {
            throw new RestServiceException("Flight Conflict", Response.Status.CONFLICT);
        }
        Response.ResponseBuilder builder;
        try {
            flight.setId(null);
            service.create(flight);
        } catch (Exception e) {
            log.severe(e.getMessage());
            throw new RestServiceException(e.getMessage());
        }

        return Response.ok(flight).status(Response.Status.CREATED).build();
    }

    @GET
    @Path("/findFlightById/{id:[0-9]+}")
    @Operation(
            summary = "Fetch a flight by id",
            description = "Returns a JSON representation of the flight object with the provided id."
    )
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Flight found"),
            @APIResponse(responseCode = "404", description = "Flight with id not found")
    })
    public Response findFlightById(@PathParam("id") Long id) {
        if (id == null || id <= 0) { // 参数校验
            throw new RestServiceException("Bad Request", Response.Status.BAD_REQUEST);
        }
        Flight flight = service.findFlightById(id);
        if (flight == null) { // 查询不到航班
            throw new RestServiceException("No flight with the id " + id + " was found!", Response.Status.NOT_FOUND);
        } else {
            return Response.ok(flight).build();
        }
    }

    // 要求是不能删除客户和航班的记录，所以该接口应该去掉
    @DELETE
    @Path("/deleteFlight/{id:[0-9]+}")
    @Operation(description = "Delete a Flight object from the database")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "The Flight has been successfully deleted"),
            @APIResponse(responseCode = "400", description = "Invalid flight id supplied"),
            @APIResponse(responseCode = "404", description = "flight with id not found"),
            @APIResponse(responseCode = "500", description = "An unexpected error occurred whilst processing the request")
    })
    @Transactional
    public Response deleteFlight(@PathParam("id") Long id) {
        if (id == null || id <= 0) { // 参数校验
            throw new RestServiceException("Bad Request", Response.Status.BAD_REQUEST);
        }
        Flight delFlight = service.findFlightById(id);
        if (delFlight == null) {
            // Verify that the contact exists. Return 404, if not present.
            throw new RestServiceException("No Flight with the id " + id + " was found!", Response.Status.NOT_FOUND);
        }

        Flight flight = null;

        try {
            flight = service.delete(delFlight);
        } catch (Exception e) {
            log.severe(e.getMessage());
            throw new RestServiceException(e.getMessage(), e);
        }
        log.info("deleteFlight completed. flight = " + flight.toString());
        return Response.ok().entity(flight).build();
    }
}
