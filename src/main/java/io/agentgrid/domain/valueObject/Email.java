package io.agentgrid.domain.valueObject;

import io.agentgrid.domain.exception.BusinessException;

import java.util.regex.Pattern;


public record Email(String value) {
    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$");

    public Email {
        // 1. D'abord normaliser
        if (value == null || value.isBlank()) {
            throw new BusinessException("Email is required");
        }
        value = value.trim().toLowerCase();  // ← NORMALISER EN PREMIER

        // 2. Ensuite valider
        if (!EMAIL_PATTERN.matcher(value).matches()) {
            throw new BusinessException("Email format is invalid");
        }
    }
}
