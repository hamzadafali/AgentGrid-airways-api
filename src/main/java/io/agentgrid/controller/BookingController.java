package io.agentgrid.controller;

import io.agentgrid.dtos.BookingRequest;
import io.agentgrid.entities.Booking;
import io.agentgrid.services.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
public class BookingController {
    private final BookingService bookingService;

    @PostMapping
    public ResponseEntity<Booking> create(@RequestBody BookingRequest req) {
        return ResponseEntity.ok(bookingService.createBooking(req.getFlightId(), req.getPassengerId(), req.getSeatNumber()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Booking> getById(@PathVariable Long id) {
        return ResponseEntity.ok(bookingService.findById(id));
    }

    @GetMapping("/reference/{ref}")
    public ResponseEntity<Booking> getByRef(@PathVariable String ref) {
        return ResponseEntity.ok(bookingService.findByReference(ref));
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<Booking> cancel(@PathVariable Long id) {
        return ResponseEntity.ok(bookingService.cancelBooking(id));
    }
}
