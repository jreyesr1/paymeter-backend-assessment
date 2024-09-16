package io.paymeter.assessment.unit.model;

import io.paymeter.assessment.model.Pricing;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PricingTest {

    @Test
    void testCalculatePriceBasic() {
        Pricing pricing = new Pricing("P000123", 2.0f, 15.0f, 24, false);
        float price = pricing.calculatePrice(5); // 5 hours, no max price

        assertEquals(10.0f, price, "The price should be 10.0 EUR");
    }

    @Test
    void testCalculatePriceWithMaxPrice() {
        Pricing pricing = new Pricing("P000123", 2.0f, 15.0f, 24, false);
        float price = pricing.calculatePrice(12); // 12 hours, with max price of 15 EUR

        assertEquals(15.0f, price);
    }

    @Test
    void testCalculatePriceWithFirstHourFree() {
        Pricing pricing = new Pricing("P000456", 3.0f, 20.0f, 12, true);
        float price = pricing.calculatePrice(15);

        assertEquals(26.0f, price);
    }


    @Test
    void testCalculatePriceZeroDuration() {
        Pricing pricing = new Pricing("P000123", 2.0f, 15.0f, 24, false);
        float price = pricing.calculatePrice(0);

        assertEquals(0.0f, price);
    }
}
