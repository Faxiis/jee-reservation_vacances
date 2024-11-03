package fr.tpreservation.request.CarReservation;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public class PutCarReservationRequest {
    @NotNull(message = "Start date is required")
    private LocalDateTime startDate;

    @NotNull(message = "End date is required")
    private LocalDateTime endDate;

    @Positive(message = "Total price must be greater than 0")
    private int totalPrice;
}