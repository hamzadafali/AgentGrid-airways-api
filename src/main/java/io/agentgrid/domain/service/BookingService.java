package io.agentgrid.domain.service;


import io.agentgrid.infrastructure.persistence.entity.Booking;
import io.agentgrid.infrastructure.persistence.entity.Flight;
import io.agentgrid.infrastructure.persistence.entity.Passenger;
import io.agentgrid.domain.enums.BookingStatus;
import io.agentgrid.domain.exception.BookingException;
import io.agentgrid.infrastructure.repository.BookingRepository;
import io.agentgrid.infrastructure.repository.FlightRepository;
import io.agentgrid.infrastructure.repository.PassengerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BookingService {
    private final BookingRepository bookingRepo;
    private final FlightRepository flightRepo;
    private final PassengerRepository passengerRepo;
    private final SeatService seatService;

    @Transactional
    public Booking createBooking(Long flightId, Long passengerId, String seatNumber) {
        Flight flight = flightRepo.findById(flightId)
                .orElseThrow(() -> new BookingException("Flight not found"));

        if (flight.getAvailableSeats() <= 0) {
            throw new BookingException("No seats available for this flight");
        }

        Passenger passenger = passengerRepo.findById(passengerId)
                .orElseThrow(() -> new BookingException("Passenger not found"));

        // check if seat is already reserved
        boolean seatTaken = bookingRepo.findAll().stream()
                .filter(b -> b.getFlight().getId().equals(flightId))
                .anyMatch(b -> b.getSeatNumber().equalsIgnoreCase(seatNumber) && b.getStatus() == BookingStatus.CONFIRMED);
        if (seatTaken) {
            throw new BookingException("Seat already reserved");
        }

        // Update available seats
        flight.setAvailableSeats(flight.getAvailableSeats() - 1);
        //make seat available
        seatService.reserveSeat(flightId, seatNumber);
        flightRepo.save(flight);

        Booking booking = Booking.builder()
                .bookingReference(generateReference())
                .bookingDate(LocalDateTime.now())
                .seatNumber(seatNumber)
                .status(BookingStatus.PENDING)
                .flight(flight)
                .passenger(passenger)
                .build();

        return bookingRepo.save(booking);
    }

    private String generateReference() {
        return UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    public Booking cancelBooking(Long id) {
        Booking booking = bookingRepo.findById(id)
                .orElseThrow(() -> new BookingException("Booking not found"));

        if (booking.getStatus() != BookingStatus.PENDING) {
            throw new BookingException("Booking cannot be canceled, it is already processed");
        }

        booking.setStatus(BookingStatus.CANCELLED);
        return bookingRepo.save(booking);
    }

    public Booking findByReference(String ref) {
        return bookingRepo.findByBookingReference(ref)
                .orElseThrow(() -> new BookingException("Booking not found with reference: " + ref));
    }

    public Booking findById(Long id) {
        return bookingRepo.findById(id).orElseThrow(() -> new BookingException("Booking not found"));
    }
}
