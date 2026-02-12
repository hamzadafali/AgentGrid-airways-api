package io.agentgrid.domain.util;

import io.agentgrid.domain.exception.BusinessException;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class DomainValidatorTest {

    @Test
    void require_and_nn_enforce_conditions() {
        DomainValidator.require(true, "ok");
        assertThat(DomainValidator.nn("x", "msg")).isEqualTo("x");

        assertThatThrownBy(() -> DomainValidator.require(false, "boom"))
                .isInstanceOf(BusinessException.class)
                .hasMessage("boom");

        assertThatThrownBy(() -> DomainValidator.nn(null, "missing"))
                .isInstanceOf(BusinessException.class)
                .hasMessage("missing");
    }

    @Test
    void requireNotBlank_rejects_blank() {
        DomainValidator.requireNotBlank("ok", "msg");

        assertThatThrownBy(() -> DomainValidator.requireNotBlank(" ", "blank"))
                .isInstanceOf(BusinessException.class)
                .hasMessage("blank");
    }

    @Test
    void requireAdult_checks_age() {
        LocalDate todayMinus16 = LocalDate.now().minusYears(16).minusDays(1);
        DomainValidator.requireAdult(todayMinus16, 16);

        LocalDate tooYoung = LocalDate.now().minusYears(15);
        assertThatThrownBy(() -> DomainValidator.requireAdult(tooYoung, 16))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("Passenger must be at least 16 years old");
    }
}
