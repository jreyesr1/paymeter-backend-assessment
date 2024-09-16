package io.paymeter.assessment.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.Currency;

@EqualsAndHashCode
public class Money {
    private static final Currency DEFAULT_CURRENCY = Currency.getInstance("EUR");

    @Getter
    private final float amount;
    private final Currency currency;

    public Money(float amount) {
        this.amount = Math.round(amount * 100) / 100.0f;
        this.currency = DEFAULT_CURRENCY;
    }

    public String getCurrencyCode() {
        return currency.getCurrencyCode();
    }

    @Override
    public String toString() {
        return String.format("%d%s", Math.round(amount * 100), getCurrencyCode());
    }
}
