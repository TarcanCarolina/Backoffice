package siit.unit.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import siit.db.OrderDao;
import siit.db.ProductDao;
import siit.model.OrderProduct;
import siit.model.Product;
import siit.service.CustomerService;
import siit.service.OrderService;

import java.math.BigDecimal;
import java.util.Collections;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

@ExtendWith(MockitoExtension.class)
public class CustomerServiceTest {

    @InjectMocks
    private CustomerService customerService = new CustomerService();

    @InjectMocks
    private OrderService orderService = new OrderService();

    @Mock
    private OrderDao orderDao;

    @Mock
    private ProductDao productDao;

    @Test
    void addOrderProductShouldAddOrderProductInDAO() {

        OrderProduct orderProduct = getOrderProduct(66);

        Mockito.when(orderDao.getOrderProductsForOrder(12L)).thenReturn(emptyList());

        //customerService.addOrderProduct(orderProduct);

        Mockito.verify(orderDao).addProductToOrder(orderProduct);
    }

    @Test
    void addOrderProductShouldLoadOrderProductFromDAO() {
        OrderProduct clientOrderProduct = getOrderProduct(66);

        OrderProduct daoOrderProduct1 = getOrderProduct(66);
        OrderProduct daoOrderProduct2 = getOrderProduct(67);
        Mockito.when(orderDao.getOrderProductsForOrder(12L))
                .thenReturn(asList(daoOrderProduct1, daoOrderProduct2));

        Product daoProduct = new Product();
        daoProduct.setId(66);
        daoProduct.setName("Test Product 66");
        Mockito.when(productDao.findById(66))
                .thenReturn(daoProduct);

       // OrderProduct returnedOrderProduct = customerService.addOrderProduct(clientOrderProduct);
        OrderProduct returnedOrderProduct = orderService.addProductToOrder(clientOrderProduct);

        assertThat(returnedOrderProduct, is(daoOrderProduct1));
        assertThat(returnedOrderProduct.getProduct(), is(daoProduct));
    }

    @Test
    void addOrderProductWithAlreadyExistingProductShouldAddQuantity() {

        OrderProduct orderProduct = getOrderProduct(66);

        Mockito.when(orderDao.getOrderProductsForOrder(12L))
                .thenReturn(singletonList(getOrderProduct(66)));

        //customerService.addOrderProduct(orderProduct);
        orderService.addProductToOrder(orderProduct);

        ArgumentCaptor<OrderProduct> captor = ArgumentCaptor.forClass(OrderProduct.class);
        Mockito.verify(orderDao).updateOrderProduct(captor.capture());

        assertThat(captor.getValue().getQuantity(), is(176));
    }


    private OrderProduct getOrderProduct(int productId) {
        OrderProduct orderProduct = new OrderProduct();
        orderProduct.setOrderId(12);
        orderProduct.setQuantity(BigDecimal.valueOf(88));

        Product product = new Product();
        product.setId(productId);
        orderProduct.setProduct(product);
        return orderProduct;
    }
}
