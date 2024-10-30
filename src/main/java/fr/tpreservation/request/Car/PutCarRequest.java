package fr.tpreservation.request.Car;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PutCarRequest {
    private String model;
    private String brand;
    private int capacity;
    private int pricePerDay;
    private String location;
    private String fuelType;
    private String description;
    private Boolean available;
}
