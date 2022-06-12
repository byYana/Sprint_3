import forcourier.Courier;
import forcourier.CourierLogin;
import forcourier.CouriersAPI;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class LoginTest {
    String courierId;
    Courier courier;
    String messageCode404 = "Учетная запись не найдена";
    String messageCode400 = "Недостаточно данных для входа";

    @Before
    public void setUp() {
        courier = Courier.getRandomCourier();
    }

    @After
    public void delete() {
        CouriersAPI.deleteCouriers(courierId).body().asString();
    }

    @Test
    public void checkCode200() { //проверяем успешный запрос и возврат кода
        //создание курьера
        CouriersAPI.createCourier(courier);
        //Вычисляем id нашего курьера
        CourierLogin courierLogin = new CourierLogin(courier.getLogin(), courier.getPassword());
        Response responseLogin = CouriersAPI.loginCouriers(courierLogin);
        //проверяем, что код 200
        assertEquals(SC_OK, responseLogin.statusCode());
        courierId = responseLogin.jsonPath().getString("id");
    }

    @Test
    public void checkResponseId() { //проверяем успешный запрос и возврат ответа
        //создание курьера
        CouriersAPI.createCourier(courier);
        //Вычисляем id нашего курьера
        CourierLogin courierLogin = new CourierLogin(courier.getLogin(), courier.getPassword());
        Response responseLogin = CouriersAPI.loginCouriers(courierLogin);
        courierId = responseLogin.jsonPath().getString("id");
        //проверяем, что вернулся ответ не равный Null
        assertNotNull(courierId);
    }

    @Test
    public void checkCodeNotExistingLogin() { //проверяем не успешный запрос и код с не существующим логином
        //Вычисляем id нашего курьера
        CourierLogin courierLogin = new CourierLogin(courier.getLogin(), courier.getPassword());
        Response responseLogin = CouriersAPI.loginCouriers(courierLogin);
        assertEquals(SC_NOT_FOUND, responseLogin.statusCode());
    }

    @Test
    public void checkResponseNotExistingLogin() { //проверяем не успешный запрос и возврат ответа с не существующим логином
        //Вычисляем id нашего курьера
        CourierLogin courierLogin = new CourierLogin(courier.getLogin(), courier.getPassword());
        Response responseLogin = CouriersAPI.loginCouriers(courierLogin);
        assertEquals(messageCode404, responseLogin.body().jsonPath().getString("message"));

    }

    @Test
    public void checkCodeDefectLogin() { //проверяем не успешный запрос и код с нехваткой данных
        //создание курьера
        CouriersAPI.createCourier(courier);
        courier.setLogin(null);
        //Вычисляем id нашего курьера
        CourierLogin courierLogin = new CourierLogin(courier.getLogin(), courier.getPassword());
        Response responseLogin = CouriersAPI.loginCouriers(courierLogin);
        //проверяем, что код 400
        assertEquals(SC_BAD_REQUEST, responseLogin.statusCode());
    }

    @Test
    public void checkResponseDefectLogin() { //проверяем не успешный запрос и ответ с нехваткой данных
        //создание курьера
        CouriersAPI.createCourier(courier);
        courier.setLogin(null);
        //Вычисляем id нашего курьера
        CourierLogin courierLogin = new CourierLogin(courier.getLogin(), courier.getPassword());
        Response responseLogin = CouriersAPI.loginCouriers(courierLogin);
        //проверяем, какой ответ вернулся с кодом 400
        assertEquals(messageCode400, responseLogin.body().jsonPath().getString("message"));
    }
}
