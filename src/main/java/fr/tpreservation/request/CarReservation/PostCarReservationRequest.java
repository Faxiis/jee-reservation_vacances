package fr.tpreservation.request.CarReservation;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class PostCarReservationRequest {
    @NotBlank(message = "Car ID is required")
    private String carId;

    @NotBlank(message = "User ID is required")
    private String userId;

    @NotNull(message = "Start date is required")
    private LocalDate startDate;

    @NotNull(message = "End date is required")
    private LocalDate endDate;

    @Positive(message = "Total price must be greater than 0")
    private int totalPrice;
}