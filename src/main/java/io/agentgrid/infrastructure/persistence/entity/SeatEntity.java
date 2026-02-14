package io.agentgrid.infrastructure.persistence.entity;


import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "seats")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SeatEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String seatNumber; // Ex: "12A", "15C"

   /* @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SeatClass seatClass; // ECONOMY, BUSINESS, FIRST*/

    @Column(nullable = false)
    private boolean isAvailable = true;

    // Relation avec le Vol : Un siège appartient à un vol spécifique
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "flight_id", nullable = false)
    private Flight flight;

    /*// Optionnel : Type de siège pour plus de précision
    @Enumerated(EnumType.STRING)
    private SeatType type; // WINDOW, AISLE, MIDDLE*/
}
