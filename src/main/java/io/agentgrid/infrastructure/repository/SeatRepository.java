package io.agentgrid.infrastructure.repository;

import io.agentgrid.infrastructure.persistence.entity.SeatEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SeatRepository extends JpaRepository<SeatEntity, Long> {

    /**
     * Trouver un siège par numéro de siège et ID de vol
     */
    Optional<SeatEntity> findBySeatNumberAndFlightId(String seatNumber, Long flightId);

    /**
     * Trouver tous les sièges d'un vol donné
     */
    List<SeatEntity> findByFlightId(Long flightId);

    /**
     * Trouver tous les sièges disponibles d'un vol donné
     */
    List<SeatEntity> findByFlightIdAndIsAvailableTrue(Long flightId);

    /**
     * Trouver tous les sièges occupés d'un vol donné
     */
    List<SeatEntity> findByFlightIdAndIsAvailableFalse(Long flightId);

    /**
     * Compter les sièges disponibles pour un vol donné
     */
    long countByFlightIdAndIsAvailableTrue(Long flightId);

}

