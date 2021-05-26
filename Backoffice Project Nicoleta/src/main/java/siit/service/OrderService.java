package siit.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import siit.db.OrderDao;
import siit.db.ProductDao;
import siit.model.OrderProduct;

import java.math.BigDecimal;
import java.util.List;

import static siit.util.FinancialRules.calculateDiscountPercent;
import static siit.util.FinancialRules.calculateValueWithDiscount;

@Service
public class OrderService {

    @Autowired
    OrderDao orderDao;

    @Autowired
    private ProductDao productDao;

    @Transactional
    public void deleteProductFromOrder(int orderId, int productId){

        orderDao.deleteProductFromOrder(orderId, productId);

        if (orderDao.countOrderProductsForOrder(orderId)==0){
            orderDao.deleteOrderById(orderId);
        }
    }

    public OrderProduct addProductToOrder(OrderProduct orderProduct) {
        int discountPercent  = 0;
        OrderProduct orderProductToAdd;
        OrderProduct existingOrderProduct = getOrderProductByProductId(orderProduct.getOrderId(), orderProduct.getProduct().getId());
        if (existingOrderProduct == null) {
            orderDao.addProductToOrder(orderProduct);
            orderProductToAdd = getOrderProductByProductId(orderProduct.getOrderId(), orderProduct.getProduct().getId());
        } else {
            BigDecimal updatedQuantity = BigDecimal.ZERO;
            updatedQuantity = existingOrderProduct.getQuantity().add(orderProduct.getQuantity());
            existingOrderProduct.setQuantity(updatedQuantity);

            orderProductToAdd = existingOrderProduct;

            orderDao.updateOrderProduct(existingOrderProduct);
        }
        discountPercent = calculateDiscountPercent(orderProductToAdd.getQuantity().intValue(), 5, 12, 2);
        orderProductToAdd.setDiscount(discountPercent);

        BigDecimal valueWithDiscount = BigDecimal.ZERO;
        valueWithDiscount = calculateValueWithDiscount(orderProductToAdd.getQuantity().multiply(orderProductToAdd.getProduct().getPrice()), discountPercent);
        orderProductToAdd.setValue(valueWithDiscount);

        return orderProductToAdd;
    }

    private OrderProduct getOrderProductByProductId(long orderId, int productId) {
        List<OrderProduct> orderProducts = orderDao.getOrderProductsForOrder(orderId);
        for (OrderProduct dbOrderProduct : orderProducts) {
            if (productId == dbOrderProduct.getProduct().getId()) {
                dbOrderProduct.setProduct(productDao.findById(dbOrderProduct.getProduct().getId()));
                return dbOrderProduct;
            }
        }
        return null;
    }
}
