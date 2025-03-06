package sfedu.railway.models;

import lombok.*;
import java.util.UUID;

@Getter
@Setter
public class Station {
    private String id;
    private String name;
    private String city;
    private String address;
    
    public Station(String name, String city, String address) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.city = city;
        this.address = address;
    }
    
    public Station() {
        this.id = UUID.randomUUID().toString();
    }
}
