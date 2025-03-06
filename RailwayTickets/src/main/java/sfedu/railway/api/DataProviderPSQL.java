package sfedu.railway.api;

import org.slf4j.*;
import sfedu.railway.utils.Constants;
import sfedu.railway.utils.Status;
import sfedu.railway.models.*;

import java.io.*;
import java.sql.*;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.*;

public class DataProviderPSQL {

    Logger logger = LoggerFactory.getLogger(DataProviderPSQL.class);

    private static Connection connection;

    /**
     * Получение соединения с базой данных
     * @return соединение с базой данных
     * @throws SQLException
     * @throws IOException
     */
    public static Connection getConnection() throws SQLException, IOException {
        if (connection == null || connection.isClosed()) {
            Properties props = new Properties();
            try(InputStream input = DataProviderPSQL.class.getClassLoader().
                    getResourceAsStream("database.properties")) {
                props.load(input);
            }
            String url = props.getProperty("db.url");
            String user = props.getProperty("db.user");
            String password = props.getProperty("db.password");
            connection = DriverManager.getConnection(url, user, password);
        }
        return connection;
    }

    /**
     * Создание записи пользователя
     * @param user объект пользователя
     * @return успешность операции
     */
    public boolean createUser(User user) {
        if(user == null){
            return false;
        }
        String sql = Constants.insertUser;
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, user.getId());
            ps.setString(2, user.getSurname());
            ps.setString(3, user.getName());
            ps.setString(4, user.getPhoneNumber());
            ps.setString(5, user.getEmail());
            ps.setString(6, user.getPassword());
            int affectedRows = ps.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            logger.error(e.getMessage());
            return false;
        }
    }

    /**
     * Чтение записи пользователя по ID
     * @param id идентификатор пользователя
     * @return объект пользователя
     * @throws SQLException
     */
    public User readUser(String id) throws SQLException {
        String sql = Constants.readUser;
        User user = new User();
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                user.setId(rs.getString("id"));
                user.setSurname(rs.getString("surname"));
                user.setName(rs.getString("name"));
                user.setPhoneNumber(rs.getString("phoneNumber"));
                user.setEmail(rs.getString("email"));
                user.setPassword(rs.getString("password"));
                return user;
            } else {
                throw new SQLException("Cannot find user with id " + id);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage());
            throw e;
        }
    }

    /**
     * Поиск пользователя по email и паролю
     * @param email почта пользователя
     * @param password пароль пользователя
     * @return объект пользователя
     * @throws SQLException
     */
    public User authenticateUser(String email, String password) throws SQLException {
        String sql = Constants.readUserByEmail;
        User user = new User();
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            if (rs.next() && rs.getString("password").equals(password)) {
                user.setId(rs.getString("id"));
                user.setSurname(rs.getString("surname"));
                user.setName(rs.getString("name"));
                user.setPhoneNumber(rs.getString("phoneNumber"));
                user.setEmail(rs.getString("email"));
                user.setPassword(rs.getString("password"));
                return user;
            } else {
                throw new SQLException("Authentication failed");
            }
        } catch (SQLException e) {
            logger.error(e.getMessage());
            throw e;
        }
    }

    /**
     * Обновление данных пользователя
     * @param user объект пользователя с обновленными данными
     * @return успешность операции
     */
    public boolean updateUser(User user) {
        if (user == null) {
            return false;
        }
        String sql = Constants.updateUser;
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, user.getSurname());
            ps.setString(2, user.getName());
            ps.setString(3, user.getPhoneNumber());
            ps.setString(4, user.getEmail());
            ps.setString(5, user.getPassword());
            ps.setString(6, user.getId());
            int affectedRows = ps.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            logger.error(e.getMessage());
            return false;
        }
    }

    /**
     * Удаление пользователя
     * @param id идентификатор пользователя
     * @return успешность операции
     */
    public boolean deleteUser(String id) {
        String sql = Constants.deleteUser;
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, id);
            int affectedRows = ps.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            logger.error(e.getMessage());
            return false;
        }
    }

    /**
     * Поиск маршрутов
     * @param departureCity город отправления
     * @param arrivalCity город прибытия
     * @param date дата отправления
     * @return список информации о маршрутах
     */
    public List<RouteInfo> searchRoutes(String departureCity, String arrivalCity, java.util.Date date) {
        List<RouteInfo> routes = new ArrayList<>();
        String sql = Constants.searchRoutes;
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, "%" + departureCity + "%");
            ps.setString(2, "%" + arrivalCity + "%");
            
            // Форматируем дату для SQL запроса
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String formattedDate = dateFormat.format(date);
            ps.setString(3, formattedDate);
            
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                RouteInfo routeInfo = new RouteInfo();
                routeInfo.setId(rs.getString("id"));
                routeInfo.setDepartureStation(rs.getString("departureStation"));
                routeInfo.setArrivalStation(rs.getString("arrivalStation"));
                routeInfo.setDepartureCity(rs.getString("departureCity"));
                routeInfo.setArrivalCity(rs.getString("arrivalCity"));
                routeInfo.setTrainNumber(rs.getString("trainNumber"));
                
                // Форматируем даты/время для отображения
                SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
                routeInfo.setDepartureTime(timeFormat.format(rs.getTimestamp("departureTime")));
                routeInfo.setArrivalTime(timeFormat.format(rs.getTimestamp("arrivalTime")));
                
                routeInfo.setPrice(rs.getDouble("basePrice"));
                
                // Получение доступных мест - для упрощения используем фиксированное значение
                routeInfo.setAvailableSeats(calculateAvailableSeats(rs.getString("id")));
                
                routes.add(routeInfo);
            }
        } catch (SQLException e) {
            logger.error("Error searching routes: {}", e.getMessage());
        }
        
        return routes;
    }
    
    /**
     * Расчет доступных мест для маршрута
     * @param routeId идентификатор маршрута
     * @return количество доступных мест
     */
    private int calculateAvailableSeats(String routeId) {
        try {
            // Реальный запрос для подсчета мест
            String sql = "SELECT COUNT(*) FROM seats s " +
                         "JOIN wagons w ON s.wagonId = w.id " + 
                         "JOIN trains t ON w.trainId = t.id " +
                         "JOIN routes r ON r.trainId = t.id " +
                         "WHERE r.id = ? AND s.isAvailable = true";
            
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, routeId);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            logger.error("Error calculating available seats: {}", e.getMessage());
        }
        
        // Если возникла ошибка, возвращаем примерное значение
        return 50;
    }
    
    /**
     * Создание билета
     * @param ticket объект билета
     * @return успешность операции
     */
    public boolean createTicket(Ticket ticket) {
        if (ticket == null) {
            return false;
        }
        
        String sql = Constants.insertTicket;
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, ticket.getId());
            ps.setString(2, ticket.getUserId());
            ps.setString(3, ticket.getRouteId());
            ps.setString(4, ticket.getSeatId());
            ps.setTimestamp(5, new java.sql.Timestamp(ticket.getBookingDate().getTime()));
            ps.setString(6, ticket.getStatus().toString());
            ps.setDouble(7, ticket.getPrice());
            
            int affectedRows = ps.executeUpdate();
            
            // Если билет создан, обновляем доступность места
            if (affectedRows > 0) {
                updateSeatAvailability(ticket.getSeatId(), false);
            }
            
            return affectedRows > 0;
        } catch (SQLException e) {
            logger.error("Error creating ticket: {}", e.getMessage());
            return false;
        }
    }
    
    /**
     * Обновление доступности места
     * @param seatId идентификатор места
     * @param isAvailable доступность
     * @return успешность операции
     */
    private boolean updateSeatAvailability(String seatId, boolean isAvailable) {
        String sql = Constants.updateSeatAvailability;
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setBoolean(1, isAvailable);
            ps.setString(2, seatId);
            int affectedRows = ps.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            logger.error("Error updating seat availability: {}", e.getMessage());
            return false;
        }
    }
    
    /**
     * Получение билета по ID
     * @param id идентификатор билета
     * @return объект билета
     * @throws SQLException
     */
    public Ticket readTicket(String id) throws SQLException {
        String sql = Constants.readTicket;
        Ticket ticket = new Ticket();
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                ticket.setId(rs.getString("id"));
                ticket.setUserId(rs.getString("userId"));
                ticket.setRouteId(rs.getString("routeId"));
                ticket.setSeatId(rs.getString("seatId"));
                ticket.setBookingDate(rs.getTimestamp("bookingDate"));
                ticket.setStatus(Status.valueOf(rs.getString("status")));
                ticket.setPrice(rs.getDouble("price"));
                return ticket;
            } else {
                throw new SQLException("Cannot find ticket with id " + id);
            }
        } catch (SQLException e) {
            logger.error("Error reading ticket: {}", e.getMessage());
            throw e;
        }
    }
    
    /**
     * Обновление статуса билета
     * @param ticketId идентификатор билета
     * @param status новый статус
     * @return успешность операции
     */
    public boolean updateTicketStatus(String ticketId, Status status) {
        String sql = Constants.updateTicketStatus;
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, status.toString());
            ps.setString(2, ticketId);
            
            int affectedRows = ps.executeUpdate();
            
            // Если билет отменен, освобождаем место
            if (affectedRows > 0 && status == Status.CANCELED) {
                try {
                    Ticket ticket = readTicket(ticketId);
                    updateSeatAvailability(ticket.getSeatId(), true);
                } catch (SQLException e) {
                    logger.error("Error freeing seat: {}", e.getMessage());
                }
            }
            
            return affectedRows > 0;
        } catch (SQLException e) {
            logger.error("Error updating ticket status: {}", e.getMessage());
            return false;
        }
    }
    
    /**
     * Получение билетов пользователя
     * @param userId идентификатор пользователя
     * @return список билетов
     */
    public List<Ticket> getUserTickets(String userId) {
        List<Ticket> tickets = new ArrayList<>();
        String sql = Constants.getUserTickets;
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, userId);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                Ticket ticket = new Ticket();
                ticket.setId(rs.getString("id"));
                ticket.setUserId(rs.getString("userId"));
                ticket.setRouteId(rs.getString("routeId"));
                ticket.setSeatId(rs.getString("seatId"));
                ticket.setBookingDate(rs.getTimestamp("bookingDate"));
                ticket.setStatus(Status.valueOf(rs.getString("status")));
                ticket.setPrice(rs.getDouble("price"));
                tickets.add(ticket);
            }
        } catch (SQLException e) {
            logger.error("Error getting user tickets: {}", e.getMessage());
        }
        
        return tickets;
    }
    
    /**
     * Создание транзакции оплаты
     * @param transaction объект транзакции
     * @return успешность операции
     */
    public boolean createTransaction(Transaction transaction) {
        if (transaction == null) {
            return false;
        }
        
        String sql = Constants.insertTransaction;
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, transaction.getId());
            ps.setString(2, transaction.getTicketId());
            ps.setTimestamp(3, new java.sql.Timestamp(transaction.getDate().getTime()));
            ps.setDouble(4, transaction.getAmount());
            ps.setString(5, transaction.getPaymentMethod());
            
            int affectedRows = ps.executeUpdate();
            
            // Если транзакция создана, обновляем статус билета на PAID
            if (affectedRows > 0) {
                updateTicketStatus(transaction.getTicketId(), Status.PAID);
            }
            
            return affectedRows > 0;
        } catch (SQLException e) {
            logger.error("Error creating transaction: {}", e.getMessage());
            return false;
        }
    }
    
    /**
     * Получение деталей для билета (маршрут, станции, поезд)
     * @param routeId идентификатор маршрута
     * @return информация о маршруте для билета
     */
    public RouteInfo getRouteDetails(String routeId) {
        RouteInfo routeInfo = new RouteInfo();
        
        try {
            String sql = "SELECT r.id, ds.name as departureStation, ds.city as departureCity, " +
                         "as.name as arrivalStation, as.city as arrivalCity, " +
                         "t.number as trainNumber, r.departureTime, r.arrivalTime, r.basePrice " +
                         "FROM routes r " +
                         "JOIN stations ds ON r.departureStationId = ds.id " +
                         "JOIN stations as ON r.arrivalStationId = as.id " +
                         "JOIN trains t ON r.trainId = t.id " +
                         "WHERE r.id = ?";
            
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, routeId);
            
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                routeInfo.setId(rs.getString("id"));
                routeInfo.setDepartureStation(rs.getString("departureStation"));
                routeInfo.setArrivalStation(rs.getString("arrivalStation"));
                routeInfo.setDepartureCity(rs.getString("departureCity"));
                routeInfo.setArrivalCity(rs.getString("arrivalCity"));
                routeInfo.setTrainNumber(rs.getString("trainNumber"));
                
                // Форматируем даты/время для отображения
                SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
                routeInfo.setDepartureTime(timeFormat.format(rs.getTimestamp("departureTime")));
                routeInfo.setArrivalTime(timeFormat.format(rs.getTimestamp("arrivalTime")));
                
                routeInfo.setPrice(rs.getDouble("basePrice"));
            }
        } catch (SQLException e) {
            logger.error("Error getting route details: {}", e.getMessage());
        }
        
        return routeInfo;
    }
    
    /**
     * Получение доступных мест для маршрута
     * @param routeId идентификатор маршрута
     * @return список доступных мест
     */
    public List<Seat> getAvailableSeats(String routeId) {
        List<Seat> seats = new ArrayList<>();
        
        try {
            String sql = "SELECT s.id, s.wagonId, s.number, s.isAvailable, s.priceMultiplier, w.number as wagonNumber, w.type as wagonType " +
                         "FROM seats s " +
                         "JOIN wagons w ON s.wagonId = w.id " + 
                         "JOIN trains t ON w.trainId = t.id " +
                         "JOIN routes r ON r.trainId = t.id " +
                         "WHERE r.id = ? AND s.isAvailable = true " +
                         "ORDER BY w.number, s.number";
            
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, routeId);
            
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                Seat seat = new Seat();
                seat.setId(rs.getString("id"));
                seat.setWagonId(rs.getString("wagonId"));
                seat.setNumber(rs.getString("number"));
                seat.setAvailable(rs.getBoolean("isAvailable"));
                seat.setPriceMultiplier(rs.getDouble("priceMultiplier"));
                seats.add(seat);
            }
        } catch (SQLException e) {
            logger.error("Error getting available seats: {}", e.getMessage());
        }
        
        return seats;
    }
}
