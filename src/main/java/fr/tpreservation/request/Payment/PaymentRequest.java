package fr.tpreservation.request.Payment;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class PaymentRequest {
    private String reservationId;
    private Double amount;
    private String paymentMethod;
    private LocalDateTime paymentDate;
}