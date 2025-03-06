package sfedu.railway.models;

import lombok.*;
import java.util.*;

@Getter
@Setter
public class Transaction {
    private String id;
    private String ticketId;
    private Date date;
    private double amount;
    private String paymentMethod;
    
    public Transaction(String ticketId, double amount, String paymentMethod) {
        this.id = UUID.randomUUID().toString();
        this.ticketId = ticketId;
        this.date = new Date();
        this.amount = amount;
        this.paymentMethod = paymentMethod;
    }
    
    public Transaction() {
        this.id = UUID.randomUUID().toString();
        this.date = new Date();
    }
}
