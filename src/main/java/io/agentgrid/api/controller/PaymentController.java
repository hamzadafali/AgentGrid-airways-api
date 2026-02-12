package io.agentgrid.api.controller;

import io.agentgrid.api.dto.Request.PaymentRequest;
import io.agentgrid.infrastructure.persistence.entity.Booking;
import io.agentgrid.infrastructure.persistence.entity.Payment;
import io.agentgrid.domain.service.BookingService;
import io.agentgrid.domain.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;
    private final BookingService bookingService;

    @PostMapping
    public ResponseEntity<Payment> process(@RequestBody PaymentRequest req) {
        Booking booking = bookingService.findById(req.getBookingId());
        return ResponseEntity.ok(paymentService.processPayment(booking, req.getAmount(), req.getMethod()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Payment> getById(@PathVariable Long id) {
        return ResponseEntity.ok(paymentService.findById(id));
    }
}
