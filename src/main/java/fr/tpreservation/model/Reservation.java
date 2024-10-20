package fr.tpreservation.model;

import java.time.LocalDateTime;

import org.hibernate.annotations.UuidGenerator;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Reservation {
    @Id
    @UuidGenerator
    private String id;

    @Column(nullable = false)
    private LocalDateTime reservationDate;

    @JoinColumn
    @ManyToOne
    private Utilisateur user;
}
