package fr.tpreservation.repo;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import fr.tpreservation.model.Utilisateur;

public interface UserRepository extends JpaRepository<Utilisateur, String> {
    Optional<Utilisateur> findByUsername(String username);
}
