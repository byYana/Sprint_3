import forparameterized.CreateNewOrder;
import forparameterized.Order;
import io.restassured.response.Response;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static org.apache.http.HttpStatus.SC_CREATED;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(Parameterized.class)
public class CreateNewOrderTest {
    private static final String[] colorNull = {};
    private static final String[] colorBlack = {"BLACK"};
    private static final String[] colorGrey = {"GREY"};
    private static final String[] colorAll = {"BLACK", "GREY"};
    String[] color;
    private final String firstName = "Naruto";
    private final String lastName = "Uchiha";
    private final String address = "Konoha, 142 apt.";
    private final String metroStation = "4";
    private final String phone = "+7 800 355 35 35";
    private final String rentTime = "5";
    private final String deliveryDate = "2020-06-06";
    private final String comment = "Saske, come back to Konoha";

    public CreateNewOrderTest(String[] color) {
        this.color = color;
    }

    @Parameterized.Parameters
    public static Object[][] colorForTest() {
        return new Object[][]{{colorNull}, {colorBlack}, {colorGrey}, {colorAll}};
    }

    @Test
    public void checkCode() {
        Order order = new Order(firstName, lastName, address, metroStation, phone, rentTime, deliveryDate, comment, color);
        Response response = CreateNewOrder.createOrder(order);
        assertEquals(SC_CREATED, response.statusCode());
    }

    @Test
    public void checkTrack() {
        Order order = new Order(firstName, lastName, address, metroStation, phone, rentTime, deliveryDate, comment, color);
        Response response = CreateNewOrder.createOrder(order);
        assertNotNull(response.body().jsonPath().getString("track"));
    }
}
