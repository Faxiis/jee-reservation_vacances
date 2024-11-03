package fr.tpreservation.repo;

import fr.tpreservation.model.HotelReservation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HostelReservationRepository extends JpaRepository<HotelReservation, String> {
}
