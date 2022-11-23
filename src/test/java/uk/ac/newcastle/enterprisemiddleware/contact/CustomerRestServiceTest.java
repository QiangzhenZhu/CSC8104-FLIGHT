package uk.ac.newcastle.enterprisemiddleware.contact;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.h2.H2DatabaseTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import uk.ac.newcastle.enterprisemiddleware.coursework.customer.Customer;
import uk.ac.newcastle.enterprisemiddleware.coursework.customer.CustomerRestService;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @description 客户接口类测试
 */

@QuarkusTest
@TestHTTPEndpoint(CustomerRestService.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@QuarkusTestResource(H2DatabaseTestResource.class)
public class CustomerRestServiceTest {
    private static Customer customer;

    @BeforeAll
    static void setup() {
        customer = new Customer();
        customer.setEmail("pdd8863@newc.com");
        customer.setName("pdd");
        customer.setPhoneNumber("09876543210");
    }

    /**
     * 测试创建客户实体类型
     */
    @Test
    @Order(1)
    public void testCanCreateCustomer() {
        given()
                .contentType(ContentType.JSON)
                .body(customer)
                .when()
                .post("/createCustomer") // 请求访问的地址
                .then().statusCode(201);
    }

    /**
     * 测试获取客户实体类型
     */
    @Test
    @Order(2)
    public void testCanGetCustomer() {
        // 测试检索所有客户接口
        Response response = given()
                .get("/findAllCustomers")
                .then()
                .statusCode(200)
                .extract().response();
        Customer[] customers = response.body().as(Customer[].class);
        assertEquals(4, customers.length);
        Customer firstCustomer = customers[0];
        assertEquals(firstCustomer.getName(), customer.getName());
        assertEquals(firstCustomer.getEmail(), customer.getEmail());
        assertEquals(firstCustomer.getPhoneNumber(), customer.getPhoneNumber());

        // 测试根据ID检索客户接口
        response = when()
                .get("/findCustomerById/" + firstCustomer.getId())
                .then()
                .statusCode(200)
                .extract().response();
        Customer secondCustomer = response.body().as(Customer.class);
        assertEquals(firstCustomer.getName(), secondCustomer.getName());
        assertEquals(firstCustomer.getEmail(), secondCustomer.getEmail());
        assertEquals(firstCustomer.getPhoneNumber(), secondCustomer.getPhoneNumber());
    }

    /**
     * 测试创建客户异常
     */
    @Test
    @Order(3)
    public void testCreateCustomerException() {
        // 测试重复创建
        given()
                .contentType(ContentType.JSON)
                .body(customer)
                .when()
                .post("/createCustomer") // 请求访问的地址
                .then().statusCode(500);

        // 测试创建参数错误
        Customer errorCustomer = new Customer();
        errorCustomer.setName("pdd006");
        errorCustomer.setEmail("apoll@newcastle.");
        errorCustomer.setPhoneNumber("09988776655");
        given()
                .contentType(ContentType.JSON)
                .body(customer)
                .when()
                .post("/createCustomer") // 请求访问的地址
                .then().statusCode(500);
    }

    /**
     * 测试获取客户异常
     */
    @Test
    @Order(4)
    public void testGetCustomerException() {
        // 测试

    }
}
