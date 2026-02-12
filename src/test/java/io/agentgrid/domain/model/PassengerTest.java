package io.agentgrid.domain.model;

import io.agentgrid.domain.exception.BusinessException;
import io.agentgrid.domain.valueObject.Email;
import io.agentgrid.domain.valueObject.PassportNumber;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PassengerTest {

    @Test
    void create_sets_fields_and_normalizes() {
        LocalDate dob = LocalDate.now().minusYears(20);
        Passenger passenger = Passenger.create(
                "  Jane ",
                " Doe ",
                new Email("JANE.DOE@EXAMPLE.COM"),
                new PassportNumber("ab1234"),
                dob,
                "  +1 555 000 "
        );

        assertThat(passenger.firstName()).isEqualTo("Jane");
        assertThat(passenger.lastName()).isEqualTo("Doe");
        assertThat(passenger.email().value()).isEqualTo("jane.doe@example.com");
        assertThat(passenger.passportNumber().value()).isEqualTo("AB1234");
        assertThat(passenger.phoneNumber()).isEqualTo("+1 555 000");
    }

    @Test
    void create_rejects_underage() {
        LocalDate dob = LocalDate.now().minusYears(15);

        assertThatThrownBy(() -> Passenger.create(
                "Jane",
                "Doe",
                new Email("jane@example.com"),
                new PassportNumber("AB1234"),
                dob,
                null
        ))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("Passenger must be at least 16 years old");
    }

    @Test
    void setPhoneNumber_allows_blank() {
        LocalDate dob = LocalDate.now().minusYears(20);
        Passenger passenger = Passenger.create(
                "Jane",
                "Doe",
                new Email("jane@example.com"),
                new PassportNumber("AB1234"),
                dob,
                ""
        );

        assertThat(passenger.phoneNumber()).isNull();
    }
}
