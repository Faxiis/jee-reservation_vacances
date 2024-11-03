package fr.tpreservation.request.Hostel;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostHostelRequest {
    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Address is required")
    private String address;

    @NotBlank(message = "City is required")
    private String city;

    @NotBlank(message = "Country is required")
    private String country;

    @Min(value = 1, message = "Stars must be at least 1")
    private int stars;

    @Positive(message = "Rooms must be greater than 0")
    private int rooms;

    @Positive(message = "Available rooms must be greater than or equal to 0")
    private int availableRooms;

    @Positive(message = "Price must be greater than 0")
    private int price;
}
