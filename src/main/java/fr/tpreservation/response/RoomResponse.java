package fr.tpreservation.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class RoomResponse {
    private String id;
    private String name;
    private int price;
    private Boolean available;
    private String hotelId;
}
