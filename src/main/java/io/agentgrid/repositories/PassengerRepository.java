package io.agentgrid.repositories;

import io.agentgrid.entities.Passenger;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PassengerRepository extends JpaRepository<Passenger, Long> {

    Optional<Passenger> findByPassportNumber(String passport);

    Optional<Passenger> findByEmail(String email);
}
