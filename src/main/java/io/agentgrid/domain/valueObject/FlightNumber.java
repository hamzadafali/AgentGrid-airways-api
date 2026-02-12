package io.agentgrid.domain.valueObject;

import io.agentgrid.domain.exception.BusinessException;

import java.util.regex.Pattern;

public record FlightNumber(String value) {
    // Simplifié : XX1234 (IATA airline code + digits)
    private static final Pattern P = Pattern.compile("^[A-Z]{2}\\d{2,4}$");
    public FlightNumber {
        if (value == null || value.isBlank()) throw new BusinessException("Flight number is required");
        value = value.trim().toUpperCase();
        if (!P.matcher(value).matches()) throw new BusinessException("Flight number format is invalid");
    }
}
