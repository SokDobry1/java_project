package sfedu.railway.models;

import lombok.*;
import java.util.UUID;

@Getter
@Setter
public class Wagon {
    private String id;
    private String trainId;
    private String number;
    private String type; // ECONOMY, BUSINESS, FIRST_CLASS
    private int totalSeats;
    
    public Wagon(String trainId, String number, String type, int totalSeats) {
        this.id = UUID.randomUUID().toString();
        this.trainId = trainId;
        this.number = number;
        this.type = type;
        this.totalSeats = totalSeats;
    }
    
    public Wagon() {
        this.id = UUID.randomUUID().toString();
    }
}
