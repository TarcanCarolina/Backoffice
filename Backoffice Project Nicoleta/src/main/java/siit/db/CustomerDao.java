package siit.db;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import siit.model.Customer;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

@Repository
public class CustomerDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public Customer getCustomerById(int id) {
        return jdbcTemplate.queryForObject(
                "select * from customers where id = ?",
                this::getCustomer,
                id);
    }

    public List<Customer> getCustomers() {
        return jdbcTemplate.query(
                "select * from customers",
                this::getCustomer);
    }

    private Customer getCustomer(ResultSet resultSet, int rowNum) throws SQLException {
        Customer customer = new Customer();
        customer.setId(resultSet.getInt("id"));
        customer.setName(resultSet.getString("name"));
        customer.setEmail(resultSet.getString("email"));
        customer.setPhone(resultSet.getString("phone"));
        customer.setBirthDay(resultSet.getDate("birthday").toLocalDate());
        return customer;
    }

    public void update(Customer customer){
        Object[] params = {customer.getName(), customer.getPhone(), customer.getId()};
        int [] types = {Types.VARCHAR, Types.VARCHAR, Types.BIGINT};
        jdbcTemplate.update(
                "update customers set name=(?), phone=(?) WHERE id=(?)",
                params,
                types
        );
    }
}
