package sfedu.railway.utils;

public class Constants {
    // MongoDB URL
    public static final String DataBaseUrlMongo = "mongodb://localhost:27017";
    public static final String ValidFileName = "src/test/resources/test.txt";

    /**
     * SQL запросы
     */
    // Таблица User
    public static String insertUser = "INSERT INTO users (id, surname, name, phoneNumber, email, password) VALUES (?,?,?,?,?,?)";
    public static String readUser = "SELECT * FROM users WHERE id = ?";
    public static String readUserByEmail = "SELECT * FROM users WHERE email = ?";
    public static String updateUser = "UPDATE users SET surname = ?, name = ?, phoneNumber = ?, email = ?, password = ? WHERE id = ?";
    public static String deleteUser = "DELETE FROM users WHERE id = ?";
    
    // Таблица Station
    public static String insertStation = "INSERT INTO stations (id, name, city, address) VALUES (?,?,?,?)";
    public static String readStation = "SELECT * FROM stations WHERE id = ?";
    public static String updateStation = "UPDATE stations SET name = ?, city = ?, address = ? WHERE id = ?";
    public static String deleteStation = "DELETE FROM stations WHERE id = ?";
    
    // Таблица Train
    public static String insertTrain = "INSERT INTO trains (id, number, type, totalWagons) VALUES (?,?,?,?)";
    public static String readTrain = "SELECT * FROM trains WHERE id = ?";
    public static String updateTrain = "UPDATE trains SET number = ?, type = ?, totalWagons = ? WHERE id = ?";
    public static String deleteTrain = "DELETE FROM trains WHERE id = ?";
    
    // Таблица Route
    public static String insertRoute = "INSERT INTO routes (id, departureStationId, arrivalStationId, trainId, departureTime, arrivalTime, basePrice) VALUES (?,?,?,?,?,?,?)";
    public static String readRoute = "SELECT * FROM routes WHERE id = ?";
    public static String updateRoute = "UPDATE routes SET departureStationId = ?, arrivalStationId = ?, trainId = ?, departureTime = ?, arrivalTime = ?, basePrice = ? WHERE id = ?";
    public static String deleteRoute = "DELETE FROM routes WHERE id = ?";
    public static String searchRoutes = "SELECT r.id, ds.name as departureStation, ds.city as departureCity, as.name as arrivalStation, " +
                                       "as.city as arrivalCity, t.number as trainNumber, r.departureTime, r.arrivalTime, r.basePrice " +
                                       "FROM routes r " +
                                       "JOIN stations ds ON r.departureStationId = ds.id " +
                                       "JOIN stations as ON r.arrivalStationId = as.id " +
                                       "JOIN trains t ON r.trainId = t.id " +
                                       "WHERE ds.city LIKE ? AND as.city LIKE ? AND DATE(r.departureTime) = ?";
    
    // Таблица Wagon
    public static String insertWagon = "INSERT INTO wagons (id, trainId, number, type, totalSeats) VALUES (?,?,?,?,?)";
    public static String readWagon = "SELECT * FROM wagons WHERE id = ?";
    public static String updateWagon = "UPDATE wagons SET trainId = ?, number = ?, type = ?, totalSeats = ? WHERE id = ?";
    public static String deleteWagon = "DELETE FROM wagons WHERE id = ?";
    public static String getWagonsByTrain = "SELECT * FROM wagons WHERE trainId = ?";
    
    // Таблица Seat
    public static String insertSeat = "INSERT INTO seats (id, wagonId, number, isAvailable, priceMultiplier) VALUES (?,?,?,?,?)";
    public static String readSeat = "SELECT * FROM seats WHERE id = ?";
    public static String updateSeat = "UPDATE seats SET wagonId = ?, number = ?, isAvailable = ?, priceMultiplier = ? WHERE id = ?";
    public static String deleteSeat = "DELETE FROM seats WHERE id = ?";
    public static String getSeatsByWagon = "SELECT * FROM seats WHERE wagonId = ?";
    public static String updateSeatAvailability = "UPDATE seats SET isAvailable = ? WHERE id = ?";
    
    // Таблица Ticket
    public static String insertTicket = "INSERT INTO tickets (id, userId, routeId, seatId, bookingDate, status, price) VALUES (?,?,?,?,?,?,?)";
    public static String readTicket = "SELECT * FROM tickets WHERE id = ?";
    public static String updateTicket = "UPDATE tickets SET userId = ?, routeId = ?, seatId = ?, bookingDate = ?, status = ?, price = ? WHERE id = ?";
    public static String deleteTicket = "DELETE FROM tickets WHERE id = ?";
    public static String getUserTickets = "SELECT * FROM tickets WHERE userId = ?";
    public static String updateTicketStatus = "UPDATE tickets SET status = ? WHERE id = ?";
    
    // Таблица Transaction
    public static String insertTransaction = "INSERT INTO transactions (id, ticketId, date, amount, paymentMethod) VALUES (?,?,?,?,?)";
    public static String readTransaction = "SELECT * FROM transactions WHERE id = ?";
    public static String updateTransaction = "UPDATE transactions SET ticketId = ?, date = ?, amount = ?, paymentMethod = ? WHERE id = ?";
    public static String deleteTransaction = "DELETE FROM transactions WHERE id = ?";
    
    /**
     * Пути к CSV файлам
     */
    public static String csvUserFilePath = "src/main/resources/csvFiles/users.csv";
    public static String csvStationFilePath = "src/main/resources/csvFiles/stations.csv";
    public static String csvTrainFilePath = "src/main/resources/csvFiles/trains.csv";
    public static String csvRouteFilePath = "src/main/resources/csvFiles/routes.csv";
    public static String csvWagonFilePath = "src/main/resources/csvFiles/wagons.csv";
    public static String csvSeatFilePath = "src/main/resources/csvFiles/seats.csv";
    public static String csvTicketFilePath = "src/main/resources/csvFiles/tickets.csv";
    public static String csvTransactionFilePath = "src/main/resources/csvFiles/transactions.csv";
    
    /**
     * MongoDB Collections
     */
    public static String userCollection = "User_Collection";
    public static String stationCollection = "Station_Collection";
    public static String trainCollection = "Train_Collection";
    public static String routeCollection = "Route_Collection";
    public static String wagonCollection = "Wagon_Collection";
    public static String seatCollection = "Seat_Collection";
    public static String ticketCollection = "Ticket_Collection";
    public static String transactionCollection = "Transaction_Collection";
    
    /**
     * Пути к XML файлам
     */
    public static String xmlUserFilePath = "src/main/resources/xmlFiles/users.xml";
    public static String xmlStationFilePath = "src/main/resources/xmlFiles/stations.xml";
    public static String xmlTrainFilePath = "src/main/resources/xmlFiles/trains.xml";
    public static String xmlRouteFilePath = "src/main/resources/xmlFiles/routes.xml";
    public static String xmlWagonFilePath = "src/main/resources/xmlFiles/wagons.xml";
    public static String xmlSeatFilePath = "src/main/resources/xmlFiles/seats.xml";
    public static String xmlTicketFilePath = "src/main/resources/xmlFiles/tickets.xml";
    public static String xmlTransactionFilePath = "src/main/resources/xmlFiles/transactions.xml";
}
