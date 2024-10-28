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
        -Train train
        -User passenger
        -String seatNumber
        -TicketStatus status
        -Double price
        -Date purchaseDate
        +book()
        +cancel()
        +generatePDF()
    }

    class Train {
        -Long id
        -String trainNumber
        -Station departureStation
        -Station arrivalStation
        -DateTime departureTime
        -DateTime arrivalTime
        -List~Wagon~ wagons
        +getAvailableSeats()
        +updateSchedule()
    }

    class Station {
        -Long id
        -String name
        -String city
        -String address
        -GeoCoordinates location
        +getSchedule()
    }

    class Wagon {
        -Long id
        -WagonType type
        -Integer capacity
        -List~Seat~ seats
        -Integer wagonNumber
        +getAvailableSeats()
        +updateStatus()
    }

    class Payment {
        -Long id
        -Ticket ticket
        -Double amount
        -PaymentStatus status
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
        -Integer duration
        +calculatePrice()
        +getStops()
    }

    User "1" -- "0..*" Ticket
    Ticket "0..*" -- "1" Train
    Train "1" -- "1..*" Wagon
    Train "1" -- "1" Route
    Route "1" -- "2..*" Station
    Ticket "1" -- "1" Payment
