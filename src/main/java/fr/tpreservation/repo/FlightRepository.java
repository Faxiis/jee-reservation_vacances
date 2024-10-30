package fr.tpreservation.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import fr.tpreservation.model.Flight;

@Repository
public interface FlightRepository extends JpaRepository<Flight, String> {
    List<Flight> findAll();
    
}