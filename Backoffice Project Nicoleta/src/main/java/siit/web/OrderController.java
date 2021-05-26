package siit.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import siit.model.Order;
import siit.model.OrderProduct;
import siit.service.CustomerService;
import siit.service.OrderService;

import java.util.List;

@RestController
@RequestMapping("/api/customers/{customerId}/orders/{orderId}")
public class OrderController {

    @Autowired
    CustomerService customerService;

    @Autowired
    OrderService orderService;

    @GetMapping
    public Order getOrder(@PathVariable int orderId) {
        return customerService.getOrderById(orderId);
    }

    @GetMapping(path = "/products")
    public List<OrderProduct> getOrderProducts(@PathVariable int orderId) {
        return customerService.getOrderById(orderId).getOrderProducts();
    }

    @PostMapping(path = "/products")
    public OrderProduct addProductToOrder(@PathVariable int orderId, @RequestBody OrderProduct orderProduct) {
        orderProduct.setOrderId(orderId);
        return orderService.addProductToOrder(orderProduct);
    }

    @DeleteMapping(path = "/products/{productId}")
    public void deleteProductFromOrder(@PathVariable int orderId, @PathVariable int productId ){
        orderService.deleteProductFromOrder(orderId, productId);
    }
}
