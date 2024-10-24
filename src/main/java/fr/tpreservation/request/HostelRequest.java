package fr.tpreservation.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HostelRequest {
    private String name;
    private String address;
    private String city;
    private String country;
    private int stars;
    private int rooms;
    private int availableRooms;
    private int price;
}