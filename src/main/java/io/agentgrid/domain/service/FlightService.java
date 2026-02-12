package io.agentgrid.domain.service;

import io.agentgrid.infrastructure.persistence.entity.Flight;
import io.agentgrid.infrastructure.repository.FlightRepository;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FlightService {
    private final FlightRepository flightRepo;

    public List<Flight> search(String origin, String destination, LocalDateTime start, LocalDateTime end) {
        return flightRepo.findByOriginAndDestinationAndDepartureDateTimeBetween(
                origin, destination, start, end
        );
    }

    public List<Flight> findAll() {
        return flightRepo.findAll();
    }

    public Flight save(Flight flight) {
        return flightRepo.save(flight);
    }
}
