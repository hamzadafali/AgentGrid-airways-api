package io.agentgrid.services;

import io.agentgrid.entities.Passenger;
import io.agentgrid.exceptions.ResourceNotFoundException;
import io.agentgrid.repositories.PassengerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PassengerService {
    private final PassengerRepository passengerRepo;

    public List<Passenger> findAll() { return passengerRepo.findAll(); }

    public Passenger findById(Long id) {
        return passengerRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Passenger not found"));
    }

    public Passenger findByEmail(String email) {
        return passengerRepo.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("Passenger not found"));
    }

    public Passenger findByPassport(String passport) {
        return passengerRepo.findByPassportNumber(passport).orElseThrow(() -> new ResourceNotFoundException("Passenger not found"));
    }

    public Passenger save(Passenger passenger) { return passengerRepo.save(passenger); }

    public void delete(Long id) { passengerRepo.deleteById(id); }
}
