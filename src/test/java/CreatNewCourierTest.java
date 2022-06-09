import forcourier.Courier;
import forcourier.CourierLogin;
import forcourier.CouriersAPI;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CreatNewCourierTest {
    String courierId;
    Courier courier;
    Courier courierDooble;
    CouriersAPI couriersAPI;
    //на сайте "https://qa-scooter.praktikum-services.ru/docs/#api-Courier-CreateCourier" написано, что в переменной messageCode409
    //должен находится текст "Этот логин уже используется". Попробовала в Postman был ответ, который я и указала в коде
    String messageCode409 = "Этот логин уже используется. Попробуйте другой.";
    String messageCode400 = "Недостаточно данных для создания учетной записи";

    @Before
    public void setUp() {
        courier = Courier.getRandomCourier();
        courierDooble = courier;
    }

    @After
    public void delete() {
        CouriersAPI.deleteCouriers(courierId).body().asString();
    }

    @Test
    public void checkCode201() { //проверяем успешный запрос и возврат кода
        //создание курьера
        Response response = CouriersAPI.createCourier(courier);
        //проверяем, что код 201
        assertEquals(SC_CREATED, response.statusCode());
        //Вычисляем id нашего курьера
        CourierLogin courierLogin = new CourierLogin(courier.getLogin(), courier.getPassword());
        Response responseLogin = CouriersAPI.loginCouriers(courierLogin);
        courierId = responseLogin.jsonPath().getString("id");
    }

    @Test
    public void checkResponseOk() { //проверяем успешный запрос и возврат ответа
        //создание курьера
        Response response = CouriersAPI.createCourier(courier);
        //проверяем, что вернулся ответ OK
        assertTrue(response.body().jsonPath().getBoolean("ok"));
        //Вычисляем id нашего курьера
        CourierLogin courierLogin = new CourierLogin(courier.getLogin(), courier.getPassword());
        Response responseLogin = CouriersAPI.loginCouriers(courierLogin);
        courierId = responseLogin.jsonPath().getString("id");
    }

    @Test
    public void checkCodeDoobleLogin() { //проверяем не успешный запрос и код с существующим логином
        //создание курьера
        CouriersAPI.createCourier(courier);
        Response responseDooble = CouriersAPI.createCourier(courierDooble);
        //проверяем, что код 409
        assertEquals(SC_CONFLICT, responseDooble.statusCode());
        //Вычисляем id нашего курьера
        CourierLogin courierLogin = new CourierLogin(courier.getLogin(), courier.getPassword());
        Response responseLogin = CouriersAPI.loginCouriers(courierLogin);
        courierId = responseLogin.jsonPath().getString("id");
    }

    @Test
    public void checkResponseDoobleLogin() { //проверяем не успешный запрос и возврат ответа
        //создание курьера
        CouriersAPI.createCourier(courier);
        Response responseDooble = CouriersAPI.createCourier(courierDooble);
        //проверяем, какой ответ вернулся с кодом 409
        assertEquals(messageCode409, responseDooble.body().jsonPath().getString("message"));
        //Вычисляем id нашего курьера
        CourierLogin courierLogin = new CourierLogin(courier.getLogin(), courier.getPassword());
        Response responseLogin = CouriersAPI.loginCouriers(courierLogin);
        courierId = responseLogin.jsonPath().getString("id");
    }

    @Test
    public void checkCodeDefectLogin() { //проверяем не успешный запрос и код с нехваткой данных
        //создание курьера
        courier.setLogin(null);
        Response response = CouriersAPI.createCourier(courier);
        //проверяем, что код 400
        assertEquals(SC_BAD_REQUEST, response.statusCode());
    }

    @Test
    public void checkResponseDefectLogin() { //проверяем не успешный запрос и ответ с нехваткой данных
        //создание курьера
        courier.setLogin(null);
        Response response = CouriersAPI.createCourier(courier);
        //проверяем, какой ответ вернулся с кодом 400
        assertEquals(messageCode400, response.body().jsonPath().getString("message"));
    }
}