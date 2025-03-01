package io.paymeter.assessment.repository;


import io.paymeter.assessment.model.Pricing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PricingRepository extends JpaRepository<Pricing, String> {
    Pricing findByParkingId(String parkingId);
}
