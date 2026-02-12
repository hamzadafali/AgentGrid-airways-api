package io.agentgrid.domain.valueObject;

import io.agentgrid.domain.exception.BusinessException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Currency;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ValueObjectsTest {

    @Test
    void email_normalizes_and_validates() {
        Email email = new Email("  John.Doe@Example.Com  ");
        assertThat(email.value()).isEqualTo("john.doe@example.com");

        assertThatThrownBy(() -> new Email(""))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("Email is required");

        assertThatThrownBy(() -> new Email("not-an-email"))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("Email format is invalid");
    }

    @Test
    void passportNumber_normalizes_and_validates() {
        PassportNumber passport = new PassportNumber(" ab123c ");
        assertThat(passport.value()).isEqualTo("AB123C");

        assertThatThrownBy(() -> new PassportNumber(null))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("Passport number is required");

        assertThatThrownBy(() -> new PassportNumber("12"))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("Passport number format is invalid");
    }

    @Test
    void seatNumber_normalizes_and_parses() {
        SeatNumber seat = new SeatNumber(" 12a ");
        assertThat(seat.value()).isEqualTo("12A");
        assertThat(seat.row()).isEqualTo(12);
        assertThat(seat.letter()).isEqualTo('A');

        assertThatThrownBy(() -> new SeatNumber("0A"))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("Seat number format invalid");
    }

    @Test
    void flightNumber_normalizes_and_validates() {
        FlightNumber flightNumber = new FlightNumber(" af123 ");
        assertThat(flightNumber.value()).isEqualTo("AF123");

        assertThatThrownBy(() -> new FlightNumber("A123"))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("Flight number format is invalid");
    }

    @Test
    void bookingReference_newRef_and_validates_length() {
        BookingReference ref = BookingReference.newRef();
        assertThat(ref.value()).hasSize(10);
        assertThat(ref.value()).isUpperCase();

        assertThatThrownBy(() -> new BookingReference("abc"))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("Booking reference length invalid");
    }

    @Test
    void money_validates_and_adds() {
        Money usd = new Money(new BigDecimal("10.50"), Currency.getInstance("USD"));
        Money usd2 = Money.of("2.25", "USD");

        assertThat(usd.add(usd2).amount()).isEqualByComparingTo("12.75");
        assertThat(usd.add(usd2).currency()).isEqualTo(Currency.getInstance("USD"));

        assertThatThrownBy(() -> new Money(new BigDecimal("1.234"), Currency.getInstance("USD")))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("Money scale must be <= 2");

        assertThatThrownBy(() -> new Money(new BigDecimal("-1.00"), Currency.getInstance("USD")))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("Money cannot be negative");

        Money eur = Money.of("1.00", "EUR");
        assertThatThrownBy(() -> usd.add(eur))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("Cannot add different currencies");
    }
}
