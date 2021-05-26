package siit.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import siit.model.Product;
import siit.service.ProductService;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    ProductService productsService;

    @GetMapping
    public List<Product> getByTerm(@RequestParam String term) {
        return productsService.searchForProducts(term);
    }
}

