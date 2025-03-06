package sfedu.railway.models;

import lombok.*;

@Getter
@Setter
public class RouteInfo {
    private String id;
    private String departureStation;
    private String arrivalStation;
    private String departureCity;
    private String arrivalCity;
    private String trainNumber;
    private String departureTime;
    private String arrivalTime;
    private double price;
    private int availableSeats;
    
    // Конструктор для отображения информации о маршруте пользователю
    public RouteInfo() {
    }
}
