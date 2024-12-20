package fr.tpreservation.request.FlightReservation;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public class PostFlightReservationRequest {
    @NotBlank(message = "Flight ID is required")
    private String flightId;

    @NotBlank(message = "User ID is required")
    private String userId;

    @NotNull(message = "Departure date is required")
    private LocalDateTime departureDate;

    @NotNull(message = "Return date is required")
    private LocalDateTime returnDate;

    @Positive(message = "Total price must be greater than 0")
    private int totalPrice;
}