#java_project

```mermaid
classDiagram
    class User {
        -Long id
        -String firstName
        -String lastName
        -String email
        -String password
        +register()
        +login()
        +updateProfile()
    }

    class Ticket {
        -Long id
        -User passenger
        -Seat seat
        -RouteStation departureStation
        -RouteStation arrivalStation
        -TicketStatus status
        -Double price
        -Date purchaseDate
        +calculatePrice()
        +book()
        +cancel()
    }

    class Train {
        -Long id
        -String trainNumber
        -DateTime departureTime
        -DateTime arrivalTime
        +getAvailableSeats()
        +getSchedule()
    }

    class Station {
        -Long id
        -String name
        -String city
        -String address
        +getSchedule()
    }

    class RouteStation {
        -Route route
        -Station station
        -Integer sequenceNumber
    }

    class StationDistance {
        -Station stationFrom
        -Station stationTo
        -Double distance
    }

    class Wagon {
        -Long id
        -Train train
        -WagonType type
        -Integer capacity
        -Integer wagonNumber
        +getAvailableSeats()
    }

    class Seat {
        -Long id
        -Wagon wagon
        -String seatNumber
        -SeatStatus status
        -Boolean isAvailable
    }

    class Payment {
        -Long id
        -Ticket ticket
        -Double amount
        -PaymentMethod method
        -DateTime timestamp
        +process()
        +refund()
        +getReceipt()
    }

    User "1" -- "0..*" Ticket
    Ticket "1" -- "1" Seat
    Ticket "1" -- "1" RouteStation : departureStation
    Ticket "1" -- "1" RouteStation : arrivalStation
    Train "1" -- "0..*" Wagon
    Wagon "1" -- "1..*" Seat
    Ticket "1" -- "1" Payment
    RouteStation "1" -- "1" Station : station
    StationDistance "1" -- "1" Station : stationFrom
    StationDistance "1" -- "1" Station : stationTo
