package fr.tpreservation.request.HostelReservation;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class PostHostelReservationRequest {
    @NotBlank(message = "Hostel ID is required")
    private String hostelId;

    @NotBlank(message = "User ID is required")
    private String userId;

    @NotNull(message = "Check-in date is required")
    private LocalDate checkInDate;

    @NotNull(message = "Check-out date is required")
    private LocalDate checkOutDate;

    @Positive(message = "Total price must be greater than 0")
    private int totalPrice;
}