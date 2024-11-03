package fr.tpreservation.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class PaymentResponse {
    private String id;
    private String reservationId;
    private Double amount;
    private String paymentMethod;
    private LocalDateTime paymentDate;
    private String status;
}
