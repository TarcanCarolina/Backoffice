package siit.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import siit.db.ProductDao;
import siit.exceptions.ValidationException;
import siit.model.Customer;
import siit.db.CustomerDao;
import siit.db.OrderDao;
import siit.model.Order;
import siit.model.OrderProduct;
import siit.model.Product;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static siit.util.FinancialRules.*;

@Service
public class CustomerService {

    @Autowired
    private CustomerDao customerDao;

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private ProductDao productDao;

    public List<Customer> getCustomers(){
        return customerDao.getCustomers();
    }

    public void updateCustomer(Customer customer){
        if (!customer.getPhone().matches("\\+\\d+")){
            throw new ValidationException("phoneNumber.malformed");
        }
        customerDao.update(customer);
    }

    public void addOrderForCustomer(int customerId, Order order){
        orderDao.addOrderForCustomer(customerId, order);
    }

    @Transactional
    public void deleteOrder(int id) {
        orderDao.deleteOrderProductsByOrderId(id);
        orderDao.deleteOrderById(id);
    }

    public Customer getCustomerByIdWithOrders(int id) {
        Customer customer = customerDao.getCustomerById(id);
        customer.setOrders(orderDao.getOrdersByCustomerId(id));
        Map<Integer, Product> productMap = new HashMap<>();
        for (Order order : customer.getOrders()) {
            populateOrderProducts(order, productMap);
            calculateOrderValue(order);
        }
        return customer;
    }

    public Order getOrderById(int id){
        Order order = orderDao.findOrderById(id);
        populateOrderProducts(order, new HashMap<>());
        calculateOrderValue(order);
        calculateOrderShipping(order);
        order.setTotal(order.getValue().add(order.getShipping()));
        return order;
    }

    private void populateOrderProducts(Order order, Map<Integer, Product> productMap) { //populate order with products from db
        order.setOrderProducts(
                orderDao.getOrderProductsForOrder(order.getId()));
        for (OrderProduct orderProduct : order.getOrderProducts()) {
            Product product = productMap.computeIfAbsent(
                    orderProduct.getProduct().getId(), productDao::findById);
            orderProduct.setProduct(product);

            int discountPercent = 0;
            discountPercent = calculateDiscountPercent(orderProduct.getQuantity().intValue(), 5, 12, 2);
            orderProduct.setDiscount(discountPercent);

            BigDecimal valueWithDiscount = BigDecimal.ZERO;
            valueWithDiscount = calculateValueWithDiscount(orderProduct.getQuantity().multiply(orderProduct.getProduct().getPrice()), discountPercent);
            orderProduct.setValue(valueWithDiscount);
        }
    }

    private void calculateOrderShipping(Order order){
        order.setShipping(calculateShippingValue(order.getValue(), BigDecimal.valueOf(1000), BigDecimal.valueOf(20)));
    }

    private void calculateOrderValue(Order order) {
        BigDecimal orderTotalValue=BigDecimal.ZERO;
        for (OrderProduct op:order.getOrderProducts()){
            orderTotalValue = orderTotalValue.add(op.getValue());
        }
        order.setValue(orderTotalValue);
    }

    /*private BigDecimal calculateValueWithDiscountForOrderProduct(OrderProduct orderProduct){
        BigDecimal valueWithDiscount = BigDecimal.ZERO;
        orderProduct.setDiscount(calculateDiscountPercent(orderProduct.getQuantity().intValue(), 5, 12, 2));
        BigDecimal discountValue = BigDecimal.valueOf(orderProduct.getDiscount()).multiply(orderProduct.getValue());
        valueWithDiscount = orderProduct.getValue().subtract(discountValue);
        orderProduct.setValue(valueWithDiscount);
        return  valueWithDiscount;
    }*/

}
