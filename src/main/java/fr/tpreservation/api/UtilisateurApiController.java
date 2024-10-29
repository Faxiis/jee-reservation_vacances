package fr.tpreservation.api;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import fr.tpreservation.config.JwtUtil;
import fr.tpreservation.exception.ProduitNotFoundException;
import fr.tpreservation.model.Utilisateur;
import fr.tpreservation.repo.UserRepository;
import fr.tpreservation.request.AuthRequest;
import fr.tpreservation.request.SubscribeRequest;
import fr.tpreservation.response.ReservationResponse;
import fr.tpreservation.response.UtilisateurResponse;
import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UtilisateurApiController {
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    @PostMapping("/auth")
    public String auth(@RequestBody AuthRequest request) {
        try {
            Authentication authentication = new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword());

            authentication = this.authenticationManager.authenticate(authentication);

            SecurityContextHolder.getContext().setAuthentication(authentication);

            return JwtUtil.generate(request.getUsername());
        }

        catch (BadCredentialsException e) {
            return "";
        }
    }

    @PostMapping("/subscribe")
    @ResponseStatus(HttpStatus.CREATED)
    public String subscribe(@RequestBody SubscribeRequest request) {
        Utilisateur utilisateur = new Utilisateur();

        utilisateur.setUsername(request.getUsername());
        utilisateur.setPassword(this.passwordEncoder.encode(request.getPassword()));

        this.repository.save(utilisateur);

        return utilisateur.getId();
    }

    @GetMapping("/{id}")
    public UtilisateurResponse findById(@PathVariable String id) {
        Utilisateur utilisateur = this.repository.findById(id).orElseThrow(ProduitNotFoundException::new);
        UtilisateurResponse resp = UtilisateurResponse.builder().build();

        // Copier les propriétés de l'utilisateur à la réponse
        BeanUtils.copyProperties(utilisateur, resp);

        // Ajouter les réservations à la réponse
        if (utilisateur.getReservations() != null) {
            List<ReservationResponse> reservationResponses = utilisateur.getReservations()
                .stream()
                .map(reservation -> ReservationResponse.builder()
                    .id(reservation.getId())
                    .reservationDate(reservation.getReservationDate())
                    .utilisateurId(reservation.getUser() != null ? reservation.getUser().getId() : null)
                    .build())
                .collect(Collectors.toList());

            resp.setReservations(reservationResponses);
        }

        return resp;
    }

    @PutMapping("/{id}")
    public UtilisateurResponse updateUser(@PathVariable String id, @RequestBody SubscribeRequest request) {
        Utilisateur utilisateur = this.repository.findById(id).orElseThrow(ProduitNotFoundException::new);

        // Mettre à jour les propriétés de l'utilisateur
        if (request.getUsername() != null) {
            utilisateur.setUsername(request.getUsername());
        }

        if (request.getPassword() != null) {
            utilisateur.setPassword(this.passwordEncoder.encode(request.getPassword()));
        }

        if (request.getEmail() != null) {
            utilisateur.setEmail(request.getEmail());
        }

        if (request.getFirstname() != null) {
            utilisateur.setFirstName(request.getFirstname());
        }
        
        if (request.getLastname() != null) {
            utilisateur.setLastName(request.getLastname());
        }

        this.repository.save(utilisateur);

        // Préparer la réponse
        UtilisateurResponse resp = UtilisateurResponse.builder().build();
        BeanUtils.copyProperties(utilisateur, resp);

        // Ajouter les réservations à la réponse
        if (utilisateur.getReservations() != null) {
            List<ReservationResponse> reservationResponses = utilisateur.getReservations()
                .stream()
                .map(reservation -> ReservationResponse.builder()
                    .id(reservation.getId())
                    .reservationDate(reservation.getReservationDate())
                    .utilisateurId(reservation.getUser() != null ? reservation.getUser().getId() : null)
                    .build())
                .collect(Collectors.toList());

            resp.setReservations(reservationResponses);
        }
        return resp;
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable String id) {
        Utilisateur utilisateur = this.repository.findById(id)
                .orElseThrow(ProduitNotFoundException::new);
        this.repository.delete(utilisateur);
    }
}
