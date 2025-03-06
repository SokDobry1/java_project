package sfedu.railway.models;

import lombok.*;
import java.util.UUID;

@Getter
@Setter
public class Seat {
    private String id;
    private String wagonId;
    private String number;
    private boolean isAvailable;
    private double priceMultiplier; // Коэффициент к базовой цене маршрута
    
    public Seat(String wagonId, String number, double priceMultiplier) {
        this.id = UUID.randomUUID().toString();
        this.wagonId = wagonId;
        this.number = number;
        this.isAvailable = true;
        this.priceMultiplier = priceMultiplier;
    }
    
    public Seat() {
        this.id = UUID.randomUUID().toString();
        this.isAvailable = true;
    }
}
