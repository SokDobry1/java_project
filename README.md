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
        -String passengerName
        -TicketStatus status
        -Double price
        -Date purchaseDate
        +book()
        +cancel()
        +getDetails()
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
        -Double distance
        +calculatePrice()
        +getStops()
    }

    class RouteStation {
        -Route route
        -Station station
        -Integer sequenceNumber
    }

    User "1" -- "0..*" Ticket
    Ticket "1" -- "1" Seat
    Train "1" -- "0..*" Wagon
    Train "1" -- "1" Route
    Route "1" -- "2..*" RouteStation
    RouteStation "1" -- "1" Station
    Wagon "1" -- "1..*" Seat
    Ticket "1" -- "1" Payment

