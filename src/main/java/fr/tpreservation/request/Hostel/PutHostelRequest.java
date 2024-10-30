package fr.tpreservation.request.Hostel;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PutHostelRequest {
    private String name;
    private String address;
    private String city;
    private String country;
    private int stars;
    private int rooms;
    private int availableRooms;
    private int price;
}
