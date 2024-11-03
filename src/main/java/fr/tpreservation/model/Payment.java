package fr.tpreservation.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

@Entity
@Table(name = "payments")
@Getter
@Setter
public class Payment {
    @Id
    @UuidGenerator
    private String id;

    @Column(nullable = false)
    private String reservationId;
    
    @Column(nullable = false)
    private Double amount;

    @Column(nullable = false)
    private String paymentMethod;
    
    private String status;
}