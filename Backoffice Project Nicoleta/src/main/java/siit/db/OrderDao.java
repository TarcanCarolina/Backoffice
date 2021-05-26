package siit.db;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import siit.model.Order;
import siit.model.OrderProduct;
import siit.model.Product;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class OrderDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void deleteProductFromOrder(int orderId, int productId){
        jdbcTemplate.update(
                "delete from ORDERS_PRODUCTS where order_id=? and product_id=?",
                orderId,
                productId
        );
    }
    public List<Order> getOrdersByCustomerId(int customerId) {
        return jdbcTemplate.query(
                "select * from orders where customer_id = ?",
                this::mapOrder,
                customerId);
    }

    public List<Order> getOrdersByCustomers(int id){
        return jdbcTemplate.query("select * from orders where customer_id = ?",
                this::mapOrder,id);
    }

    public void deleteOrderById(int id) {
        jdbcTemplate.update(
                "delete from orders where id = ?", id);
    }

    public void deleteOrderProductsByOrderId(int id) {
        jdbcTemplate.update(
                "delete from orders_products where order_id = ?", id);
    }

    private Order mapOrder(ResultSet resultSet, int rowNum) throws SQLException {
        Order order = new Order();
        order.setNumber(resultSet.getString("number"));
        order.setId(resultSet.getInt("id"));
        return order;
    }

    public void addOrderForCustomer(int customerId, Order order){
        jdbcTemplate.update("insert into orders (customer_id, number) values (?,?)",
                customerId, order.getNumber());
    }

   public List<OrderProduct> getOrderProductsForOrder(long orderId) {
        return jdbcTemplate.query("select * from orders_products where order_id = ?",
                this::mapRowToOrderProduct, orderId);
    }
    public int countOrderProductsForOrder(long orderId){
        return Optional.ofNullable(jdbcTemplate.queryForObject(
                "select count(*) from ORDERS_PRODUCTS where order_id=?",
                Integer.class,
                orderId)).orElse(0);
    }

    public void addProductToOrder(OrderProduct orderProduct) {
        jdbcTemplate.update("insert into orders_products (order_id, product_id, quantity) values (?,?,?)",
                orderProduct.getOrderId(), orderProduct.getProduct().getId(), orderProduct.getQuantity());
    }

    private OrderProduct mapRowToOrderProduct(ResultSet rs, int rowNum) throws SQLException {
        OrderProduct orderProduct = new OrderProduct();
        Product product = new Product();
        product.setId(rs.getInt("product_id"));
        orderProduct.setProduct(product);
        orderProduct.setOrderId(rs.getInt("order_id"));
        orderProduct.setQuantity(rs.getBigDecimal("quantity"));
        return orderProduct;
    }

    public Order findOrderById(int id) {
        return jdbcTemplate.queryForObject("select * from orders where id = ?",
                this::mapRowToOrder, id);
    }

    private Order mapRowToOrder(ResultSet rs, int rowNum) throws SQLException {
        Order order = new Order();
        order.setId(rs.getInt("id"));
        order.setNumber(rs.getString("number"));
        return order;
    }

    public void updateOrderProduct(OrderProduct orderProduct) {
        jdbcTemplate.update(
                "UPDATE ORDERS_PRODUCTS SET QUANTITY = ? WHERE ORDER_ID = ? AND PRODUCT_ID = ?",
                orderProduct.getQuantity(), orderProduct.getOrderId(), orderProduct.getProduct().getId());
    }
}
