package fr.tpreservation.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class PaymentResponse {
    private Long id;
    private String reservationId;
    private Double amount;
    private String paymentMethod;
    private String status;
}