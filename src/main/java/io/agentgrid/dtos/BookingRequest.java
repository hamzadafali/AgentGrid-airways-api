package io.agentgrid.dtos;

import lombok.Data;

@Data
public class BookingRequest {
    private Long flightId;
    private Long passengerId;
    private String seatNumber;
}
