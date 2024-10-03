package fr.tpreservation.model;

import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "flight")
@Getter
@Setter
public class Flight extends Reservation {
    @Id
    @UuidGenerator
    private String id;

    @Column(nullable = false)
    private String flightNumber;

    @Column(nullable = false)
    private String departure;

    @Column(nullable = false)
    private String arrival;

    @Column(nullable = false)
    private LocalDateTime departureTime;

    @Column(nullable = false)
    private LocalDateTime arrivalTime;

    private int price;

    private int availableSeats;

    private int totalSeats;

    @JoinColumn
    @ManyToOne
    private Plane plane;

    private List<Reservation> reservations;

}
