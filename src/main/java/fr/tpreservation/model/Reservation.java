package fr.tpreservation.model;

import java.time.LocalDateTime;

import org.hibernate.annotations.UuidGenerator;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "reservation")
@Getter
@Setter
public class Reservation {
    @Id
    @UuidGenerator
    private String id;

    @Column(nullable = false)
    private LocalDateTime reservationDate;

    @JoinColumn(nullable = false)
    @ManyToOne
    private User user;
}
