package com.algaworks.algashop.ordering.domain.entity;

import com.algaworks.algashop.ordering.domain.exception.OrderCannotBeEditedException;
import com.algaworks.algashop.ordering.domain.exception.OrderDoesNotContainOrderItemException;
import com.algaworks.algashop.ordering.domain.exception.OrderInvalidShippingDeliveryDateException;
import com.algaworks.algashop.ordering.domain.valueobject.Money;
import com.algaworks.algashop.ordering.domain.valueobject.Quantity;
import com.algaworks.algashop.ordering.domain.valueobject.Shipping;
import com.algaworks.algashop.ordering.domain.valueobject.id.CustomerId;
import com.algaworks.algashop.ordering.domain.valueobject.id.OrderItemId;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

class OrderRemoveItemTest {

    @Test
    void shouldCalculateTotals_whenRemoveAItem() {
        Order order = Order.draft(new CustomerId());

        order.addItem(
                ProductTestDataBuilder.aProductAltMousePad().build(),
                new Quantity(2)
        );

        order.addItem(
                ProductTestDataBuilder.aProductAltRamMemory().build(),
                new Quantity(1)
        );

        OrderItemId orderItemId = order.items().iterator().next().id();
        order.removeItem(orderItemId);

        Assertions.assertThat(order.totalAmount()).isEqualTo(new Money("200"));
        Assertions.assertThat(order.totalItems()).isEqualTo(new Quantity(1));
    }

    @Test
    void givenPlacedOrder_whenRemoveItem_shouldNotAllowRemoveItem() {
        Order order = OrderTestDataBuilder.anOrder().build();
        order.place();

        ThrowableAssert.ThrowingCallable removeItemTask = () -> {
            OrderItemId orderItemId = order.items().iterator().next().id();
            order.removeItem(orderItemId);
        };

        Assertions.assertThatExceptionOfType(OrderCannotBeEditedException.class).isThrownBy(removeItemTask);
    }

    @Test
    void givenItemNotExists_whenRemoveItem_shouldNotAllowRemoveItem() {
        Order order = Order.draft(new CustomerId());

        ThrowableAssert.ThrowingCallable removeItemTask = () -> order.removeItem(new OrderItemId());

        Assertions.assertThatExceptionOfType(OrderDoesNotContainOrderItemException.class).isThrownBy(removeItemTask);
    }
}