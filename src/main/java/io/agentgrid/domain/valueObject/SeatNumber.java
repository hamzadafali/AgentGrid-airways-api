package io.agentgrid.domain.valueObject;

import io.agentgrid.domain.exception.BusinessException;

import java.util.regex.Pattern;

public record SeatNumber(String value) {
    // Simplifié : 1-60 + A-F (selon avions)
    private static final Pattern P = Pattern.compile("^(?:[1-9]|[1-5]\\d|60)[A-F]$");
    public SeatNumber {
        if (value == null || value.isBlank()) throw new BusinessException("Seat number is required");
        value = value.trim().toUpperCase();
        if (!P.matcher(value).matches()) throw new BusinessException("Seat number format invalid (e.g., 12A)");
    }

    public int row() { return Integer.parseInt(value.substring(0, value.length() - 1)); }
    public char letter() { return value.charAt(value.length() - 1); }
}
