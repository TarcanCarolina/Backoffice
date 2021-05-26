package siit.integration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class TestDataUtil {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public Integer getProductIdByName(String name) {
        return jdbcTemplate.queryForObject(
                "SELECT ID FROM PRODUCTS WHERE NAME = ?", Integer.class, name);
    }

    public Integer getOrderByNumber(String number) {
        return jdbcTemplate.queryForObject(
                "SELECT ID FROM ORDERS WHERE NUMBER = ?", Integer.class, number);
    }
}
