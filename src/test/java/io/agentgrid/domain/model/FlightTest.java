package io.agentgrid.domain.model;

import io.agentgrid.domain.exception.BusinessException;
import io.agentgrid.domain.valueObject.FlightNumber;
import io.agentgrid.domain.valueObject.Money;
import io.agentgrid.domain.valueObject.SeatNumber;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.*;
import java.util.Currency;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class FlightTest {

    private static final Money BASE_PRICE = new Money(new BigDecimal("120.00"), Currency.getInstance("USD"));

    private Flight buildFlight(LocalDateTime departure, LocalDateTime arrival, int capacity) {
        return Flight.schedule(
                new FlightNumber("AF123"),
                "Air France",
                "CDG",
                "JFK",
                departure,
                arrival,
                capacity,
                BASE_PRICE
        );
    }

    @Test
    void schedule_sets_initial_state() {
        LocalDateTime departure = LocalDateTime.of(2030, 1, 1, 10, 0);
        Flight flight = buildFlight(departure, departure.plusHours(2), 2);

        assertThat(flight.status()).isEqualTo(Flight.FlightStatus.SCHEDULED);
        assertThat(flight.availableSeats()).isEqualTo(2);
        assertThat(flight.origin()).isEqualTo("CDG");
        assertThat(flight.destination()).isEqualTo("JFK");
    }

    @Test
    void route_and_schedule_rules_are_enforced() {
        LocalDateTime departure = LocalDateTime.of(2030, 1, 1, 10, 0);
        LocalDateTime arrival = departure.plusHours(2);
        Flight flight = buildFlight(departure, arrival, 2);

        assertThatThrownBy(() -> flight.setRoute("AF", "CDG", "cdg"))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("Origin and destination must be different");

        assertThatThrownBy(() -> flight.setSchedule(arrival, departure))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("Departure must be before arrival");
    }

    @Test
    void reserve_and_release_seat_rules() {
        LocalDateTime departure = LocalDateTime.of(2030, 1, 1, 10, 0);
        LocalDateTime arrival = departure.plusHours(2);
        Flight flight = buildFlight(departure, arrival, 1);
        Clock beforeDeparture = Clock.fixed(departure.minusHours(1).toInstant(ZoneOffset.UTC), ZoneOffset.UTC);

        flight.reserveSeat(new SeatNumber("1A"), beforeDeparture);
        assertThat(flight.isFull()).isTrue();

        assertThatThrownBy(() -> flight.reserveSeat(new SeatNumber("1B"), beforeDeparture))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("flight is full");

        assertThatThrownBy(() -> flight.reserveSeat(new SeatNumber("1A"), beforeDeparture))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("Seat already reserved");

        flight.releaseSeat(new SeatNumber("1A"));
        assertThat(flight.availableSeats()).isEqualTo(1);

        assertThatThrownBy(() -> flight.releaseSeat(new SeatNumber("1A")))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("Seat was not reserved");
    }

    @Test
    void reserve_seat_rejects_cancelled_or_after_departure() {
        LocalDateTime departure = LocalDateTime.of(2030, 1, 1, 10, 0);
        LocalDateTime arrival = departure.plusHours(2);
        Flight flight = buildFlight(departure, arrival, 2);
        Clock afterDeparture = Clock.fixed(departure.plusMinutes(1).toInstant(ZoneOffset.UTC), ZoneOffset.UTC);

        assertThatThrownBy(() -> flight.reserveSeat(new SeatNumber("2A"), afterDeparture))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("Cannot reserve seat after departure time");

        flight.cancel();
        Clock beforeDeparture = Clock.fixed(departure.minusHours(1).toInstant(ZoneOffset.UTC), ZoneOffset.UTC);

        assertThatThrownBy(() -> flight.reserveSeat(new SeatNumber("2A"), beforeDeparture))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("Cannot reserve seat on a cancelled flight");
    }

    @Test
    void status_transitions_are_time_bound() {
        LocalDateTime departure = LocalDateTime.of(2030, 1, 1, 10, 0);
        LocalDateTime arrival = departure.plusHours(2);
        Flight flight = buildFlight(departure, arrival, 2);

        Clock beforeDeparture = Clock.fixed(departure.minusMinutes(10).toInstant(ZoneOffset.UTC), ZoneOffset.UTC);
        flight.startBoarding(beforeDeparture);
        assertThat(flight.status()).isEqualTo(Flight.FlightStatus.BOARDING);

        Clock atDeparture = Clock.fixed(departure.toInstant(ZoneOffset.UTC), ZoneOffset.UTC);
        flight.depart(atDeparture);
        assertThat(flight.status()).isEqualTo(Flight.FlightStatus.DEPARTED);

        Clock beforeArrival = Clock.fixed(arrival.minusMinutes(5).toInstant(ZoneOffset.UTC), ZoneOffset.UTC);
        assertThatThrownBy(() -> flight.arrive(beforeArrival))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("Cannot arrive before arrival time");

        Clock atArrival = Clock.fixed(arrival.toInstant(ZoneOffset.UTC), ZoneOffset.UTC);
        flight.arrive(atArrival);
        assertThat(flight.status()).isEqualTo(Flight.FlightStatus.ARRIVED);
    }

    @Test
    void release_seat_rejects_after_departure() {
        LocalDateTime departure = LocalDateTime.of(2030, 1, 1, 10, 0);
        LocalDateTime arrival = departure.plusHours(2);
        Flight flight = buildFlight(departure, arrival, 2);
        Clock beforeDeparture = Clock.fixed(departure.minusHours(1).toInstant(ZoneOffset.UTC), ZoneOffset.UTC);

        flight.reserveSeat(new SeatNumber("1A"), beforeDeparture);

        Clock atDeparture = Clock.fixed(departure.toInstant(ZoneOffset.UTC), ZoneOffset.UTC);
        flight.depart(atDeparture);

        assertThatThrownBy(() -> flight.releaseSeat(new SeatNumber("1A")))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("Cannot release seat after departure");
    }
}
