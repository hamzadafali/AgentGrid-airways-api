package io.agentgrid.infrastructure.mapper;

import io.agentgrid.infrastructure.persistence.entity.SeatEntity;
import org.springframework.stereotype.Component;

@Component
public class SeatMapper {

    /**
     * Créer un SeatEntity à partir des paramètres principaux
     */
    public SeatEntity createSeatEntity(String seatNumber, boolean isAvailable) {
        return SeatEntity.builder()
                .seatNumber(seatNumber)
                .isAvailable(isAvailable)
                .build();
    }

    /**
     * Créer un SeatEntity avec tous les paramètres
     */
    public SeatEntity toEntity(String seatNumber, boolean isAvailable) {
        return createSeatEntity(seatNumber, isAvailable);
    }
}

