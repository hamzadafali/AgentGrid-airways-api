package io.agentgrid.controller;

import io.agentgrid.dtos.PaymentRequest;
import io.agentgrid.entities.Booking;
import io.agentgrid.entities.Payment;
import io.agentgrid.services.BookingService;
import io.agentgrid.services.PaymentService;
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
