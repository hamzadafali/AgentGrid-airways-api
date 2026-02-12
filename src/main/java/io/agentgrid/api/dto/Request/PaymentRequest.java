package io.agentgrid.api.dto.Request;

import io.agentgrid.domain.enums.PaymentMethod;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class PaymentRequest {
    @NotNull
    private Long bookingId;

    @NotNull
    @Positive
    private Double amount;

    @NotNull
    private PaymentMethod method;
}
