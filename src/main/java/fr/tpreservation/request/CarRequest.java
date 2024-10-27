package fr.tpreservation.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CarRequest {
    @NotBlank(message = "Model is required")
    private String model;

    @NotBlank(message = "Brand is required")
    private String brand;

    @Positive(message = "Capacity must be greater than 0")
    private int capacity;

    @Positive(message = "Price per day must be greater than 0")
    private int pricePerDay;

    @NotBlank(message = "Location is required")
    private String location;

    @NotBlank(message = "Fuel type is required")
    private String fuelType;
    
    private String description;

    private Boolean available;
}
