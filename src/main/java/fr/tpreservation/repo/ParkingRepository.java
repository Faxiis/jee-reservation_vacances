package fr.tpreservation.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import fr.tpreservation.model.ParkingPlace;


@Repository
public interface ParkingRepository extends JpaRepository<ParkingPlace, String> {
    List<ParkingPlace> findByIsAvailableTrue();
}