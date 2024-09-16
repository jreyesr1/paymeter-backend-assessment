package io.paymeter.assessment.config;

import io.paymeter.assessment.model.Pricing;
import io.paymeter.assessment.repository.PricingRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataLoader {

    @Bean
    public CommandLineRunner loadData(PricingRepository pricingRepository) {
        return args -> {
            // Customer 1: P000123, 2€/hr, max 15€/day
            pricingRepository.save(new Pricing("P000123", 2, 15, 24, false));

            // Customer 2: P000456, 3€/hr, max 20€/12hr, first hour free
            pricingRepository.save(new Pricing("P000456", 3, 20, 12, true));
        };
    }
}
