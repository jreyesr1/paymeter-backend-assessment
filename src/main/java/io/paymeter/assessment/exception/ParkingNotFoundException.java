package io.paymeter.assessment.exception;

public class ParkingNotFoundException extends RuntimeException {
    public ParkingNotFoundException(String parkingId) {
        super(String.format("Parking with ID '%s' not found.", parkingId));
    }
}
