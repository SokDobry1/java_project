#java_project

```mermaid
classDiagram
    class User {
        -Long id
        -String firstName
        -String lastName
        -String email
        -String phoneNumber
        -String password
        +register()
        +login()
        +updateProfile()
    }

    class Ticket {
        -Long id
        -User passenger
        -Seat seat
        -TicketStatus status
        -Double price
        -Date purchaseDate
        +book()
        +cancel()
    }

    class Train {
        -Long id
        -String trainNumber
        -Route route
        -DateTime departureTime
        -DateTime arrivalTime
        +getAvailableSeats()
        +updateSchedule()
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
        -WagonType type
        -Integer capacity
        -Integer wagonNumber
        +getAvailableSeats()
        +updateStatus()
    }

    class Seat {
        -Long id
        -Wagon wagon
        -String seatNumber
        -SeatStatus status
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
        -List~Station~ stations
        -Double distance
        +calculatePrice()
        +getStops()
    }

    User "1" -- "0..*" Ticket
    Ticket "1" -- "1" Seat
    Train "1" -- "1..*" Wagon
    Train "1" -- "1" Route
    Route "1" -- "2..*" Station
    Wagon "1" -- "1..*" Seat
    Ticket "1" -- "1" Payment
