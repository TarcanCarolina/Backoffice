package siit.integration.service;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import siit.integration.ITBase;
import siit.service.ProductService;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class ProductServiceIT extends ITBase {

    @Autowired
    private ProductService productService;

    @Test
    void smokeTest() {
        assertThat(productService, is(notNullValue()));
        assertThat(productService.getProducts(), hasSize(6));
    }
}
