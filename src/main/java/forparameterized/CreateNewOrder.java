package forparameterized;

import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class CreateNewOrder {
    private static final String URL = "http://qa-scooter.praktikum-services.ru/";
    private static final String OAUTH2 = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJfaWQiOiI2MjUxNWIzMzA0NmM5ZDAwM2QxNmVlY2UiLCJpYXQiOjE2NTMwNjk0NDEsImV4cCI6MTY1MzY3NDI0MX0.-oDRbCaVGmC2Z8CZ427MBV3SV1WlT__wfcuF9qe22Q0";

    public static Response createOrder(Order order) {
        return given().contentType(ContentType.JSON)
                .auth().oauth2(OAUTH2).and().body(order).when()
                .post(URL + "api/v1/orders");
    }
}
