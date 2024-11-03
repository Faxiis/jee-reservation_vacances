package fr.tpreservation.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.tpreservation.model.Utilisateur;
import fr.tpreservation.repo.UserRepository;

@Service
public class UtilisateurService {
  
    @Autowired
    private UserRepository userRepository;

    public List<String> getRolesByUsername(String username) {
      Optional<Utilisateur> utilisateurOptional = userRepository.findByUsername(username);
      
      // Si l'utilisateur existe, retourner les noms de ses rôles sous forme de liste de chaînes
      return utilisateurOptional
              .map(utilisateur -> 
                  utilisateur.getRoles().stream()
                      .map(role -> role.getName()) // Extraction du nom de chaque rôle
                      .toList() // Conversion en liste
              )
              .orElseGet(List::of); // Retourner une liste vide si l'utilisateur n'existe pas
  }
}
