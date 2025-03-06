package sfedu.railway.models;

import lombok.*;
import java.util.*;

@Getter
@Setter
public class Route {
    private String id;
    private String departureStationId;
    private String arrivalStationId;
    private String trainId;
    private Date departureTime;
    private Date arrivalTime;
    private double basePrice;
    
    public Route(String departureStationId, String arrivalStationId, String trainId, 
                Date departureTime, Date arrivalTime, double basePrice) {
        this.id = UUID.randomUUID().toString();
        this.departureStationId = departureStationId;
        this.arrivalStationId = arrivalStationId;
        this.trainId = trainId;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.basePrice = basePrice;
    }
    
    public Route() {
        this.id = UUID.randomUUID().toString();
    }
}
