package fr.tpreservation.repo;

import fr.tpreservation.model.FlightReservation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FlightReservationRepository extends JpaRepository<FlightReservation, String> {
}
