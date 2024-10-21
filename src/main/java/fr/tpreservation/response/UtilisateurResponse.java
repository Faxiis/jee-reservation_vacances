package fr.tpreservation.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class UtilisateurResponse {
    private String id;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private List<ReservationResponse> reservations;
}
