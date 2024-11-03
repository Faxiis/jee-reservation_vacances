package fr.tpreservation.model;

import org.hibernate.annotations.UuidGenerator;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class ParkingPlace {
    @Id
    @UuidGenerator
    private String id;

    @Column(nullable = false)
    private String parkingName;

    @Column(nullable = false)
    private String parkingAddress;

    private int floor;

    private int placeNumber;

    private boolean isAvailable;
}
