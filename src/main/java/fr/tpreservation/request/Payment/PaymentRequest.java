package fr.tpreservation.request.Payment;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.util.Assert;

import java.time.LocalDateTime;

@Getter
@Setter
public class PaymentRequest {
    @NotNull(message = "ReservationId must not be null")
    private String reservationId;
    @NotNull(message = "Amount must not be null")
    private Double amount;
    @NotNull(message = "PaymentMethod must not be null")
    private String paymentMethod;
    @NotNull(message = "PaymentDate must not be null")
    private LocalDateTime paymentDate;
}