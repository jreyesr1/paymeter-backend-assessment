package io.paymeter.assessment.unit.service;


import io.paymeter.assessment.exception.ParkingNotFoundException;
import io.paymeter.assessment.model.CalculationResponse;
import io.paymeter.assessment.model.Pricing;
import io.paymeter.assessment.repository.PricingRepository;
import io.paymeter.assessment.service.PricingService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
class PricingServiceTest {

    @Autowired
    private PricingService pricingService;

    @MockBean
    private PricingRepository pricingRepository;

    @Test
    void testCalculatePriceSuccess() {
        Pricing pricing = new Pricing("P000123", 2.0f, 15.0f, 24, false);
        Mockito.when(pricingRepository.findByParkingId("P000123"))
                .thenReturn(pricing);

        LocalDateTime from = LocalDateTime.of(2024, 9, 16, 10, 0);
        LocalDateTime to = LocalDateTime.of(2024, 9, 16, 15, 0);

        CalculationResponse response = pricingService.calculatePrice("P000123", from, to);

        assertEquals("P000123", response.getParkingId());
        assertEquals(300, response.getDuration());
        assertEquals("1000EUR", response.getPrice());
    }

    @Test
    void testCalculatePriceWithParkingNotFound() {
        Mockito.when(pricingRepository.findByParkingId(any(String.class)))
                .thenReturn(null);

        LocalDateTime from = LocalDateTime.of(2024, 9, 16, 10, 0);
        LocalDateTime to = LocalDateTime.of(2024, 9, 16, 15, 0);

        assertThrows(ParkingNotFoundException.class, () -> {
            pricingService.calculatePrice("P000999", from, to);
        });
    }

    @Test
    void testCalculatePriceFreeParkingIfLessThanOneMinute() {
        Pricing pricing = new Pricing("P000123", 2.0f, 15.0f, 24, false);
        Mockito.when(pricingRepository.findByParkingId("P000123"))
                .thenReturn(pricing);

        LocalDateTime from = LocalDateTime.of(2024, 9, 16, 10, 0);
        LocalDateTime to = LocalDateTime.of(2024, 9, 16, 10, 0, 30);

        CalculationResponse response = pricingService.calculatePrice("P000123", from, to);

        assertEquals(0, response.getDuration());
        assertEquals("0EUR", response.getPrice());
    }
}