package sfedu.railway.models;

import lombok.*;
import java.util.UUID;

@Getter
@Setter
public class Train {
    private String id;
    private String number;
    private String type;
    private int totalWagons;
    
    public Train(String number, String type, int totalWagons) {
        this.id = UUID.randomUUID().toString();
        this.number = number;
        this.type = type;
        this.totalWagons = totalWagons;
    }
    
    public Train() {
        this.id = UUID.randomUUID().toString();
    }
}
