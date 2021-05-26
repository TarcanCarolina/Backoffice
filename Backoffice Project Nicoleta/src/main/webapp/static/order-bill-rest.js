
var customerId = $.url('#customerId');
var orderId = $.url('#orderId');

$(() => {
    $.getJSON(`/api/customers/${customerId}/orders/${orderId}`)
     .done((order) => {
        $('#titleOrderNumber').text(order.number);
        $('#buttonDone').attr('href', `/customers/${customerId}/orders/`);
        $('#orderValue').text(order.value);
        $('#shippingValue').text(order.shipping);
        $('#orderTotal').text(order.total);
    });

    $.getJSON(`/api/customers/${customerId}/orders/${orderId}/products`)
     .done((orderProducts) => orderProducts.forEach(addOrderProductRow)
    );

    });

function addOrderProductRow(orderProduct) {
    var newRow = $(`
        <tr id="op_${orderProduct.product.id}">
            <th>${orderProduct.product.name}</th>
            <th><span name="spanPrice">${orderProduct.product.price}</span></th>
            <th><span name="spanQuantity">${orderProduct.quantity}</span></th>
            <th><span name="spanDiscount" style="color: #e8000d;">${orderProduct.discount}</span></th>
            <th><span name="spanValue">${orderProduct.value}</span></th>
        </tr>
    `);

    newRow.hide().insertBefore('#productAddFormRow').show('slow');
}

