package io.agentgrid.infrastructure.persistence.entity;

import io.agentgrid.domain.enums.FlightStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "flights")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Flight {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    @NotBlank
    private String flightNumber;

    @NotBlank
    private String airline;

    @NotBlank
    private String origin;

    @NotBlank
    private String destination;

    @NotNull
    private LocalDateTime departureDateTime;

    @NotNull
    private LocalDateTime arrivalDateTime;

    @Min(0)
    private Integer totalSeats;

    @Min(0)
    private Integer availableSeats;

    @Positive
    @NotNull
    private BigDecimal price;

    @Enumerated(EnumType.STRING)
    private FlightStatus status;

}
