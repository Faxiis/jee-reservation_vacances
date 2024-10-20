package fr.tpreservation.request;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class SubscribeRequest {
    private String username;
    private String password;
}