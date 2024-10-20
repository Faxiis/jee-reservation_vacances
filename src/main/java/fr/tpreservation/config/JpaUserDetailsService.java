package fr.tpreservation.config;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import fr.tpreservation.model.Utilisateur;
import fr.tpreservation.repo.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class JpaUserDetailsService implements UserDetailsService {
  private final UserRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Utilisateur utilisateur = this.repository
            .findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("L'utilisateur n'existe pas"));
        
        return User.withUsername(username)
            .password(utilisateur.getPassword())
            .build()
        ;
    }
}
