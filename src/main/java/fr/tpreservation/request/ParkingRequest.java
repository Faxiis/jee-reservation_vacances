package fr.tpreservation.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ParkingRequest {
    @NotBlank
    private String parkingName;

    @NotBlank
    private String parkingAddress;

    @PositiveOrZero
    private int floor;

    @Positive
    private int placeNumber;

    @NotNull
    private boolean isAvailable;
}
