package fr.tpreservation.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CarRequest {
    private String model;
    private String brand;
    private int capacity;
    private int pricePerDay;
    private String location;
    private String fuelType;
    private String description;
    private Boolean available;
}
