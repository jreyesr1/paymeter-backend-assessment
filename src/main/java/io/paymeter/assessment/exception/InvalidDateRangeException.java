package io.paymeter.assessment.exception;

public class InvalidDateRangeException extends RuntimeException {
    public InvalidDateRangeException() {
        super("The 'to' date must not be before the 'from' date.");
    }
}
