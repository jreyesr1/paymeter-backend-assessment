package io.paymeter.assessment.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Pricing {

    @Id
    private String parkingId;
    private float hourlyRate;
    private float maxPrice;
    private int periodInHours;
    private boolean firstHourFree;

    public float calculatePrice(long durationHours) {
        if (firstHourFree && durationHours > periodInHours) {
            durationHours = Math.max(durationHours - 1, 0);
        }

        long fullPeriods = durationHours / periodInHours;
        long remainingHours = durationHours % periodInHours;

        float totalPrice = fullPeriods * maxPrice;

        if (remainingHours > 0) {
            totalPrice += Math.min(hourlyRate * remainingHours, maxPrice);
        }

        return totalPrice;
    }
}
