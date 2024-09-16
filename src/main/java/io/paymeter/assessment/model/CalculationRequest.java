package io.paymeter.assessment.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Value;

import java.time.LocalDateTime;

@Value
@Data
@AllArgsConstructor
public class CalculationRequest {

    @NotBlank(message = "Parking ID is required")
    String parkingId;

    @NotNull(message = "From date is required")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    LocalDateTime from;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    LocalDateTime to;
}
