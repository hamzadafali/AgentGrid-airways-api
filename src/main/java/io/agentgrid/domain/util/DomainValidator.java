package io.agentgrid.domain.util;

import io.agentgrid.domain.exception.BusinessException;

import java.time.LocalDate;

public class DomainValidator {


    public static void require(boolean condition, String message) {
        if (!condition) throw new BusinessException(message);
    }

    public static <T> T nn(T v, String message) {
        if (v == null) throw new BusinessException(message);
        return v;
    }

    public static void requireNotBlank(String v, String message) {
        if (v == null || v.isBlank()) throw new BusinessException(message);
    }

    public static void requireAdult(LocalDate dob, int minAgeYears) {
        nn(dob, "Date of birth is required");
        LocalDate today = LocalDate.now();
        int age = today.getYear() - dob.getYear() - ((today.getDayOfYear() < dob.getDayOfYear()) ? 1 : 0);
        if (age < minAgeYears) throw new BusinessException("Passenger must be at least " + minAgeYears + " years old");
    }
}

