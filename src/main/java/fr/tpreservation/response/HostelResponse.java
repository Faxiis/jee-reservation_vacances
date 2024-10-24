package fr.tpreservation.response;

import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class HostelResponse {
  private String id;
  private String name;
  private String address;
  private String city;
  private String country;
  private int stars;
  private int rooms;
  private int availableRooms;
  private int price;
  private List<RoomResponse> roomList;
}