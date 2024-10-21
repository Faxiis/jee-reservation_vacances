package fr.tpreservation.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class ReservationResponse {
    private String id;
    private LocalDateTime reservationDate;
    private String utilisateurId;
}
