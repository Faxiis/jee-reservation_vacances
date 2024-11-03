package fr.tpreservation.repo;

import fr.tpreservation.model.CarReservation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarReservationRepository  extends JpaRepository<CarReservation, String> {
}
