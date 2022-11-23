package uk.ac.newcastle.enterprisemiddleware.coursework.customer;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import uk.ac.newcastle.enterprisemiddleware.util.RestServiceException;

import javax.inject.Inject;
import javax.inject.Named;
import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.logging.Logger;

/**
 * @description 客户数据的接口类
 */

@Path("/api/customer")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class CustomerRestService {
    @Inject
    @Named("logger")
    Logger log;

    @Inject
    CustomerService service;

    @GET
    @Path("/findAllCustomers")
    @Operation(summary = "Fetch all customers", description = "Returns a JSON array of all stored customer objects.")
    public Response findAllCustomers() {
        List<Customer> customers = service.findAllCustomers();
        return Response.ok(customers).build();
    }

    @GET
    @Path("/findCustomerById/{id:[0-9]+}")
    @Operation(
            summary = "Fetch customer info by id.",
            description = "Returns a JSON representation of the customer object with the provided id.")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Customer found"),
            @APIResponse(responseCode = "404", description = "Customer with id not found")
    })
    public Response findCustomerById(@PathParam("id") Long id) {
        if (id == null || id <= 0) { // 参数校验
            throw new RestServiceException("Bad Request", Response.Status.BAD_REQUEST);
        }
        Customer customer = service.findById(id);
        if (customer == null) { // 查询不到航班
            throw new RestServiceException("No customer with the id " + id + " was found!", Response.Status.NOT_FOUND);
        } else {
            return Response.ok(customer).build();
        }
    }

    @GET
    @Path("findCustomerByEmail/{email}")
    @Operation(
            summary = "Fetch customer info by id.",
            description = "Returns a JSON representation of the customer object with the provided id.")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Customer found"),
            @APIResponse(responseCode = "404", description = "Customer with email not found")
    })
    public Response findCustomerByEmail(
            @PathParam("email")
            @Email /** 邮箱格式校验注解 */
                    String email) {
        Customer customer = service.findByEmail(email);
        if (customer == null) { // 查询不到客户
            throw new RestServiceException("No customer with the email " + email + " was found!", Response.Status.NOT_FOUND);
        } else {
            return Response.ok(customer).build();
        }
    }

    @POST
    @Path("/createCustomer")
    @Operation(
            summary = "post a new customer",
            description = "posts a new customer object to the database.")
    @APIResponses(value = {
            @APIResponse(responseCode = "201", description = "Customer created successfully."),
            @APIResponse(responseCode = "400", description = "Invalid customer object supplied in request body"),
            @APIResponse(responseCode = "500", description = "An unexpected error occurred whilst processing the request")
    })
    @Transactional
    public Response createCustomer(
            @Valid // 用于参数校验的注解
            Customer customer) {
        if (customer == null) {
            log.severe("customer can't be null");
            throw new RestServiceException("Bad Request", Response.Status.BAD_REQUEST);
        }
        try {
            customer.setId(null);
            service.createCustomer(customer);
        } catch (Exception e) {
            log.severe(e.getMessage());
            throw new RestServiceException(e.getMessage());
        }

        return Response.ok(customer).status(Response.Status.CREATED).build();
    }

    @DELETE
    @Path("/deleteCustomer/{id:[0-9]+}")
    @Operation(description = "Delete a customer object from the database")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "The customer has been successfully deleted"),
            @APIResponse(responseCode = "400", description = "Invalid customer id supplied"),
            @APIResponse(responseCode = "404", description = "Customer with id not found"),
            @APIResponse(responseCode = "500", description = "An unexpected error occurred whilst processing the request")
    })
    @Transactional
    public Response deleteCustomer(@PathParam("id") Long id) {
        if (id == null || id <= 0) {
            throw new RestServiceException("Bad Request", Response.Status.BAD_REQUEST);
        }
        Customer customer = service.findById(id);
        if (customer == null) {
            throw new RestServiceException(
                    "No customer with the id " + id + " was found!",
                    Response.Status.NOT_FOUND);
        }


        Customer delCustomer = null;
        try {
            delCustomer = service.deleteCustomer(customer);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        log.info("deleteContact completed. customer = " + customer);
        return Response.ok(delCustomer).build();
    }
}
