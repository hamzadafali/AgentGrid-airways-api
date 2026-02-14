package io.agentgrid.domain.service;

import io.agentgrid.domain.enums.BookingStatus;
import io.agentgrid.domain.exception.BookingException;
import io.agentgrid.infrastructure.persistence.entity.Booking;
import io.agentgrid.infrastructure.persistence.entity.Flight;
import io.agentgrid.infrastructure.persistence.entity.Passenger;
import io.agentgrid.infrastructure.repository.BookingRepository;
import io.agentgrid.infrastructure.repository.FlightRepository;
import io.agentgrid.infrastructure.repository.PassengerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingServiceTest {

    @Mock
    BookingRepository bookingRepo;

    @Mock
    FlightRepository flightRepo;

    @Mock
    PassengerRepository passengerRepo;

    @InjectMocks
    BookingService bookingService;

    @Mock
    SeatService seatService;

    private Flight flight;
    private Passenger passenger;

    @BeforeEach
    void setUp() {
        flight = Flight.builder()
                .id(1L)
                .availableSeats(10)
                .build();

        passenger = Passenger.builder()
                .id(1L)
                .email("test@example.com")
                .build();
    }

    @Test
    void createBooking_success() {
        when(flightRepo.findById(1L)).thenReturn(Optional.of(flight));
        when(passengerRepo.findById(1L)).thenReturn(Optional.of(passenger));
        when(flightRepo.save(any(Flight.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(bookingRepo.save(any(Booking.class))).thenAnswer(invocation -> {
            Booking b = invocation.getArgument(0);
            b.setId(100L);
            return b;
        });

        Booking result = bookingService.createBooking(1L, 1L, "12A");

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(100L);
        assertThat(result.getSeatNumber()).isEqualTo("12A");
        assertThat(result.getStatus()).isEqualTo(BookingStatus.PENDING);
        assertThat(result.getFlight()).isEqualTo(flight);
        assertThat(result.getPassenger()).isEqualTo(passenger);

        verify(flightRepo).save(any(Flight.class));
        verify(bookingRepo).save(any(Booking.class));
    }

    @Test
    void createBooking_seatAlreadyReserved() {
        when(flightRepo.findById(1L)).thenReturn(Optional.of(flight));
        when(passengerRepo.findById(1L)).thenReturn(Optional.of(passenger));
        when(bookingRepo.findAll()).thenReturn(List.of(
                Booking.builder()
                        .seatNumber("12A")
                        .flight(flight)
                        .status(BookingStatus.CONFIRMED)
                        .build()
        ));

        assertThatThrownBy(() -> bookingService.createBooking(1L, 1L, "12A"))
                .isInstanceOf(BookingException.class)
                .hasMessage("Seat already reserved");

        verify(flightRepo).findById(1L);
        verify(passengerRepo).findById(1L);
        verify(bookingRepo, never()).save(any());
    }

    @Test
    void createBooking_reserveSeatFails() {
        when(flightRepo.findById(1L)).thenReturn(Optional.of(flight));
        when(passengerRepo.findById(1L)).thenReturn(Optional.of(passenger));
        doThrow(new RuntimeException("Seat reservation error"))
                .when(seatService).reserveSeat(1L, "12A");

        assertThatThrownBy(() -> bookingService.createBooking(1L, 1L, "12A"))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Seat reservation error");

        verify(flightRepo).findById(1L);
        verify(passengerRepo).findById(1L);
        verify(seatService).reserveSeat(1L, "12A");
        verify(flightRepo, never()).save(any());
        verify(bookingRepo, never()).save(any());
    }

    @Test
    void createBooking_flightNotFound() {
        when(flightRepo.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> bookingService.createBooking(1L, 1L, "1A"))
                .isInstanceOf(BookingException.class)
                .hasMessageContaining("Flight not found");

        verifyNoInteractions(passengerRepo, bookingRepo);
    }

    @Test
    void createBooking_noSeats() {
        flight.setAvailableSeats(0);
        when(flightRepo.findById(1L)).thenReturn(Optional.of(flight));

        assertThatThrownBy(() -> bookingService.createBooking(1L, 1L, "1A"))
                .isInstanceOf(BookingException.class)
                .hasMessageContaining("No seats available");

        verifyNoInteractions(passengerRepo, bookingRepo);
    }

    @Test
    void createBooking_passengerNotFound() {
        when(flightRepo.findById(1L)).thenReturn(Optional.of(flight));
        when(passengerRepo.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> bookingService.createBooking(1L, 1L, "1A"))
                .isInstanceOf(BookingException.class)
                .hasMessageContaining("Passenger not found");

        verify(flightRepo).findById(1L);
        verify(passengerRepo).findById(1L);
        verifyNoInteractions(bookingRepo);
    }

    @Test
    void cancelBooking_success() {
        Booking booking = Booking.builder()
                .id(200L)
                .status(BookingStatus.PENDING)
                .build();

        when(bookingRepo.findById(200L)).thenReturn(Optional.of(booking));
        when(bookingRepo.save(any(Booking.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Booking cancelled = bookingService.cancelBooking(200L);

        assertThat(cancelled.getStatus()).isEqualTo(BookingStatus.CANCELLED);
        verify(bookingRepo).save(any(Booking.class));
    }

    @Test
    void cancelBooking_nonPending() {
        Booking booking = Booking.builder()
                .id(201L)
                .status(BookingStatus.CONFIRMED)
                .build();

        when(bookingRepo.findById(201L)).thenReturn(Optional.of(booking));

        assertThatThrownBy(() -> bookingService.cancelBooking(201L))
                .isInstanceOf(BookingException.class)
                .hasMessageContaining("Booking cannot be canceled");

        verify(bookingRepo, never()).save(any());
    }
}
