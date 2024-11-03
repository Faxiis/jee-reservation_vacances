package fr.tpreservation.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ParkingResponse {
    private String id;
    private String parkingName;
    private String parkingAddress;
    private int floor;
    private int placeNumber;
    private boolean isAvailable;
}
