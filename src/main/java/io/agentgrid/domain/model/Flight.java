package io.agentgrid.domain.model;


import io.agentgrid.domain.valueObject.FlightNumber;
import io.agentgrid.domain.valueObject.Money;
import io.agentgrid.domain.valueObject.SeatNumber;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.*;
import static io.agentgrid.domain.util.DomainValidator.*;


public final class Flight {

    public enum FlightStatus { SCHEDULED, BOARDING, DEPARTED, ARRIVED, CANCELLED }

    private final UUID id;
    private final FlightNumber flightNumber;

    private String airline;
    private String origin;
    private String destination;

    private LocalDateTime departureAt;
    private LocalDateTime arrivalAt;

    private int capacity;
    private Money basePrice;

    private FlightStatus status;

    // Pour garantir siège unique (sans DB)
    private final Set<SeatNumber> occupiedSeats = new HashSet<>();

    private Flight(UUID id,
                   FlightNumber flightNumber,
                   String airline,
                   String origin,
                   String destination,
                   LocalDateTime departureAt,
                   LocalDateTime arrivalAt,
                   int capacity,
                   Money basePrice) {

        this.id = nn(id, "Flight id is required");
        this.flightNumber = nn(flightNumber, "Flight number is required");

        setRoute(airline, origin, destination);
        setSchedule(departureAt, arrivalAt);
        setCapacity(capacity);
        setBasePrice(basePrice);

        this.status = FlightStatus.SCHEDULED;
    }

    public static Flight schedule(FlightNumber flightNumber,
                                  String airline,
                                  String origin,
                                  String destination,
                                  LocalDateTime departureAt,
                                  LocalDateTime arrivalAt,
                                  int capacity,
                                  Money basePrice) {

        return new Flight(UUID.randomUUID(), flightNumber, airline, origin, destination, departureAt, arrivalAt, capacity, basePrice);
    }

    // -------- Business rules setters --------

    public void setRoute(String airline, String origin, String destination) {
        requireNotBlank(airline, "Airline is required");
        requireNotBlank(origin, "Origin is required");
        requireNotBlank(destination, "Destination is required");
        require(!origin.trim().equalsIgnoreCase(destination.trim()), "Origin and destination must be different");
        this.airline = airline.trim();
        this.origin = origin.trim().toUpperCase();
        this.destination = destination.trim().toUpperCase();
    }

    public void setSchedule(LocalDateTime departureAt, LocalDateTime arrivalAt) {
        nn(departureAt, "Departure datetime is required");
        nn(arrivalAt, "Arrival datetime is required");
        require(departureAt.isBefore(arrivalAt), "Departure must be before arrival");
        this.departureAt = departureAt;
        this.arrivalAt = arrivalAt;
    }

    public void setCapacity(int capacity) {
        require(capacity > 0, "Capacity must be > 0");
        require(occupiedSeats.size() <= capacity, "Capacity cannot be less than already occupied seats");
        this.capacity = capacity;
    }

    public void setBasePrice(Money basePrice) {
        this.basePrice = nn(basePrice, "Base price is required");
    }

    // -------- Seat / booking behavior --------

    public boolean isFull() {
        return occupiedSeats.size() >= capacity;
    }

    public int availableSeats() {
        return capacity - occupiedSeats.size();
    }

    public void reserveSeat(SeatNumber seat, Clock clock) {
        nn(clock, "Clock is required");
        nn(seat, "Seat is required");

        require(status != FlightStatus.CANCELLED, "Cannot reserve seat on a cancelled flight");
        require(LocalDateTime.now(clock).isBefore(departureAt), "Cannot reserve seat after departure time");

        // 1. D'abord, on vérifie si le siège est déjà pris
        // Note : .contains() permet de vérifier sans modifier l'état si on veut être strict,
        // mais ici on peut simplement vérifier si le siège existe déjà avant de checker la capacité.
        require(!occupiedSeats.contains(seat), "Seat already reserved: " + seat.value());

        // 2. Ensuite, on vérifie si on a de la place pour un nouveau siège
        require(!isFull(), "Cannot reserve seat: flight is full");

        occupiedSeats.add(seat);
    }

    public void releaseSeat(SeatNumber seat) {
        nn(seat, "Seat is required");
        require(status != FlightStatus.DEPARTED && status != FlightStatus.ARRIVED, "Cannot release seat after departure");
        boolean removed = occupiedSeats.remove(seat);
        require(removed, "Seat was not reserved: " + seat.value());
    }

    // -------- Status transitions --------

    public void startBoarding(Clock clock) {
        nn(clock, "Clock is required");
        require(status == FlightStatus.SCHEDULED, "Only SCHEDULED flight can start boarding");
        require(LocalDateTime.now(clock).isBefore(departureAt), "Boarding must start before departure");
        status = FlightStatus.BOARDING;
    }

    public void depart(Clock clock) {
        nn(clock, "Clock is required");
        require(status == FlightStatus.BOARDING || status == FlightStatus.SCHEDULED, "Flight must be boarding or scheduled to depart");
        require(!LocalDateTime.now(clock).isBefore(departureAt), "Cannot depart before departure time");
        status = FlightStatus.DEPARTED;
    }

    public void arrive(Clock clock) {
        nn(clock, "Clock is required");
        require(status == FlightStatus.DEPARTED, "Only DEPARTED flight can arrive");
        require(!LocalDateTime.now(clock).isBefore(arrivalAt), "Cannot arrive before arrival time");
        status = FlightStatus.ARRIVED;
    }

    public void cancel() {
        require(status == FlightStatus.SCHEDULED || status == FlightStatus.BOARDING, "Cannot cancel after departure/arrival");
        status = FlightStatus.CANCELLED;
    }

    // -------- Getters --------

    public UUID id() { return id; }
    public FlightNumber flightNumber() { return flightNumber; }
    public String airline() { return airline; }
    public String origin() { return origin; }
    public String destination() { return destination; }
    public LocalDateTime departureAt() { return departureAt; }
    public LocalDateTime arrivalAt() { return arrivalAt; }
    public int capacity() { return capacity; }
    public Money basePrice() { return basePrice; }
    public FlightStatus status() { return status; }

    public Set<SeatNumber> occupiedSeatsSnapshot() {
        return Collections.unmodifiableSet(occupiedSeats);
    }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Flight other)) return false;
        return id.equals(other.id);
    }
    @Override public int hashCode() { return Objects.hash(id); }
}

