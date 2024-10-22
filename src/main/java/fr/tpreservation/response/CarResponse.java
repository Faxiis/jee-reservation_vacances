package fr.tpreservation.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CarResponse {
    private String id;
    private String model;
    private String brand;
    private int capacity;
    private int pricePerDay;
    private String location;
    private String fuelType;
    private String description;
    private Boolean available;
}
