package io.paymeter.assessment.service;

import io.paymeter.assessment.exception.InvalidDateRangeException;
import io.paymeter.assessment.exception.ParkingNotFoundException;
import io.paymeter.assessment.model.CalculationResponse;
import io.paymeter.assessment.model.Money;
import io.paymeter.assessment.model.Pricing;
import io.paymeter.assessment.repository.PricingRepository;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;

@Service
public class PricingService {

    private final PricingRepository pricingRepository;
    private static final int DEFAULT_PRICE_LESS_A_MINUTE = 0;

    public PricingService(PricingRepository pricingRepository) {
        this.pricingRepository = pricingRepository;
    }

    public CalculationResponse calculatePrice(String parkingId, LocalDateTime from, LocalDateTime to) {

        if (to != null && to.isBefore(from)) {
            throw new InvalidDateRangeException();
        }

        Pricing pricing = pricingRepository.findByParkingId(parkingId);
        if (pricing == null) {
            throw new ParkingNotFoundException(parkingId);
        }

        long durationMinutes = Duration.between(from, to).toMinutes();
        if (durationMinutes < 1) {
            return CalculationResponse.builder()
                    .parkingId(parkingId)
                    .from(from)
                    .to(to)
                    .duration(0)
                    .price(new Money(DEFAULT_PRICE_LESS_A_MINUTE).toString())
                    .build();
        }

        long durationHours = (long) Math.ceil((double) durationMinutes / 60);
        float price = pricing.calculatePrice(durationHours);

        return CalculationResponse.builder()
                .parkingId(parkingId)
                .from(from)
                .to(to)
                .duration(durationMinutes)
                .price(new Money(price).toString())
                .build();
    }
}
