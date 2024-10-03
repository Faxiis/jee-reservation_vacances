package fr.tpreservation.model;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class FlightReservation extends Reservation {
    @ManyToOne
    @JoinColumn(name = "flight_id")
    private Flight flight;
}
