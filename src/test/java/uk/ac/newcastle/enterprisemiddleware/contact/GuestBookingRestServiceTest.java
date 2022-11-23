package uk.ac.newcastle.enterprisemiddleware.contact;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.h2.H2DatabaseTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.*;
import uk.ac.newcastle.enterprisemiddleware.coursework.book.FlightBooking;
import uk.ac.newcastle.enterprisemiddleware.coursework.book.FlightBookingService;
import uk.ac.newcastle.enterprisemiddleware.coursework.book.GuestBooking;
import uk.ac.newcastle.enterprisemiddleware.coursework.book.GuestBookingRestService;
import uk.ac.newcastle.enterprisemiddleware.coursework.customer.Customer;
import uk.ac.newcastle.enterprisemiddleware.coursework.customer.CustomerService;
import uk.ac.newcastle.enterprisemiddleware.coursework.flight.FlightService;

import javax.inject.Inject;
import javax.transaction.UserTransaction;
import java.util.Date;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author jos
 * @date 2022/11/21 17:07:03
 * @description 交易站点接口服务类测试
 */

@QuarkusTest
@TestHTTPEndpoint(GuestBookingRestService.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@QuarkusTestResource(H2DatabaseTestResource.class)
public class GuestBookingRestServiceTest {

    private static Customer customer;
    private static FlightBooking flightBooking;
    private static GuestBooking guestBooking;

    private static Customer errorCustomer;
    private static FlightBooking errorFlightBooking;
    private static GuestBooking errorGuestBooking;

    @Inject
    UserTransaction userTransaction;

    @Inject
    CustomerService customerService;

    @Inject
    FlightBookingService flightBookingService;

    @BeforeAll
    static void setup() {
        customer = new Customer();
        customer.setName("bob");
        customer.setEmail("bob@newcastle.uk");
        customer.setPhoneNumber("07773214329");

        errorCustomer = new Customer();
        errorCustomer.setName("taylor");
        errorCustomer.setEmail("taylor@newcastle.uk");
        errorCustomer.setPhoneNumber("07773210009");

        flightBooking = new FlightBooking();
        flightBooking.setCustomerId(1L);
        flightBooking.setFlightId(22222L);
        flightBooking.setBookingDate(new Date());

        errorFlightBooking = new FlightBooking();
        errorFlightBooking.setCustomerId(1L);
        errorFlightBooking.setFlightId(null);
        errorFlightBooking.setBookingDate(new Date());

        guestBooking = new GuestBooking();
        guestBooking.setCustomer(customer);
        guestBooking.setFlightBooking(flightBooking);

        errorGuestBooking = new GuestBooking();
        errorGuestBooking.setCustomer(errorCustomer);
        errorGuestBooking.setFlightBooking(errorFlightBooking);
    }

    // 测试添加GuestBooking
    // @Test
    // @Order(1)
    public void testCanCreateGuestBooking() {
        given()
                .contentType(ContentType.JSON)
                .body(guestBooking)
                .when()
                .post()
                .then().statusCode(201);

        // 测试新客户是否已插入
        Customer addCustomer = customerService.findByEmail(customer.getEmail());
        assertNotNull(addCustomer);
        assertEquals(addCustomer.getName(), customer.getName());
        assertEquals(addCustomer.getEmail(), customer.getEmail());
        assertEquals(addCustomer.getPhoneNumber(), customer.getPhoneNumber());

        // 测试新航班预定是否已插入
        FlightBooking addFlightBooking = flightBookingService.findByFlightIdAndDate(
                flightBooking.getFlightId(),
                flightBooking.getBookingDate());
        assertNotNull(addFlightBooking);
        assertEquals(addFlightBooking.getCustomerId(), flightBooking.getCustomerId());
        assertEquals(addFlightBooking.getFlightId(), flightBooking.getFlightId());
        assertEquals(addFlightBooking.getBookingDate().getTime(),
                flightBooking.getBookingDate().getTime());
    }

    // 测试提供一个有效的客户但无效的预订的GuestBooking端点，客户是否存在于数据库中
    @Test
    @Order(2)
    public void testCreateErrorGuestBooking() {
        given()
                .contentType(ContentType.JSON)
                .body(errorGuestBooking)
                .when()
                .post()
                .then().statusCode(500);

        // 测试新客户是否已插入
        Customer addCustomer = customerService.findByEmail(errorCustomer.getEmail());
        assertNull(addCustomer);
    }
}
