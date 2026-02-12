package io.agentgrid.domain.service;

import io.agentgrid.infrastructure.persistence.entity.Flight;
import io.agentgrid.infrastructure.repository.FlightRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FlightServiceTest {

    @Mock
    FlightRepository flightRepo;

    @InjectMocks
    FlightService flightService;

    @Test
    void search_delegates_to_repository() {
        LocalDateTime start = LocalDateTime.of(2030, 1, 1, 0, 0);
        LocalDateTime end = start.plusDays(1);
        List<Flight> flights = List.of(Flight.builder().id(1L).build());

        when(flightRepo.findByOriginAndDestinationAndDepartureDateTimeBetween("CDG", "JFK", start, end))
                .thenReturn(flights);

        List<Flight> result = flightService.search("CDG", "JFK", start, end);

        assertThat(result).isEqualTo(flights);
        verify(flightRepo).findByOriginAndDestinationAndDepartureDateTimeBetween("CDG", "JFK", start, end);
    }

    @Test
    void findAll_delegates_to_repository() {
        List<Flight> flights = List.of(Flight.builder().id(1L).build());
        when(flightRepo.findAll()).thenReturn(flights);

        assertThat(flightService.findAll()).isEqualTo(flights);
        verify(flightRepo).findAll();
    }

    @Test
    void save_delegates_to_repository() {
        Flight flight = Flight.builder().id(1L).build();
        when(flightRepo.save(flight)).thenReturn(flight);

        assertThat(flightService.save(flight)).isEqualTo(flight);
        verify(flightRepo).save(flight);
    }
}
