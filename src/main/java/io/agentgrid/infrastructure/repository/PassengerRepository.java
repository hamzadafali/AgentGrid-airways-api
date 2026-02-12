package io.agentgrid.infrastructure.repository;

import io.agentgrid.infrastructure.persistence.entity.Passenger;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PassengerRepository extends JpaRepository<Passenger, Long> {

    Optional<Passenger> findByPassportNumber(String passport);

    Optional<Passenger> findByEmail(String email);
}
