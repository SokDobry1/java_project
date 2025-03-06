package sfedu.railway.api;

import com.opencsv.*;
import com.opencsv.exceptions.CsvException;
import org.slf4j.*;
import sfedu.railway.models.*;
import sfedu.railway.utils.Constants;
import sfedu.railway.utils.Status;

import java.io.*;
import java.nio.file.*;
import java.text.*;
import java.util.*;

public class DataProviderCsv {

    Logger logger = LoggerFactory.getLogger(DataProviderCsv.class);

    /**
     * Запись данных в CSV файл
     * @param data данные для записи
     * @param csvFilePath путь к файлу
     * @throws IOException
     */
    public static void writeToCsv(List<String[]> data, String csvFilePath) throws IOException {
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(csvFilePath));
             CSVWriter csvWriter = new CSVWriter(writer)) {
            csvWriter.writeAll(data);
        }
    }

    /**
     * Чтение данных из CSV файла
     * @param csvFilePath путь к файлу
     * @return список строк данных
     * @throws IOException
     * @throws CsvException
     */
    public List<String[]> readFromCsv(String csvFilePath) throws IOException, CsvException {
        List<String[]> data = new ArrayList<>();
        if (Files.exists(Paths.get(csvFilePath))) {
            try (BufferedReader reader = Files.newBufferedReader(Paths.get(csvFilePath));
                 CSVReader csvReader = new CSVReader(reader)) {
                data = csvReader.readAll();
            }
        }
        return data;
    }

    /**
     * Создание записи пользователя в CSV
     * @param user объект пользователя
     * @return успешность операции
     * @throws IOException
     * @throws CsvException
     */
    public boolean createUser(User user) throws IOException, CsvException {
        if(user == null){
            return false;
        }
        try {
            List<String[]> data = readFromCsv(Constants.csvUserFilePath);
            data.add(new String[]{
                    user.getId(),
                    user.getSurname(),
                    user.getName(),
                    user.getPhoneNumber(),
                    user.getEmail(),
                    user.getPassword()
            });
            writeToCsv(data, Constants.csvUserFilePath);
            return true;
        } catch (CsvException | IOException e) {
            logger.error(e.getMessage());
            return false;
        }
    }

    /**
     * Чтение пользователя из CSV по ID
     * @param id идентификатор пользователя
     * @return объект пользователя
     * @throws IOException
     * @throws CsvException
     */
    public User readUser(String id) throws IOException, CsvException {
        User user = new User();
        try {
            List<String[]> data = readFromCsv(Constants.csvUserFilePath);
            for (String[] row : data) {
                if(row[0].equals(id)) {
                    user.setId(row[0]);
                    user.setSurname(row[1]);
                    user.setName(row[2]);
                    user.setPhoneNumber(row[3]);
                    user.setEmail(row[4]);
                    user.setPassword(row[5]);
                    return user;
                }
            }
            throw new CsvException("Cannot find user with id " + id);
        } catch (CsvException | IOException e) {
            logger.error(e.getMessage());
            throw e;
        }
    }

    /**
     * Поиск пользователя по email и паролю
     * @param email почта пользователя
     * @param password пароль пользователя
     * @return объект пользователя
     * @throws IOException
     * @throws CsvException
     */
    public User authenticateUser(String email, String password) throws IOException, CsvException {
        User user = new User();
        try {
            List<String[]> data = readFromCsv(Constants.csvUserFilePath);
            for (String[] row : data) {
                if(row[4].equals(email) && row[5].equals(password)) {
                    user.setId(row[0]);
                    user.setSurname(row[1]);
                    user.setName(row[2]);
                    user.setPhoneNumber(row[3]);
                    user.setEmail(row[4]);
                    user.setPassword(row[5]);
                    return user;
                }
            }
            throw new CsvException("Authentication failed");
        } catch (CsvException | IOException e) {
            logger.error(e.getMessage());
            throw e;
        }
    }

    /**
     * Обновление данных пользователя в CSV
     * @param user объект пользователя с обновленными данными
     * @return успешность операции
     * @throws IOException
     * @throws CsvException
     */
    public boolean updateUser(User user) throws IOException, CsvException {
        if (user == null) {
            throw new CsvException("User object must not be null");
        }
        try {
            List<String[]> data = readFromCsv(Constants.csvUserFilePath);
            boolean found = false;
            for (int i = 0; i < data.size(); i++) {
                if (data.get(i)[0].equals(user.getId())) {
                    data.set(i, new String[]{
                            user.getId(),
                            user.getSurname(),
                            user.getName(),
                            user.getPhoneNumber(),
                            user.getEmail(),
                            user.getPassword()
                    });
                    found = true;
                    break;
                }
            }
            writeToCsv(data, Constants.csvUserFilePath);
            return found;
        } catch (CsvException | IOException e) {
            logger.error(e.getMessage());
            return false;
        }
    }

    /**
     * Удаление пользователя из CSV
     * @param id идентификатор пользователя
     * @return успешность операции
     * @throws IOException
     * @throws CsvException
     */
    public boolean deleteUser(String id) throws IOException, CsvException {
        if(id == null){
            return false;
        }
        try {
            List<String[]> data = readFromCsv(Constants.csvUserFilePath);
            data.removeIf(row -> row[0].equals(id));
            writeToCsv(data, Constants.csvUserFilePath);
            return true;
        } catch (CsvException | IOException e) {
            logger.error(e.getMessage());
            return false;
        }
    }

    /**
     * Создание маршрута
     * @param route объект маршрута
     * @return успешность операции
     * @throws IOException
     * @throws CsvException
     */
    public boolean createRoute(Route route) throws IOException, CsvException {
        if(route == null){
            return false;
        }
        try {
            List<String[]> data = readFromCsv(Constants.csvRouteFilePath);
            data.add(new String[]{
                    route.getId(),
                    route.getDepartureStationId(),
                    route.getArrivalStationId(),
                    route.getTrainId(),
                    new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(route.getDepartureTime()),
                    new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(route.getArrivalTime()),
                    String.valueOf(route.getBasePrice())
            });
            writeToCsv(data, Constants.csvRouteFilePath);
            return true;
        } catch (CsvException | IOException e) {
            logger.error(e.getMessage());
            return false;
        }
    }

    /**
     * Чтение маршрута по ID
     * @param id идентификатор маршрута
     * @return объект маршрута
     * @throws IOException
     * @throws CsvException
     */
    public Route readRoute(String id) throws IOException, CsvException, ParseException {
        Route route = new Route();
        try {
            List<String[]> data = readFromCsv(Constants.csvRouteFilePath);
            for (String[] row : data) {
                if(row[0].equals(id)) {
                    route.setId(row[0]);
                    route.setDepartureStationId(row[1]);
                    route.setArrivalStationId(row[2]);
                    route.setTrainId(row[3]);
                    route.setDepartureTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(row[4]));
                    route.setArrivalTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(row[5]));
                    route.setBasePrice(Double.parseDouble(row[6]));
                    return route;
                }
            }
            throw new CsvException("Cannot find route with id " + id);
        } catch (CsvException | IOException | ParseException e) {
            logger.error(e.getMessage());
            throw e;
        }
    }

    /**
     * Поиск маршрутов с определенными параметрами
     * @param departureCity город отправления
     * @param arrivalCity город прибытия
     * @param date дата отправления
     * @return список информации о маршрутах
     * @throws IOException
     * @throws CsvException
     */
    public List<RouteInfo> searchRoutes(String departureCity, String arrivalCity, java.util.Date date) throws IOException, CsvException {
        List<RouteInfo> routes = new ArrayList<>();
        
        try {
            // Получаем маршруты
            List<String[]> routeData = readFromCsv(Constants.csvRouteFilePath);
            List<String[]> stationData = readFromCsv(Constants.csvStationFilePath);
            List<String[]> trainData = readFromCsv(Constants.csvTrainFilePath);
            
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String searchDate = dateFormat.format(date);
            
            for (String[] routeRow : routeData) {
                String routeId = routeRow[0];
                String departureStationId = routeRow[1];
                String arrivalStationId = routeRow[2];
                String trainId = routeRow[3];
                String departureTimeStr = routeRow[4];
                String arrivalTimeStr = routeRow[5];
                double basePrice = Double.parseDouble(routeRow[6]);
                
                // Проверяем дату отправления
                Date departureTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(departureTimeStr);
                if (!dateFormat.format(departureTime).equals(searchDate)) {
                    continue;
                }
                
                // Находим информацию о станциях
                String departureStationName = "";
                String arrivalStationName = "";
                String departureStationCity = "";
                String arrivalStationCity = "";
                
                for (String[] stationRow : stationData) {
                    if (stationRow[0].equals(departureStationId)) {
                        departureStationName = stationRow[1];
                        departureStationCity = stationRow[2];
                    }
                    if (stationRow[0].equals(arrivalStationId)) {
                        arrivalStationName = stationRow[1];
                        arrivalStationCity = stationRow[2];
                    }
                }
                
                // Проверяем соответствие городам
                if (!departureStationCity.toLowerCase().contains(departureCity.toLowerCase()) || 
                    !arrivalStationCity.toLowerCase().contains(arrivalCity.toLowerCase())) {
                    continue;
                }
                
                // Находим информацию о поезде
                String trainNumber = "";
                for (String[] trainRow : trainData) {
                    if (trainRow[0].equals(trainId)) {
                        trainNumber = trainRow[1];
                        break;
                    }
                }
                
                // Создаем объект RouteInfo
                RouteInfo routeInfo = new RouteInfo();
                routeInfo.setId(routeId);
                routeInfo.setDepartureStation(departureStationName);
                routeInfo.setArrivalStation(arrivalStationName);
                routeInfo.setDepartureCity(departureStationCity);
                routeInfo.setArrivalCity(arrivalStationCity);
                routeInfo.setTrainNumber(trainNumber);
                
                SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
                routeInfo.setDepartureTime(timeFormat.format(departureTime));
                routeInfo.setArrivalTime(timeFormat.format(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(arrivalTimeStr)));
                
                routeInfo.setPrice(basePrice);
                
                // Подсчет доступных мест
                routeInfo.setAvailableSeats(calculateAvailableSeats(routeId));
                
                routes.add(routeInfo);
            }
        } catch (Exception e) {
            logger.error("Error searching routes: {}", e.getMessage());
        }
        
        return routes;
    }
    
    /**
     * Подсчет доступных мест для маршрута
     * @param routeId идентификатор маршрута
     * @return количество доступных мест
     * @throws IOException
     * @throws CsvException
     */
    private int calculateAvailableSeats(String routeId) throws IOException, CsvException {
        try {
            // Получаем маршрут
            List<String[]> routeData = readFromCsv(Constants.csvRouteFilePath);
            String trainId = "";
            
            for (String[] routeRow : routeData) {
                if (routeRow[0].equals(routeId)) {
                    trainId = routeRow[3];
                    break;
                }
            }
            
            if (trainId.isEmpty()) {
                return 0;
            }
            
            // Получаем вагоны для поезда
            List<String[]> wagonData = readFromCsv(Constants.csvWagonFilePath);
            List<String> wagonIds = new ArrayList<>();
            
            for (String[] wagonRow : wagonData) {
                if (wagonRow[1].equals(trainId)) {
                    wagonIds.add(wagonRow[0]);
                }
            }
            
            // Подсчитываем доступные места
            List<String[]> seatData = readFromCsv(Constants.csvSeatFilePath);
            int availableSeats = 0;
            
            for (String[] seatRow : seatData) {
                if (wagonIds.contains(seatRow[1]) && seatRow[3].equals("true")) {
                    availableSeats++;
                }
            }
            
            return availableSeats;
        } catch (Exception e) {
            logger.error("Error calculating available seats: {}", e.getMessage());
            return 0;
        }
    }
    
    /**
     * Создание билета
     * @param ticket объект билета
     * @return успешность операции
     * @throws IOException
     * @throws CsvException
     */
    public boolean createTicket(Ticket ticket) throws IOException, CsvException {
        if(ticket == null){
            return false;
        }
        try {
            List<String[]> data = readFromCsv(Constants.csvTicketFilePath);
            data.add(new String[]{
                    ticket.getId(),
                    ticket.getUserId(),
                    ticket.getRouteId(),
                    ticket.getSeatId(),
                    new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(ticket.getBookingDate()),
                    ticket.getStatus().toString(),
                    String.valueOf(ticket.getPrice())
            });
            writeToCsv(data, Constants.csvTicketFilePath);
            
            // Обновляем доступность места
            updateSeatAvailability(ticket.getSeatId(), false);
            
            return true;
        } catch (CsvException | IOException e) {
            logger.error(e.getMessage());
            return false;
        }
    }
    
    /**
     * Обновление доступности места
     * @param seatId идентификатор места
     * @param isAvailable доступность
     * @return успешность операции
     * @throws IOException
     * @throws CsvException
     */
    private boolean updateSeatAvailability(String seatId, boolean isAvailable) throws IOException, CsvException {
        try {
            List<String[]> data = readFromCsv(Constants.csvSeatFilePath);
            boolean found = false;
            
            for (int i = 0; i < data.size(); i++) {
                if (data.get(i)[0].equals(seatId)) {
                    String[] row = data.get(i);
                    row[3] = String.valueOf(isAvailable);
                    data.set(i, row);
                    found = true;
                    break;
                }
            }
            
            if (found) {
                writeToCsv(data, Constants.csvSeatFilePath);
            }
            
            return found;
        } catch (CsvException | IOException e) {
            logger.error(e.getMessage());
            return false;
        }
    }
    
    /**
     * Чтение билета по ID
     * @param id идентификатор билета
     * @return объект билета
     * @throws IOException
     * @throws CsvException
     */
    public Ticket readTicket(String id) throws IOException, CsvException, ParseException {
        Ticket ticket = new Ticket();
        try {
            List<String[]> data = readFromCsv(Constants.csvTicketFilePath);
            for (String[] row : data) {
                if(row[0].equals(id)) {
                    ticket.setId(row[0]);
                    ticket.setUserId(row[1]);
                    ticket.setRouteId(row[2]);
                    ticket.setSeatId(row[3]);
                    ticket.setBookingDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(row[4]));
                    ticket.setStatus(Status.valueOf(row[5]));
                    ticket.setPrice(Double.parseDouble(row[6]));
                    return ticket;
                }
            }
            throw new CsvException("Cannot find ticket with id " + id);
        } catch (CsvException | IOException | ParseException e) {
            logger.error(e.getMessage());
            throw e;
        }
    }
    
