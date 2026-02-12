package io.agentgrid.api.controller;

import io.agentgrid.infrastructure.persistence.entity.Passenger;
import io.agentgrid.domain.service.PassengerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/passengers")
@RequiredArgsConstructor
public class PassengerController {
    private final PassengerService passengerService;

    @GetMapping
    public List<Passenger> getAll() { return passengerService.findAll(); }

    @GetMapping("/{id}")
    public ResponseEntity<Passenger> getById(@PathVariable Long id) { return ResponseEntity.ok(passengerService.findById(id)); }

    @GetMapping("/email/{email}")
    public ResponseEntity<Passenger> getByEmail(@PathVariable String email) { return ResponseEntity.ok(passengerService.findByEmail(email)); }

    @GetMapping("/passport/{passport}")
    public ResponseEntity<Passenger> getByPassport(@PathVariable String passport) { return ResponseEntity.ok(passengerService.findByPassport(passport)); }

    @PostMapping
    public ResponseEntity<Passenger> create(@Valid @RequestBody Passenger p) { return ResponseEntity.ok(passengerService.save(p)); }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) { passengerService.delete(id); return ResponseEntity.noContent().build(); }
}
