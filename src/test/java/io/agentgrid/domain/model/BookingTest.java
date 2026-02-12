package io.agentgrid.domain.model;

import io.agentgrid.domain.exception.BusinessException;
import io.agentgrid.domain.valueObject.Money;
import io.agentgrid.domain.valueObject.SeatNumber;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.*;
import java.util.Currency;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class BookingTest {

    private static final Money AMOUNT = new Money(new BigDecimal("80.00"), Currency.getInstance("USD"));

    @Test
    void create_sets_initial_state() {
        Clock clock = Clock.fixed(Instant.parse("2030-01-01T10:00:00Z"), ZoneOffset.UTC);
        Booking booking = Booking.create(UUID.randomUUID(), UUID.randomUUID(), new SeatNumber("12A"), clock);

        assertThat(booking.status()).isEqualTo(Booking.BookingStatus.CREATED);
        assertThat(booking.bookedAt()).isEqualTo(LocalDateTime.ofInstant(clock.instant(), ZoneOffset.UTC));
        assertThat(booking.reference().value()).isNotBlank();
    }

    @Test
    void confirm_checkin_and_cancel_rules() {
        Clock clock = Clock.fixed(Instant.parse("2030-01-01T10:00:00Z"), ZoneOffset.UTC);
        Booking booking = Booking.create(UUID.randomUUID(), UUID.randomUUID(), new SeatNumber("12A"), clock);

        booking.confirm();
        assertThat(booking.status()).isEqualTo(Booking.BookingStatus.CONFIRMED);

        booking.checkIn();
        assertThat(booking.status()).isEqualTo(Booking.BookingStatus.CHECKED_IN);

        assertThatThrownBy(booking::cancel)
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("Only CREATED or CONFIRMED booking can be cancelled");
    }

    @Test
    void cancel_rejects_paid_booking() {
        Clock clock = Clock.fixed(Instant.parse("2030-01-01T10:00:00Z"), ZoneOffset.UTC);
        Booking booking = Booking.create(UUID.randomUUID(), UUID.randomUUID(), new SeatNumber("12A"), clock);
        booking.pay(AMOUNT, clock);

        assertThatThrownBy(booking::cancel)
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("Cannot cancel a paid booking");
    }

    @Test
    void pay_and_change_seat_rules() {
        Clock clock = Clock.fixed(Instant.parse("2030-01-01T10:00:00Z"), ZoneOffset.UTC);
        Booking booking = Booking.create(UUID.randomUUID(), UUID.randomUUID(), new SeatNumber("12A"), clock);

        booking.pay(AMOUNT, clock);
        assertThat(booking.paidAmount()).isEqualTo(AMOUNT);
        assertThat(booking.paidAt()).isEqualTo(LocalDateTime.ofInstant(clock.instant(), ZoneOffset.UTC));

        assertThatThrownBy(() -> booking.pay(AMOUNT, clock))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("Booking already paid");

        booking.confirm();
        booking.changeSeat(new SeatNumber("12B"));
        assertThat(booking.seat().value()).isEqualTo("12B");

        booking.checkIn();
        assertThatThrownBy(() -> booking.changeSeat(new SeatNumber("12C")))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("Seat can only be changed before check-in");
    }

    @Test
    void change_seat_requires_different_seat() {
        Clock clock = Clock.fixed(Instant.parse("2030-01-01T10:00:00Z"), ZoneOffset.UTC);
        Booking booking = Booking.create(UUID.randomUUID(), UUID.randomUUID(), new SeatNumber("12A"), clock);

        assertThatThrownBy(() -> booking.changeSeat(new SeatNumber("12A")))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("New seat must be different");
    }
}
