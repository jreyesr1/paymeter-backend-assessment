package io.paymeter.assessment.controller;

import io.paymeter.assessment.model.CalculationRequest;
import io.paymeter.assessment.model.CalculationResponse;
import io.paymeter.assessment.service.PricingService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Clock;
import java.time.LocalDateTime;

@RestController
@RequestMapping("tickets")
public class TicketController {

	private final PricingService pricingService;
	private final Clock clock;

	public TicketController(PricingService pricingService, Clock clock) {
		this.pricingService = pricingService;
		this.clock = clock;
	}


	@PostMapping("/calculate")
	public ResponseEntity<CalculationResponse> calculate(@Valid @RequestBody CalculationRequest request) {
		LocalDateTime to = request.getTo() != null ? request.getTo() : LocalDateTime.now(clock);
		CalculationResponse response = pricingService.calculatePrice(request.getParkingId(), request.getFrom(), to);
		return ResponseEntity.ok(response);
	}


}
