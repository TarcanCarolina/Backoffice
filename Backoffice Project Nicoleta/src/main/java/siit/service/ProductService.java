package siit.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import siit.db.ProductDao;
import siit.model.Product;

import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductDao productDao;

    public List<Product> getProducts(){
        return productDao.getProducts();
    }

    public List<Product> searchForProducts(String term) {
        return productDao.searchForProducts(term);
    }

}
