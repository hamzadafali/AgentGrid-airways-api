package io.agentgrid.domain.service;

import io.agentgrid.domain.exception.ResourceNotFoundException;
import io.agentgrid.infrastructure.persistence.entity.Passenger;
import io.agentgrid.infrastructure.repository.PassengerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PassengerServiceTest {

    @Mock
    PassengerRepository passengerRepo;

    @InjectMocks
    PassengerService passengerService;

    @Test
    void findAll_delegates_to_repository() {
        List<Passenger> passengers = List.of(Passenger.builder().id(1L).build());
        when(passengerRepo.findAll()).thenReturn(passengers);

        assertThat(passengerService.findAll()).isEqualTo(passengers);
        verify(passengerRepo).findAll();
    }

    @Test
    void findById_returns_passenger() {
        Passenger passenger = Passenger.builder().id(1L).build();
        when(passengerRepo.findById(1L)).thenReturn(Optional.of(passenger));

        assertThat(passengerService.findById(1L)).isEqualTo(passenger);
    }

    @Test
    void findById_throws_when_missing() {
        when(passengerRepo.findById(2L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> passengerService.findById(2L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Passenger not found");
    }

    @Test
    void findByEmail_throws_when_missing() {
        when(passengerRepo.findByEmail("missing@example.com")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> passengerService.findByEmail("missing@example.com"))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Passenger not found");
    }

    @Test
    void findByPassport_throws_when_missing() {
        when(passengerRepo.findByPassportNumber("P123"))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> passengerService.findByPassport("P123"))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Passenger not found");
    }

    @Test
    void save_and_delete_delegate_to_repository() {
        Passenger passenger = Passenger.builder().id(1L).build();
        when(passengerRepo.save(passenger)).thenReturn(passenger);

        assertThat(passengerService.save(passenger)).isEqualTo(passenger);
        verify(passengerRepo).save(passenger);

        passengerService.delete(1L);
        verify(passengerRepo).deleteById(1L);
    }
}
