package fr.tpreservation.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class FlightRequest {
    @NotBlank
    private String flightNumber;

    @NotBlank
    private String departure;

    @NotBlank
    private String arrival;

    @NotNull
    private LocalDateTime departureTime;

    @NotNull
    private LocalDateTime arrivalTime;

    @Positive
    private int price;

    @PositiveOrZero
    private int availableSeats;

    @Positive
    private int totalSeats;
}
