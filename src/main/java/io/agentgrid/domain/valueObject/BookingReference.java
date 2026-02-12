package io.agentgrid.domain.valueObject;

import io.agentgrid.domain.exception.BusinessException;

import java.util.UUID;

public record BookingReference(String value) {
    // Référence stable et non devinable (pratique)
    public static BookingReference newRef() {
        return new BookingReference(UUID.randomUUID().toString().replace("-", "").substring(0, 10).toUpperCase());
    }
    public BookingReference {
        if (value == null || value.isBlank()) throw new BusinessException("Booking reference is required");
        value = value.trim().toUpperCase();
        if (value.length() < 6 || value.length() > 12) throw new BusinessException("Booking reference length invalid");
    }
}
