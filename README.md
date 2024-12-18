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
        -Station departureStation
        -Station arrivalStation
        -TicketStatus status
        -Double price
        -Date purchaseDate
        +calculatePrice()
        +book()
        +cancel()
    }

    class TicketStation {
        -Ticket ticket
        -Station station
    }

    class Train {
        -Long id
        -String trainNumber
        -Route route
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

    class Route {
        -Long id
        -String name
        +getStops()
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

    User "1" -- "0..*" Ticket
    Ticket "1" -- "1" Seat
    Ticket "1" -- "0..*" TicketStation
    TicketStation "1" -- "1" Station
    Train "1" -- "0..*" Wagon
    Train "1" -- "1" Route
    Route "1" -- "2..*" RouteStation
    RouteStation "1" -- "1" Station
    Wagon "1" -- "1..*" Seat
    Ticket "1" -- "1" Payment
    StationDistance "1" -- "1" Station : stationFrom
    StationDistance "1" -- "1" Station : stationTo


