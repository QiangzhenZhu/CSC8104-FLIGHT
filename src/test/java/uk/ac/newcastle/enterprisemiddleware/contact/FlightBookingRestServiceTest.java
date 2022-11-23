package uk.ac.newcastle.enterprisemiddleware.contact;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.h2.H2DatabaseTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import uk.ac.newcastle.enterprisemiddleware.coursework.book.FlightBooking;
import uk.ac.newcastle.enterprisemiddleware.coursework.book.FlightBookingRestService;
import uk.ac.newcastle.enterprisemiddleware.coursework.customer.CustomerService;
import uk.ac.newcastle.enterprisemiddleware.coursework.flight.Flight;
import uk.ac.newcastle.enterprisemiddleware.coursework.flight.FlightService;

import javax.inject.Inject;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;
import java.util.Date;

import static io.restassured.RestAssured.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @description 航班预定接口服务类测试
 */

@QuarkusTest
@TestHTTPEndpoint(FlightBookingRestService.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@QuarkusTestResource(H2DatabaseTestResource.class)
public class FlightBookingRestServiceTest {

    private static FlightBooking flightBooking;

    @Inject
    FlightService flightService;

    @Inject
    CustomerService customerService;

    @Inject
    UserTransaction userTransaction;

    @BeforeAll
    static void setup() {
        /**
         * import.sql 已经插入了3条客户和3条航班的信息
         */
        flightBooking = new FlightBooking();
        flightBooking.setCustomerId(11111L);
        flightBooking.setFlightId(22222L);
        flightBooking.setBookingDate(new Date());
    }

    // 测试创建航班预定
    @Test
    @Order(1)
    public void testCanCreateFlightBooking() {
        given()
                .contentType(ContentType.JSON)
                .body(flightBooking)
                .when()
                .post("/createFlightBooking")
                .then().statusCode(201);
    }

    // 测试查询航班预定
    @Test
    @Order(2)
    public void testCanGetFlightBooking() {
        Response response = given()
                .get("/findAllBookings")
                .then()
                .statusCode(200)
                .extract().response();

        FlightBooking[] flightBookings = response.body().as(FlightBooking[].class);
        assertEquals(1, flightBookings.length);
        FlightBooking queryBooking = flightBookings[0];
        assertEquals(flightBooking.getCustomerId(), queryBooking.getCustomerId());
        assertEquals(flightBooking.getFlightId(), queryBooking.getFlightId());
        assertEquals(flightBooking.getBookingDate(), queryBooking.getBookingDate());

        flightBooking.setId(queryBooking.getId());
    }

    // 测试联级删除，即删除客户或航班后，自动删除对应的航班预订
    @Test
    @Order(4)
    public void testAssociatedBookingsDeleted() {
        // 先查询确定航班预约是存在的
        Response response = given()
                .get("/findBookingById/" + flightBooking.getId())
                .then().statusCode(200)
                .extract().response();
        FlightBooking firstQueryFlight = response.body().as(FlightBooking.class);
        assertNotNull(firstQueryFlight);

        try {
            // 删除对应的航班
            // 使用jpa删除操作必须加上事务
            userTransaction.begin();
            Flight delFlightBooking = flightService.findFlightById(flightBooking.getFlightId());
            flightService.delete(delFlightBooking);
            userTransaction.commit();
        } catch (Exception e) {
            try {
                userTransaction.rollback();
            } catch (SystemException ex) {
                throw new RuntimeException(ex);
            }
            throw new RuntimeException(e);
        }

        // 再查询航班预约，确定是否已级联删除
        response = given()
                .get("/findAllBookings/")
                .then().statusCode(200)
                .extract().response();
        FlightBooking[] flightBookings = response.body().as(FlightBooking[].class);
        assertEquals(0, flightBookings.length);
    }
}
