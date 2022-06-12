import fororders.Order;
import fororders.OrderApi;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.SC_OK;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;


public class OrdersTest {
    Order order;

    @Before
    public void setUp() {
        order = Order.getRandomOrder();
    }

    @Test
    public void checkCodeOrder() {
        Response listOrder = OrderApi.getListOrders();
        assertEquals(SC_OK, listOrder.statusCode());
    }

    @Test
    public void checkTextOrder() {
        Response listOrder = OrderApi.getListOrders();
        assertNotNull(listOrder);
    }
}
