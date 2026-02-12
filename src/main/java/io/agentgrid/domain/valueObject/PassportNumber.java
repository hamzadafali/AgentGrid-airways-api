package io.agentgrid.domain.valueObject;

import io.agentgrid.domain.exception.BusinessException;

import java.util.regex.Pattern;

public record PassportNumber(String value) {
    // Simplifié : 6 à 12 alphanum. (selon pays c’est variable)
    private static final Pattern P = Pattern.compile("^[A-Z0-9]{6,12}$");
    public PassportNumber {
        if (value == null || value.isBlank()) throw new BusinessException("Passport number is required");
        value = value.trim().toUpperCase();
        if (!P.matcher(value).matches()) throw new BusinessException("Passport number format is invalid");
    }
}