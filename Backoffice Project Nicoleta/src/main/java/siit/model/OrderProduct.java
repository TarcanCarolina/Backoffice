package siit.model;

import java.math.BigDecimal;

public class OrderProduct {

    private long orderId;
    private Product product;
    private BigDecimal quantity;

    private int discount;
    private BigDecimal value;

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public BigDecimal getValue() {
        return this.value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "OrderProduct{" +
                "orderId=" + orderId +
                ", product=" + product +
                ", value=" + value +
                ", quantity=" + quantity +
                '}';
    }
}
