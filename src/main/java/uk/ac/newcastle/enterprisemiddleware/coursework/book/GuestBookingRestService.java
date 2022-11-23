package uk.ac.newcastle.enterprisemiddleware.coursework.book;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import uk.ac.newcastle.enterprisemiddleware.coursework.customer.Customer;
import uk.ac.newcastle.enterprisemiddleware.coursework.customer.CustomerService;
import uk.ac.newcastle.enterprisemiddleware.coursework.flight.Flight;
import uk.ac.newcastle.enterprisemiddleware.coursework.flight.FlightService;
import uk.ac.newcastle.enterprisemiddleware.util.RestServiceException;

import javax.inject.Inject;
import javax.inject.Named;
import javax.transaction.SystemException;
import javax.transaction.Transactional;
import javax.transaction.UserTransaction;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Objects;
import java.util.logging.Logger;

/**
 * @description 交易端点接口服务类
 */

@Path("/api/guestBooking")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class GuestBookingRestService {

    @Inject
    @Named("logger")
    Logger log;

    @Inject
    CustomerService customerService;

    @Inject
    FlightBookingService flightBookingService;

    @Inject
    FlightService flightService;

    // 要求必须使用 UserTransaction
    @Inject
    UserTransaction userTransaction;

    @POST
    @Operation(description = "Add a new guestBooking to the database")
    @APIResponses(value = {
            @APIResponse(responseCode = "201", description = "guestBooking created successfully."),
            @APIResponse(responseCode = "400", description = "Invalid guestBooking supplied in request body"),
            @APIResponse(responseCode = "500", description = "An unexpected error occurred whilst processing the request")
    })
    // @Transactional // 注释掉，不能重复启用事务
    public Response createGuestBooking(
            @Parameter(description = "JSON representation of guestBooking object to be added to the database", required = true)
            @Valid /** 进行递归校验 */
                    GuestBooking guestBooking) {

        if (guestBooking == null) {
            throw new RestServiceException("Bad Request", Response.Status.BAD_REQUEST);
        }

        Response resp = null;

        try {

            // 事务开始
            userTransaction.begin();

            Customer customer = guestBooking.getCustomer();
            Customer tempCustomer = customerService.findByEmail(customer.getEmail());
            // 不存在则创建新的客户
            if (tempCustomer == null) {
                customer.setId(null);
                tempCustomer = customerService.createCustomer(customer);
            }


            FlightBooking flightBooking = guestBooking.getFlightBooking();

            flightBooking.setId(null);
            flightBooking.setCustomerId(tempCustomer.getId());
            flightBookingService.create(flightBooking);

            resp = Response.status(Response.Status.CREATED).entity(flightBooking).build();

            // 事务提交
            userTransaction.commit();


        } catch (Exception e) {
            log.severe(e.getMessage());
            try {
                userTransaction.rollback();
            } catch (SystemException ex) {
                throw new RestServiceException(ex.getMessage());
            }
            throw new RestServiceException(e.getMessage());
        }

        return resp;
    }
}
