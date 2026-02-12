package io.agentgrid.api.dto.Request;

import lombok.Data;

@Data
public class BookingRequest {
    private Long flightId;
    private Long passengerId;
    private String seatNumber;
}
