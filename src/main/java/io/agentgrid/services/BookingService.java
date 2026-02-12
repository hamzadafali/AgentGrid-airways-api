package io.agentgrid.services;


import io.agentgrid.entities.Booking;
import io.agentgrid.entities.Flight;
import io.agentgrid.entities.Passenger;
import io.agentgrid.enums.BookingStatus;
import io.agentgrid.exceptions.BookingException;
import io.agentgrid.repositories.BookingRepository;
import io.agentgrid.repositories.FlightRepository;
import io.agentgrid.repositories.PassengerRepository;
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

    @Transactional
    public Booking createBooking(Long flightId, Long passengerId, String seatNumber) {
        Flight flight = flightRepo.findById(flightId)
                .orElseThrow(() -> new BookingException("Flight not found"));

        if (flight.getAvailableSeats() <= 0) {
            throw new BookingException("No seats available for this flight");
        }

        Passenger passenger = passengerRepo.findById(passengerId)
                .orElseThrow(() -> new BookingException("Passenger not found"));

        // Update available seats
        flight.setAvailableSeats(flight.getAvailableSeats() - 1);
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
