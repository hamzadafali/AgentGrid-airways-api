package io.agentgrid.services;

import io.agentgrid.entities.Flight;
import io.agentgrid.repositories.FlightRepository;
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
