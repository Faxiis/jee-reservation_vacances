package fr.tpreservation.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import fr.tpreservation.model.Hotel;

public interface HostelRepository extends JpaRepository<Hotel, String> {
}
