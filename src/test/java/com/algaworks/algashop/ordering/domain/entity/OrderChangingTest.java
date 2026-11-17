package com.algaworks.algashop.ordering.domain.entity;

import com.algaworks.algashop.ordering.domain.exception.OrderCannotBeEditedException;
import com.algaworks.algashop.ordering.domain.exception.ProductOutOfStockException;
import com.algaworks.algashop.ordering.domain.valueobject.Money;
import com.algaworks.algashop.ordering.domain.valueobject.Quantity;
import com.algaworks.algashop.ordering.domain.valueobject.id.CustomerId;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.Test;

class OrderChangingTest {

    @Test
    void givenDraftOrder_whenChangeItem_shouldNotException() {
        Order order = Order.draft(new CustomerId());

        order.addItem(
                ProductTestDataBuilder.aProductAltMousePad().build(),
                new Quantity(3)
        );

        OrderItem orderItem = order.items().iterator().next();

        order.changeItemQuantity(orderItem.id(), new Quantity(5));

        Assertions.assertWith(order,
                (o) -> Assertions.assertThat(o.totalAmount()).isEqualTo(new Money("500")),
                (o) -> Assertions.assertThat(o.totalItems()).isEqualTo(new Quantity(5))
        );
    }

    @Test
    void givenNotDraftOrder_whenChangeItem_shouldException() {
        Order order = OrderTestDataBuilder.anOrder().build();
        order.place();

        ThrowableAssert.ThrowingCallable addItemTask = () -> order.addItem(
                ProductTestDataBuilder.aProductUnavailable().build(),
                new Quantity(1)
        );

        Assertions.assertThatExceptionOfType(OrderCannotBeEditedException.class).isThrownBy(addItemTask);
    }
}