/**
     * Обновление статуса билета
     * @param ticketId идентификатор билета
     * @param status новый статус
     * @return успешность операции
     * @throws IOException
     * @throws CsvException
     */
    public boolean updateTicketStatus(String ticketId, Status status) throws IOException, CsvException {
        try {
            List<String[]> data = readFromCsv(Constants.csvTicketFilePath);
            boolean found = false;
            String seatId = "";
            
            for (int i = 0; i < data.size(); i++) {
                if (data.get(i)[0].equals(ticketId)) {
                    String[] row = data.get(i);
                    seatId = row[3];
                    row[5] = status.toString();
                    data.set(i, row);
                    found = true;
                    break;
                }
            }
            
            if (found) {
                writeToCsv(data, Constants.csvTicketFilePath);
                
                // Если билет отменен, освобождаем место
                if (status == Status.CANCELED && !seatId.isEmpty()) {
                    updateSeatAvailability(seatId, true);
                }
            }
            
            return found;
        } catch (CsvException | IOException e) {
            logger.error(e.getMessage());
            return false;
        }
    }
    
    /**
     * Получение билетов пользователя
     * @param userId идентификатор пользователя
     * @return список билетов
     * @throws IOException
     * @throws CsvException
     */
    public List<Ticket> getUserTickets(String userId) throws IOException, CsvException {
        List<Ticket> tickets = new ArrayList<>();
        
        try {
            List<String[]> data = readFromCsv(Constants.csvTicketFilePath);
            
            for (String[] row : data) {
                if (row[1].equals(userId)) {
                    Ticket ticket = new Ticket();
                    ticket.setId(row[0]);
                    ticket.setUserId(row[1]);
                    ticket.setRouteId(row[2]);
                    ticket.setSeatId(row[3]);
                    try {
                        ticket.setBookingDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(row[4]));
                    } catch (ParseException e) {
                        logger.error("Error parsing date: {}", e.getMessage());
                        ticket.setBookingDate(new Date());
                    }
                    ticket.setStatus(Status.valueOf(row[5]));
                    ticket.setPrice(Double.parseDouble(row[6]));
                    tickets.add(ticket);
                }
            }
        } catch (CsvException | IOException e) {
            logger.error(e.getMessage());
        }
        
        return tickets;
    }
    
    /**
     * Создание транзакции оплаты
     * @param transaction объект транзакции
     * @return успешность операции
     * @throws IOException
     * @throws CsvException
     */
    public boolean createTransaction(Transaction transaction) throws IOException, CsvException {
        if(transaction == null){
            return false;
        }
        try {
            List<String[]> data = readFromCsv(Constants.csvTransactionFilePath);
            data.add(new String[]{
                    transaction.getId(),
                    transaction.getTicketId(),
                    new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(transaction.getDate()),
                    String.valueOf(transaction.getAmount()),
                    transaction.getPaymentMethod()
            });
            writeToCsv(data, Constants.csvTransactionFilePath);
            
            // Обновляем статус билета на PAID
            updateTicketStatus(transaction.getTicketId(), Status.PAID);
            
            return true;
        } catch (CsvException | IOException e) {
            logger.error(e.getMessage());
            return false;
        }
    }
    
    /**
     * Получение доступных мест для маршрута
     * @param routeId идентификатор маршрута
     * @return список доступных мест
     * @throws IOException
     * @throws CsvException
     */
    public List<Seat> getAvailableSeats(String routeId) throws IOException, CsvException {
        List<Seat> seats = new ArrayList<>();
        
        try {
            // Получаем маршрут и поезд
            List<String[]> routeData = readFromCsv(Constants.csvRouteFilePath);
            String trainId = "";
            
            for (String[] routeRow : routeData) {
                if (routeRow[0].equals(routeId)) {
                    trainId = routeRow[3];
                    break;
                }
            }
            
            if (trainId.isEmpty()) {
                return seats;
            }
            
            // Получаем вагоны для поезда
            List<String[]> wagonData = readFromCsv(Constants.csvWagonFilePath);
            List<String> wagonIds = new ArrayList<>();
            
            for (String[] wagonRow : wagonData) {
                if (wagonRow[1].equals(trainId)) {
                    wagonIds.add(wagonRow[0]);
                }
            }
            
            // Получаем доступные места
            List<String[]> seatData = readFromCsv(Constants.csvSeatFilePath);
            
            for (String[] seatRow : seatData) {
                if (wagonIds.contains(seatRow[1]) && seatRow[3].equals("true")) {
                    Seat seat = new Seat();
                    seat.setId(seatRow[0]);
                    seat.setWagonId(seatRow[1]);
                    seat.setNumber(seatRow[2]);
                    seat.setAvailable(Boolean.parseBoolean(seatRow[3]));
                    seat.setPriceMultiplier(Double.parseDouble(seatRow[4]));
                    seats.add(seat);
                }
            }
        } catch (Exception e) {
            logger.error("Error getting available seats: {}", e.getMessage());
        }
        
        return seats;
    }
    
    /**
     * Получение деталей для билета (маршрут, станции, поезд)
     * @param routeId идентификатор маршрута
     * @return информация о маршруте для билета
     * @throws IOException
     * @throws CsvException
     */
    public RouteInfo getRouteDetails(String routeId) throws IOException, CsvException {
        RouteInfo routeInfo = new RouteInfo();
        
        try {
            // Получаем маршрут
            List<String[]> routeData = readFromCsv(Constants.csvRouteFilePath);
            String departureStationId = "";
            String arrivalStationId = "";
            String trainId = "";
            String departureTimeStr = "";
            String arrivalTimeStr = "";
            double basePrice = 0.0;
            
            for (String[] routeRow : routeData) {
                if (routeRow[0].equals(routeId)) {
                    departureStationId = routeRow[1];
                    arrivalStationId = routeRow[2];
                    trainId = routeRow[3];
                    departureTimeStr = routeRow[4];
                    arrivalTimeStr = routeRow[5];
                    basePrice = Double.parseDouble(routeRow[6]);
                    break;
                }
            }
            
            if (departureStationId.isEmpty() || arrivalStationId.isEmpty() || trainId.isEmpty()) {
                return routeInfo;
            }
            
            // Получаем информацию о станциях
            List<String[]> stationData = readFromCsv(Constants.csvStationFilePath);
            String departureStationName = "";
            String arrivalStationName = "";
            String departureStationCity = "";
            String arrivalStationCity = "";
            
            for (String[] stationRow : stationData) {
                if (stationRow[0].equals(departureStationId)) {
                    departureStationName = stationRow[1];
                    departureStationCity = stationRow[2];
                }
                if (stationRow[0].equals(arrivalStationId)) {
                    arrivalStationName = stationRow[1];
                    arrivalStationCity = stationRow[2];
                }
            }
            
            // Получаем информацию о поезде
            List<String[]> trainData = readFromCsv(Constants.csvTrainFilePath);
            String trainNumber = "";
            
            for (String[] trainRow : trainData) {
                if (trainRow[0].equals(trainId)) {
                    trainNumber = trainRow[1];
                    break;
                }
            }
            
            // Заполняем объект RouteInfo
            routeInfo.setId(routeId);
            routeInfo.setDepartureStation(departureStationName);
            routeInfo.setArrivalStation(arrivalStationName);
            routeInfo.setDepartureCity(departureStationCity);
            routeInfo.setArrivalCity(arrivalStationCity);
            routeInfo.setTrainNumber(trainNumber);
            
            try {
                SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
                routeInfo.setDepartureTime(timeFormat.format(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(departureTimeStr)));
                routeInfo.setArrivalTime(timeFormat.format(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(arrivalTimeStr)));
            } catch (ParseException e) {
                logger.error("Error parsing date: {}", e.getMessage());
            }
            
            routeInfo.setPrice(basePrice);
            routeInfo.setAvailableSeats(calculateAvailableSeats(routeId));
            
        } catch (Exception e) {
            logger.error("Error getting route details: {}", e.getMessage());
        }
        
        return routeInfo;
    }
}
