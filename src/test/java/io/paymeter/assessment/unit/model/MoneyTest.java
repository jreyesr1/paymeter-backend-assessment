package io.paymeter.assessment.unit.model;

import io.paymeter.assessment.model.Money;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MoneyTest {

    @Test
    void testCurrencyIsAlwaysEuro() {
        var money = new Money(123);

        assertEquals("EUR", money.getCurrencyCode());
    }

    @Test
    void testAmountIsRoundedCorrectly() {
        var money = new Money(123.456f);

        assertEquals(123.46f, money.getAmount());
    }

    @Test
    void testToString() {
        var money = new Money(123.45f);

        assertEquals("12345EUR", money.toString());
    }
}
