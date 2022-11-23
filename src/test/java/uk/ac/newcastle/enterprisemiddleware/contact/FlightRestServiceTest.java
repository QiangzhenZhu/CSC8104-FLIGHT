package uk.ac.newcastle.enterprisemiddleware.contact;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.h2.H2DatabaseTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import uk.ac.newcastle.enterprisemiddleware.coursework.flight.Flight;
import uk.ac.newcastle.enterprisemiddleware.coursework.flight.FlightRestService;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author jos
 * @date 2022/11/20 21:37:15
 * @description 航班接口服务测试类
 */

@QuarkusTest
@TestHTTPEndpoint(FlightRestService.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@QuarkusTestResource(H2DatabaseTestResource.class)
public class FlightRestServiceTest {
    private static Flight flight;

    @BeforeAll
    static void setup() {
        flight = new Flight();
        flight.setFlightNumber("AE886");
        flight.setDeparture("ABC");
        flight.setDestination("XYZ");
    }

    /**
     * 测试创建航班实体类型
     */
    @Test
    @Order(1)
    public void testCanCreateFlight() {
        given()
                .contentType(ContentType.JSON)
                .body(flight)
                .when()
                .post("/createFlight") // 请求访问的地址
                .then().statusCode(201);
    }

    /**
     * 测试获取航班实体类型
     */
    @Test
    @Order(2)
    public void testCanGetFlight() {
        // 测试检索所有客户接口
        Response response = given()
                .get("/findAllFlights")
                .then()
                .statusCode(200)
                .extract().response();
        Flight[] flights = response.body().as(Flight[].class);
        assertTrue(flights.length > 0);
        Flight firstFlight = flights[0];
        assertEquals(firstFlight.getFlightNumber(), flight.getFlightNumber());
        assertEquals(firstFlight.getDeparture(), flight.getDeparture());
        assertEquals(firstFlight.getDestination(), flight.getDestination());

        // 测试根据ID检索客户接口
        response = when()
                .get("/findFlightById/" + firstFlight.getId())
                .then()
                .statusCode(200)
                .extract().response();
        Flight secondFlight = response.body().as(Flight.class);
        assertEquals(firstFlight.getId(), secondFlight.getId());
        assertEquals(firstFlight.getFlightNumber(), secondFlight.getFlightNumber());
        assertEquals(firstFlight.getDeparture(), secondFlight.getDeparture());
        assertEquals(firstFlight.getDestination(), secondFlight.getDestination());
    }

    /**
     * 测试创建航班异常
     */
    @Test
    @Order(3)
    public void testCreateFlightException() {
        // 测试重复创建
        given()
                .contentType(ContentType.JSON)
                .body(flight)
                .when()
                .post("/createFlight") // 请求访问的地址
                .then().statusCode(409);

        // 测试创建参数错误
        Flight errorFlight = new Flight();
        errorFlight.setFlightNumber("AE006");
        errorFlight.setDestination("UK");
        errorFlight.setFlightNumber("QA");
        given()
                .contentType(ContentType.JSON)
                .body(flight)
                .when()
                .post("/createFlight") // 请求访问的地址
                .then().statusCode(409);
    }

    /**
     * 测试获取航班异常
     */
    @Test
    @Order(4)
    public void testGetFlightException() {
        // 测试

    }

}
