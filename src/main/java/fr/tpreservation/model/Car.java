package fr.tpreservation.model;

import org.hibernate.annotations.UuidGenerator;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "car")
@Getter
@Setter
public class Car extends Reservation {
    @Id
    @UuidGenerator
    private String id;

    @Column(nullable = false)
    private String model;

    @Column(nullable = false)
    private String brand;

    @Column(nullable = false)
    private int capacity;

    private int pricePerDay;

    private String location;

    private String fuelType;

    @Column(length = 1000)
    private String description;

    private Boolean available;

}
