package com.algaworks.algashop.ordering.domain.valueobject;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

public record Money(BigDecimal value) implements Comparable<Money> {

    private static final RoundingMode ROUNDING_MODE_HALF_EVEN = RoundingMode.HALF_EVEN;
    public static final Money ZERO = new Money(BigDecimal.ZERO);

    public Money(BigDecimal value) {
        Objects.requireNonNull(value);

        if (value.signum() < 0) {
            throw new IllegalArgumentException();
        }

        this.value = value.setScale(2, ROUNDING_MODE_HALF_EVEN);
    }

    public Money(String value) {
        this(new BigDecimal(value));
    }

    public Money multiply(Quantity quantity) {
        Objects.requireNonNull(quantity);

        if (quantity.value() < 1) {
            throw new IllegalArgumentException();
        }

        BigDecimal multiply = this.value.multiply(new BigDecimal(quantity.value()));
        return new Money(multiply);
    }

    public Money add(Money other) {
        Objects.requireNonNull(other);

        if (other.value.signum() <= 0) {
            throw new IllegalArgumentException();
        }

        return new Money(this.value.add(other.value));
    }

    public Money divide(Money other) {
        Objects.requireNonNull(other);

        if (other.value.signum() <= 0) {
            throw new IllegalArgumentException();
        }

        BigDecimal divide = this.value.divide(other.value);
        return new Money(divide.setScale(2, ROUNDING_MODE_HALF_EVEN));
    }

    @Override
    public int compareTo(Money other) {
        return this.value.compareTo(other.value);
    }

    @Override
    public String toString() {
        return value.toString();
    }
}