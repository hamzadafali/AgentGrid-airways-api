package io.agentgrid.domain.service;

import io.agentgrid.infrastructure.persistence.entity.SeatEntity;
import io.agentgrid.infrastructure.repository.SeatRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SeatService {

    private final SeatRepository seatRepository;

    public SeatService(SeatRepository seatRepository) {
        this.seatRepository = seatRepository;
    }

    /**
     * Trouver un siège par son ID
     */
    public Optional<SeatEntity> findById(Long seatId) {
        return seatRepository.findById(seatId);
    }

    /**
     * Sauvegarder un siège
     */
    public SeatEntity save(SeatEntity seat) {
        return seatRepository.save(seat);
    }

    /**
     * Vérifier si un siège est disponible
     */
    public boolean isSeatAvailable(Long flightId, String seatNumber) {
        // Logique pour vérifier si le siège est disponible pour le vol donné
        Optional<SeatEntity> seat = seatRepository.findBySeatNumberAndFlightId(seatNumber, flightId);
        return seat.isPresent() && seat.get().isAvailable();
    }

    /**
     * Réserver un siège (marquer comme occupé)
     */
    public void reserveSeat(Long flightId, String seatNumber) {
        Optional<SeatEntity> seat = seatRepository.findBySeatNumberAndFlightId(seatNumber, flightId);
        if (seat.isPresent()) {
            seat.get().setAvailable(false);
            seatRepository.save(seat.get());
        }
    }

    /**
     * Libérer un siège (marquer comme disponible)
     */
    public void releaseSeat(Long flightId, String seatNumber) {
        Optional<SeatEntity> seat = seatRepository.findBySeatNumberAndFlightId(seatNumber, flightId);
        if (seat.isPresent()) {
            seat.get().setAvailable(true);
            seatRepository.save(seat.get());
        }
    }

}
