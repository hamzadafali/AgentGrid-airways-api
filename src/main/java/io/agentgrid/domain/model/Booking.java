package io.agentgrid.domain.model;


import io.agentgrid.domain.valueObject.BookingReference;
import io.agentgrid.domain.valueObject.Money;
import io.agentgrid.domain.valueObject.SeatNumber;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

import static io.agentgrid.domain.util.DomainValidator.nn;
import static io.agentgrid.domain.util.DomainValidator.require;


public final class Booking {

    public enum BookingStatus { CREATED, CONFIRMED, CANCELLED, CHECKED_IN }

    private final UUID id;
    private final BookingReference reference;
    private final LocalDateTime bookedAt;

    private final UUID flightId;
    private final UUID passengerId;

    private SeatNumber seat;
    private BookingStatus status;

    private Money paidAmount; // null si non payé
    private LocalDateTime paidAt;

    private Booking(UUID id,
                    BookingReference reference,
                    LocalDateTime bookedAt,
                    UUID flightId,
                    UUID passengerId,
                    SeatNumber seat) {

        this.id = nn(id, "Booking id is required");
        this.reference = nn(reference, "Booking reference is required");
        this.bookedAt = nn(bookedAt, "BookedAt is required");
        this.flightId = nn(flightId, "FlightId is required");
        this.passengerId = nn(passengerId, "PassengerId is required");
        this.seat = nn(seat, "Seat is required");
        this.status = BookingStatus.CREATED;
    }

    public static Booking create(UUID flightId, UUID passengerId, SeatNumber seat, Clock clock) {
        nn(clock, "Clock is required");
        return new Booking(
                UUID.randomUUID(),
                BookingReference.newRef(),
                LocalDateTime.now(clock),
                flightId,
                passengerId,
                seat
        );
    }

    // -------- Business transitions --------

    public void confirm() {
        require(status == BookingStatus.CREATED, "Only CREATED booking can be confirmed");
        this.status = BookingStatus.CONFIRMED;
    }

    public void checkIn() {
        require(status == BookingStatus.CONFIRMED, "Only CONFIRMED booking can be checked-in");
        this.status = BookingStatus.CHECKED_IN;
    }

    public void cancel() {
        require(status == BookingStatus.CREATED || status == BookingStatus.CONFIRMED,
                "Only CREATED or CONFIRMED booking can be cancelled");
        require(paidAmount == null, "Cannot cancel a paid booking (refund flow required)");
        this.status = BookingStatus.CANCELLED;
    }

    public void pay(Money amount, Clock clock) {
        nn(clock, "Clock is required");
        nn(amount, "Payment amount is required");
        require(status != BookingStatus.CANCELLED, "Cannot pay a cancelled booking");
        require(paidAmount == null, "Booking already paid");
        this.paidAmount = amount;
        this.paidAt = LocalDateTime.now(clock);
    }

    public void changeSeat(SeatNumber newSeat) {
        nn(newSeat, "Seat is required");
        require(status == BookingStatus.CREATED || status == BookingStatus.CONFIRMED,
                "Seat can only be changed before check-in");
        require(!newSeat.equals(this.seat), "New seat must be different");
        this.seat = newSeat;
    }

    // -------- Getters --------
    public UUID id() { return id; }
    public BookingReference reference() { return reference; }
    public LocalDateTime bookedAt() { return bookedAt; }
    public UUID flightId() { return flightId; }
    public UUID passengerId() { return passengerId; }
    public SeatNumber seat() { return seat; }
    public BookingStatus status() { return status; }
    public Money paidAmount() { return paidAmount; }
    public LocalDateTime paidAt() { return paidAt; }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Booking other)) return false;
        return id.equals(other.id);
    }
    @Override public int hashCode() { return Objects.hash(id); }
}

