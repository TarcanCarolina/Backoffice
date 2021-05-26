package siit.util;

import java.math.BigDecimal;

public class FinancialRules {
    //The [price of product] changes with quantity.
    //If quantity is equal to or above minQuantityToDiscount,
    // the price drops with discountDrop% for all products,
    // up to a maximum discount of maxDiscountPercent%
    public static int calculateDiscountPercent(int quantity, int minQuantityToDiscount, int maxDiscountPercent, int discountDrop){
        int discount = 0;
        if (quantity >= minQuantityToDiscount){
            discount = (quantity - minQuantityToDiscount + 1) * discountDrop;
        }
        if (discount > maxDiscountPercent)
            discount = maxDiscountPercent;
        return discount;
    }

    ///Value of shipping is discountValue,
    // unless the amount (sum[order products]) is equal to or more than minValue
    // in which case it is 0
    public static BigDecimal calculateShippingValue(BigDecimal amount, BigDecimal minValue, BigDecimal discountValue){
        BigDecimal shipping = BigDecimal.ZERO;
        if (amount.compareTo(minValue)< 0)
            shipping = discountValue;
        return shipping;
    }

    public static BigDecimal calculateValueWithDiscount(BigDecimal value, int discount){
        BigDecimal valueWithDiscount = BigDecimal.ZERO;
        BigDecimal discountValue = value.multiply(BigDecimal.valueOf(discount)).divide(BigDecimal.valueOf(100));
        valueWithDiscount = value.subtract(discountValue);
        return  valueWithDiscount;
    }


}
