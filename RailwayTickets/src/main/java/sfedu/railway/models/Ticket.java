package sfedu.railway.models;

import lombok.*;
import java.util.*;
import sfedu.railway.utils.Status;

@Getter
@Setter
public class Ticket {
    private String id;
    private String userId;
    private String routeId;
    private String seatId;
    private Date bookingDate;
    private Status status; // BOOKED, PAID, CANCELED
    private double price;
    
    public Ticket(String userId, String routeId, String seatId, double price) {
        this.id = UUID.randomUUID().toString();
        this.userId = userId;
        this.routeId = routeId;
        this.seatId = seatId;
        this.bookingDate = new Date();
        this.status = Status.BOOKED;
        this.price = price;
    }
    
    public Ticket() {
        this.id = UUID.randomUUID().toString();
        this.bookingDate = new Date();
        this.status = Status.BOOKED;
    }
}
