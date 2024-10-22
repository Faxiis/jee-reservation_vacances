package fr.tpreservation.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository; // Correct import for Optional

import fr.tpreservation.model.Car;

public interface CarRepository extends JpaRepository<Car, String> {
    List<Car> findAll();
}
