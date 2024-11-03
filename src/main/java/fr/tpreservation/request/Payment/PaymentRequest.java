package fr.tpreservation.request.Payment;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentRequest {
    private String reservationId;
    private Double amount;
    private String paymentMethod;
}