package siit.integration.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import siit.integration.ITBase;
import siit.integration.TestDataUtil;
import siit.model.OrderProduct;
import siit.model.Product;
import siit.service.CustomerService;
import siit.service.OrderService;

import java.math.BigDecimal;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class CustomerServiceIT extends ITBase {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private TestDataUtil testDataUtil;

    @Test
    void smokeTest() {
        assertThat(customerService, is(notNullValue()));
        assertThat(customerService.getCustomers(), hasSize(1));
    }

    @Test
    void addOrderProductShouldAddOrderProductInDatabase() {
        OrderProduct orderProduct = getOrderProduct("Roasted beans");

        orderService.addProductToOrder(orderProduct);

        Map<String, Object> dbOrderProduct = getDbOrderProduct(orderProduct);

        assertThat(dbOrderProduct, is(notNullValue()));
        assertThat(dbOrderProduct.get("QUANTITY"), is(BigDecimal.valueOf(88)));
    }

    @Test
    void addOrderProductShouldLoadOrderProductFromDatabase() {
        OrderProduct orderProduct = getOrderProduct("Roasted beans");

        OrderProduct returnedOrderProduct = orderService.addProductToOrder(orderProduct);

        assertThat(returnedOrderProduct.getProduct().getName(), is("Roasted beans"));
        assertThat(returnedOrderProduct.getProduct().getPrice(), is(BigDecimal.valueOf(11)));
        assertThat(returnedOrderProduct.getProduct().getWeight(), is(BigDecimal.valueOf(600)));
    }

    @Test
    void addOrderProductWithAlreadyExistingProductShouldAddQuantity() {
        OrderProduct orderProduct = getOrderProduct("Halogen lights");

        //customerService.addOrderProduct(orderProduct);
        orderService.addProductToOrder(orderProduct);

        Map<String, Object> dbOrderProduct = getDbOrderProduct(orderProduct);
        assertThat(dbOrderProduct.get("QUANTITY"), is(BigDecimal.valueOf(92)));
    }

    private OrderProduct getOrderProduct(String productName) {
        OrderProduct orderProduct = new OrderProduct();
        orderProduct.setOrderId(testDataUtil.getOrderByNumber("FNR982774"));
        orderProduct.setQuantity(BigDecimal.valueOf(88));

        Product product = new Product();
        product.setId(testDataUtil.getProductIdByName(productName));
        orderProduct.setProduct(product);
        return orderProduct;
    }

    private Map<String, Object> getDbOrderProduct(OrderProduct orderProduct) {
        return jdbcTemplate.queryForMap(
                "SELECT * FROM ORDERS_PRODUCTS WHERE ORDER_ID = ? AND PRODUCT_ID = ?",
                orderProduct.getOrderId(), orderProduct.getProduct().getId());
    }

}
