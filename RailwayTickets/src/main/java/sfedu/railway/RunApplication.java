package sfedu.railway;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sfedu.railway.api.*;
import sfedu.railway.models.*;
import sfedu.railway.utils.Status;

import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.*;

public class RunApplication {
    private static final Logger logger = LoggerFactory.getLogger(RunApplication.class);
    private static Scanner sc = new Scanner(System.in);
    private static String dataSource = "PostgreSQL"; // По умолчанию используем PostgreSQL
    private static DataProviderPSQL dataProviderPSQL;
    private static DataProviderCsv dataProviderCsv;
    private static User currentUser = null;

    public static void main(String[] args) {
        try {
            initializeDataProviders();
            showWelcomeScreen();
            
            // Основной цикл приложения
            boolean exit = false;
            while (!exit) {
                if (currentUser == null) {
                    // Пользователь не авторизован
                    exit = showAuthMenu();
                } else {
                    // Пользователь авторизован
                    showMainMenu();
                }
            }
            
            System.out.println("Спасибо за использование нашей системы. До свидания!");
            
        } catch (Exception e) {
            logger.error("Ошибка при запуске приложения: " + e.getMessage());
            System.out.println("Произошла ошибка при запуске приложения. Подробности в логах.");
        } finally {
            // Закрываем сканнер перед выходом
            if (sc != null) {
                sc.close();
            }
        }
    }
    
    /**
     * Инициализация провайдеров данных
     */
    private static void initializeDataProviders() {
        try {
            // Инициализируем провайдеры данных
            Connection connection = DataProviderPSQL.getConnection();
            dataProviderPSQL = new DataProviderPSQL();
            dataProviderCsv = new DataProviderCsv();
            
            logger.info("Провайдеры данных успешно инициализированы");
        } catch (Exception e) {
            logger.error("Ошибка при инициализации провайдеров данных: " + e.getMessage());
            System.out.println("Ошибка при подключении к базе данных. Подробности в логах.");
            System.exit(1);
        }
    }
    
    /**
     * Отображение приветственного экрана
     */
    private static void showWelcomeScreen() {
        System.out.println("======================================================");
        System.out.println("  СИСТЕМА ПРОДАЖИ И БРОНИРОВАНИЯ ЖД БИЛЕТОВ");
        System.out.println("======================================================");
        System.out.println("Добро пожаловать в систему электронных ЖД билетов!");
        System.out.println();
        
        // Выбор источника данных
        System.out.println("Выберите источник данных:");
        System.out.println("1. PostgreSQL");
        System.out.println("2. CSV-файлы");
        System.out.print("Ваш выбор: ");
        int choice = getIntInput();
        
        switch (choice) {
            case 1:
                dataSource = "PostgreSQL";
                break;
            case 2:
                dataSource = "CSV";
                break;
            default:
                System.out.println("Неверный выбор. Используем PostgreSQL по умолчанию.");
                dataSource = "PostgreSQL";
        }
        
        System.out.println("Выбран источник данных: " + dataSource);
        System.out.println();
    }
    
    /**
     * Меню авторизации
     * @return true, если нужно выйти из приложения
     */
    private static boolean showAuthMenu() {
        System.out.println("======================================================");
        System.out.println("  АВТОРИЗАЦИЯ");
        System.out.println("======================================================");
        System.out.println("1. Вход в систему");
        System.out.println("2. Регистрация");
        System.out.println("3. Выход");
        System.out.print("Ваш выбор: ");
        
        int choice = getIntInput();
        
        switch (choice) {
            case 1:
                loginUser();
                return false;
            case 2:
                registerUser();
                return false;
            case 3:
                return true;
            default:
                System.out.println("Неверный выбор. Попробуйте снова.");
                return false;
        }
    }
    
    /**
     * Вход в систему
     */
    private static void loginUser() {
        System.out.println("\n--- Вход в систему ---");
        System.out.print("Введите email: ");
        String email = sc.nextLine();
        System.out.print("Введите пароль: ");
        String password = sc.nextLine();
        
        try {
            if (dataSource.equals("PostgreSQL")) {
                currentUser = dataProviderPSQL.authenticateUser(email, password);
            } else {
                currentUser = dataProviderCsv.authenticateUser(email, password);
            }
            
            System.out.println("Успешный вход в систему. Добро пожаловать, " + currentUser.getName() + "!");
        } catch (Exception e) {
            logger.error("Ошибка при входе в систему: " + e.getMessage());
            System.out.println("Ошибка при входе в систему. Проверьте правильность email и пароля.");
        }
    }
    
    /**
     * Регистрация нового пользователя
     */
    private static void registerUser() {
        System.out.println("\n--- Регистрация нового пользователя ---");
        
        System.out.print("Введите фамилию: ");
        String surname = sc.nextLine();
        
        System.out.print("Введите имя: ");
        String name = sc.nextLine();
        
        System.out.print("Введите номер телефона: ");
        String phoneNumber = sc.nextLine();
        
        System.out.print("Введите email: ");
        String email = sc.nextLine();
        
        System.out.print("Введите пароль: ");
        String password = sc.nextLine();
        
        User user = new User(surname, name, phoneNumber, email, password);
        
        try {
            boolean success = false;
            if (dataSource.equals("PostgreSQL")) {
                success = dataProviderPSQL.createUser(user);
            } else {
                success = dataProviderCsv.createUser(user);
            }
            
            if (success) {
                System.out.println("Регистрация успешно завершена! Теперь вы можете войти в систему.");
            } else {
                System.out.println("Ошибка при регистрации. Попробуйте еще раз.");
            }
        } catch (Exception e) {
            logger.error("Ошибка при регистрации пользователя: " + e.getMessage());
            System.out.println("Ошибка при регистрации. Возможно, такой email уже существует.");
        }
    }
    
    /**
     * Основное меню (после авторизации)
     */
    private static void showMainMenu() {
        System.out.println("\n======================================================");
        System.out.println("  ГЛАВНОЕ МЕНЮ");
        System.out.println("======================================================");
        System.out.println("1. Поиск маршрутов");
        System.out.println("2. Мои билеты");
        System.out.println("3. Личный кабинет");
        System.out.println("4. Выход из аккаунта");
        System.out.print("Ваш выбор: ");
        
        int choice = getIntInput();
        
        switch (choice) {
            case 1:
                searchRoutes();
                break;
            case 2:
                showUserTickets();
                break;
            case 3:
                showUserProfile();
                break;
            case 4:
                currentUser = null;
                System.out.println("Вы вышли из аккаунта.");
                break;
            default:
                System.out.println("Неверный выбор. Попробуйте снова.");
        }
    }
    
    /**
     * Поиск маршрутов
     */
    private static void searchRoutes() {
        System.out.println("\n--- Поиск маршрутов ---");
        
        System.out.print("Введите город отправления: ");
        String departureCity = sc.nextLine();
        
        System.out.print("Введите город прибытия: ");
        String arrivalCity = sc.nextLine();
        
        System.out.print("Введите дату отправления (в формате ДД.ММ.ГГГГ): ");
        String dateStr = sc.nextLine();
        
        try {
            // Преобразуем строку даты в объект Date
            Date date = new SimpleDateFormat("dd.MM.yyyy").parse(dateStr);
            
            // Ищем маршруты
            List<RouteInfo> routes;
            if (dataSource.equals("PostgreSQL")) {
                routes = dataProviderPSQL.searchRoutes(departureCity, arrivalCity, date);
            } else {
                routes = dataProviderCsv.searchRoutes(departureCity, arrivalCity, date);
            }
            
            if (routes.isEmpty()) {
                System.out.println("Маршруты не найдены. Попробуйте изменить параметры поиска.");
            } else {
                // Выводим список найденных маршрутов
                System.out.println("\nНайденные маршруты:");
                System.out.println("-------------------------------------------------------");
                System.out.printf("%-5s %-20s %-20s %-10s %-10s %-10s %-6s\n",
                        "№", "Отправление", "Прибытие", "Время отпр.", "Время приб.", "Цена", "Места");
                System.out.println("-------------------------------------------------------");
                
                for (int i = 0; i < routes.size(); i++) {
                    RouteInfo route = routes.get(i);
                    System.out.printf("%-5d %-20s %-20s %-10s %-10s %-10.2f %-6d\n",
                            (i + 1),
                            route.getDepartureStation() + " (" + route.getDepartureCity() + ")",
                            route.getArrivalStation() + " (" + route.getArrivalCity() + ")",
                            route.getDepartureTime(),
                            route.getArrivalTime(),
                            route.getPrice(),
                            route.getAvailableSeats());
                }
                
                // Предлагаем выбрать маршрут для бронирования
                System.out.print("\nВыберите номер маршрута для бронирования (0 - вернуться назад): ");
                int routeChoice = getIntInput();
                
                if (routeChoice > 0 && routeChoice <= routes.size()) {
                    // Пользователь выбрал маршрут
                    RouteInfo selectedRoute = routes.get(routeChoice - 1);
                    bookTicket(selectedRoute);
                }
            }
        } catch (Exception e) {
            logger.error("Ошибка при поиске маршрутов: " + e.getMessage());
            System.out.println("Ошибка при поиске маршрутов. Проверьте правильность ввода данных.");
        }
    }
    
    /**
     * Бронирование билета
     * @param route выбранный маршрут
     */
    private static void bookTicket(RouteInfo route) {
        System.out.println("\n--- Бронирование билета ---");
        System.out.println("Маршрут: " + route.getDepartureStation() + " -> " + route.getArrivalStation());
        System.out.println("Дата и время отправления: " + route.getDepartureTime());
        System.out.println("Базовая цена: " + route.getPrice() + " руб.");
        
        try {
            // Получаем доступные места
            List<Seat> availableSeats;
            if (dataSource.equals("PostgreSQL")) {
                availableSeats = dataProviderPSQL.getAvailableSeats(route.getId());
            } else {
                availableSeats = dataProviderCsv.getAvailableSeats(route.getId());
            }
            
            if (availableSeats.isEmpty()) {
                System.out.println("К сожалению, нет доступных мест на этот маршрут.");
                return;
            }
            
            // Выводим список доступных мест
            System.out.println("\nДоступные места:");
            System.out.println("----------------------------------");
            System.out.printf("%-5s %-10s %-15s %-10s\n",
                    "№", "Вагон", "Место", "Цена");
            System.out.println("----------------------------------");
            
            for (int i = 0; i < availableSeats.size(); i++) {
                Seat seat = availableSeats.get(i);
                double price = route.getPrice() * seat.getPriceMultiplier();
                System.out.printf("%-5d %-10s %-15s %-10.2f\n",
                        (i + 1),
                        "Вагон " + getWagonNumber(seat.getWagonId()),
                        "Место " + seat.getNumber(),
                        price);
            }
            
            // Предлагаем выбрать место
            System.out.print("\nВыберите номер места (0 - вернуться назад): ");
            int seatChoice = getIntInput();
            
            if (seatChoice > 0 && seatChoice <= availableSeats.size()) {
                // Пользователь выбрал место
                Seat selectedSeat = availableSeats.get(seatChoice - 1);
                double price = route.getPrice() * selectedSeat.getPriceMultiplier();
                
                // Создаем билет
                Ticket ticket = new Ticket(currentUser.getId(), route.getId(), selectedSeat.getId(), price);
                
                boolean success = false;
                if (dataSource.equals("PostgreSQL")) {
                    success = dataProviderPSQL.createTicket(ticket);
                } else {
                    success = dataProviderCsv.createTicket(ticket);
                }
                
                if (success) {
                    System.out.println("\nБилет успешно забронирован!");
                    System.out.println("Номер билета: " + ticket.getId());
                    System.out.println("Статус: ЗАБРОНИРОВАН");
                    System.out.println("\nЧтобы завершить покупку, выберите способ оплаты:");
                    System.out.println("1. Банковская карта");
                    System.out.println("2. Электронный кошелек");
                    System.out.println("3. Отложить оплату");
                    System.out.print("Ваш выбор: ");
                    
                    int paymentChoice = getIntInput();
                    
                    if (paymentChoice == 1 || paymentChoice == 2) {
                        String paymentMethod = paymentChoice == 1 ? "Банковская карта" : "Электронный кошелек";
                        
                        // Имитация процесса оплаты
                        System.out.println("\nОбработка платежа...");
                        Thread.sleep(1500);
                        
                        // Создаем транзакцию
                        Transaction transaction = new Transaction(ticket.getId(), price, paymentMethod);
                        
                        if (dataSource.equals("PostgreSQL")) {
                            dataProviderPSQL.createTransaction(transaction);
                        } else {
                            dataProviderCsv.createTransaction(transaction);
                        }
                        
                        System.out.println("\nПлатеж успешно обработан!");
                        System.out.println("Билет оплачен. Статус изменен на ОПЛАЧЕН.");
                        System.out.println("Номер транзакции: " + transaction.getId());
                    } else {
                        System.out.println("\nОплата отложена. Не забудьте оплатить билет до отправления поезда.");
                    }
                } else {
                    System.out.println("\nОшибка при бронировании билета. Попробуйте еще раз.");
                }
            }
        } catch (Exception e) {
            logger.error("Ошибка при бронировании билета: " + e.getMessage());
            System.out.println("Ошибка при бронировании билета.");
        }
    }
    
    /**
     * Получение номера вагона
     * @param wagonId идентификатор вагона
     * @return номер вагона
     */
    private static String getWagonNumber(String wagonId) {
        // В реальном приложении здесь был бы запрос к БД
        // Для упрощения возвращаем что-то условное
        return "1";
    }
    
    /**
     * Просмотр билетов пользователя
     */
    private static void showUserTickets() {
        System.out.println("\n--- Мои билеты ---");
        
        try {
            // Получаем билеты пользователя
            List<Ticket> tickets;
            if (dataSource.equals("PostgreSQL")) {
                tickets = dataProviderPSQL.getUserTickets(currentUser.getId());
            } else {
                tickets = dataProviderCsv.getUserTickets(currentUser.getId());
            }
            
            if (tickets.isEmpty()) {
                System.out.println("У вас пока нет забронированных или купленных билетов.");
            } else {
                // Выводим список билетов
                System.out.println("\nВаши билеты:");
                System.out.println("----------------------------------------------------------------------");
                System.out.printf("%-5s %-10s %-30s %-15s %-10s\n",
                        "№", "Статус", "Маршрут", "Дата", "Цена");
                System.out.println("----------------------------------------------------------------------");
                
                for (int i = 0; i < tickets.size(); i++) {
                    Ticket ticket = tickets.get(i);
                    
                    // Получаем информацию о маршруте
                    RouteInfo routeInfo;
                    if (dataSource.equals("PostgreSQL")) {
                        routeInfo = dataProviderPSQL.getRouteDetails(ticket.getRouteId());
                    } else {
                        routeInfo = dataProviderCsv.getRouteDetails(ticket.getRouteId());
                    }
                    
                    String routeStr = routeInfo.getDepartureStation() + " -> " + routeInfo.getArrivalStation();
                    String dateStr = routeInfo.getDepartureTime();
                    
                    System.out.printf("%-5d %-10s %-30s %-15s %-10.2f\n",
                            (i + 1),
                            ticket.getStatus(),
                            routeStr,
                            dateStr,
                            ticket.getPrice());
                }
                
                // Предлагаем выбрать билет для действий
                System.out.print("\nВыберите номер билета для действий (0 - вернуться назад): ");
                int ticketChoice = getIntInput();
                
                if (ticketChoice > 0 && ticketChoice <= tickets.size()) {
                    // Пользователь выбрал билет
                    Ticket selectedTicket = tickets.get(ticketChoice - 1);
                    showTicketActions(selectedTicket);
                }
            }
        } catch (Exception e) {
            logger.error("Ошибка при получении билетов: " + e.getMessage());
            System.out.println("Ошибка при получении списка билетов.");
        }
    }
    
/**
     * Действия с билетом
     * @param ticket выбранный билет
     */
    private static void showTicketActions(Ticket ticket) {
        System.out.println("\n--- Действия с билетом ---");
        
        // Получаем информацию о маршруте
        try {
            RouteInfo routeInfo;
            if (dataSource.equals("PostgreSQL")) {
                routeInfo = dataProviderPSQL.getRouteDetails(ticket.getRouteId());
            } else {
                routeInfo = dataProviderCsv.getRouteDetails(ticket.getRouteId());
            }
            
            System.out.println("Билет №: " + ticket.getId());
            System.out.println("Статус: " + ticket.getStatus());
            System.out.println("Маршрут: " + routeInfo.getDepartureStation() + " -> " + routeInfo.getArrivalStation());
            System.out.println("Отправление: " + routeInfo.getDepartureTime());
            System.out.println("Цена: " + ticket.getPrice() + " руб.");
            
            System.out.println("\nДоступные действия:");
            
            // Доступные действия зависят от статуса билета
            if (ticket.getStatus() == Status.BOOKED) {
                System.out.println("1. Оплатить билет");
                System.out.println("2. Отменить бронирование");
                System.out.println("3. Вернуться назад");
                System.out.print("Ваш выбор: ");
                
                int choice = getIntInput();
                
                switch (choice) {
                    case 1:
                        payTicket(ticket);
                        break;
                    case 2:
                        cancelTicket(ticket);
                        break;
                }
            } else if (ticket.getStatus() == Status.PAID) {
                System.out.println("1. Вернуть билет");
                System.out.println("2. Вернуться назад");
                System.out.print("Ваш выбор: ");
                
                int choice = getIntInput();
                
                if (choice == 1) {
                    cancelTicket(ticket);
                }
            } else {
                System.out.println("1. Вернуться назад");
                System.out.print("Ваш выбор: ");
                getIntInput(); // просто считываем ввод
            }
        } catch (Exception e) {
            logger.error("Ошибка при работе с билетом: " + e.getMessage());
            System.out.println("Ошибка при работе с билетом.");
        }
    }
    
    /**
     * Оплата билета
     * @param ticket билет для оплаты
     */
    private static void payTicket(Ticket ticket) {
        System.out.println("\n--- Оплата билета ---");
        System.out.println("Выберите способ оплаты:");
        System.out.println("1. Банковская карта");
        System.out.println("2. Электронный кошелек");
        System.out.println("3. Отменить оплату");
        System.out.print("Ваш выбор: ");
        
        int paymentChoice = getIntInput();
        
        if (paymentChoice == 1 || paymentChoice == 2) {
            String paymentMethod = paymentChoice == 1 ? "Банковская карта" : "Электронный кошелек";
            
            try {
                // Имитация процесса оплаты
                System.out.println("\nОбработка платежа...");
                Thread.sleep(1500);
                
                // Создаем транзакцию
                Transaction transaction = new Transaction(ticket.getId(), ticket.getPrice(), paymentMethod);
                
                boolean success = false;
                if (dataSource.equals("PostgreSQL")) {
                    success = dataProviderPSQL.createTransaction(transaction);
                } else {
                    success = dataProviderCsv.createTransaction(transaction);
                }
                
                if (success) {
                    System.out.println("\nПлатеж успешно обработан!");
                    System.out.println("Билет оплачен. Статус изменен на ОПЛАЧЕН.");
                    System.out.println("Номер транзакции: " + transaction.getId());
                } else {
                    System.out.println("\nОшибка при обработке платежа. Попробуйте еще раз.");
                }
            } catch (Exception e) {
                logger.error("Ошибка при оплате билета: " + e.getMessage());
                System.out.println("Ошибка при оплате билета.");
            }
        }
    }
    
    /**
     * Отмена билета
     * @param ticket билет для отмены
     */
    private static void cancelTicket(Ticket ticket) {
        System.out.println("\n--- Отмена билета ---");
        System.out.println("Вы уверены, что хотите отменить билет? (1 - Да, 2 - Нет)");
        System.out.print("Ваш выбор: ");
        
        int choice = getIntInput();
        
        if (choice == 1) {
            try {
                boolean success = false;
                if (dataSource.equals("PostgreSQL")) {
                    success = dataProviderPSQL.updateTicketStatus(ticket.getId(), Status.CANCELED);
                } else {
                    success = dataProviderCsv.updateTicketStatus(ticket.getId(), Status.CANCELED);
                }
                
                if (success) {
                    System.out.println("\nБилет успешно отменен.");
                    if (ticket.getStatus() == Status.PAID) {
                        System.out.println("Средства будут возвращены на счет в течение 3-5 рабочих дней.");
                    }
                } else {
                    System.out.println("\nОшибка при отмене билета. Попробуйте еще раз.");
                }
            } catch (Exception e) {
                logger.error("Ошибка при отмене билета: " + e.getMessage());
                System.out.println("Ошибка при отмене билета.");
            }
        }
    }
    
    /**
     * Просмотр и редактирование профиля пользователя
     */
    private static void showUserProfile() {
        System.out.println("\n--- Личный кабинет ---");
        System.out.println("Информация о пользователе:");
        System.out.println("ФИО: " + currentUser.getSurname() + " " + currentUser.getName());
        System.out.println("Телефон: " + currentUser.getPhoneNumber());
        System.out.println("Email: " + currentUser.getEmail());
        
        System.out.println("\nДоступные действия:");
        System.out.println("1. Редактировать профиль");
        System.out.println("2. Вернуться в главное меню");
        System.out.print("Ваш выбор: ");
        
        int choice = getIntInput();
        
        if (choice == 1) {
            editUserProfile();
        }
    }
    
    /**
     * Редактирование профиля пользователя
     */
    private static void editUserProfile() {
        System.out.println("\n--- Редактирование профиля ---");
        
        System.out.println("Оставьте поле пустым, если не хотите его менять.");
        
        System.out.print("Фамилия [" + currentUser.getSurname() + "]: ");
        String surname = sc.nextLine().trim();
        if (!surname.isEmpty()) {
            currentUser.setSurname(surname);
        }
        
        System.out.print("Имя [" + currentUser.getName() + "]: ");
        String name = sc.nextLine().trim();
        if (!name.isEmpty()) {
            currentUser.setName(name);
        }
        
        System.out.print("Телефон [" + currentUser.getPhoneNumber() + "]: ");
        String phoneNumber = sc.nextLine().trim();
        if (!phoneNumber.isEmpty()) {
            currentUser.setPhoneNumber(phoneNumber);
        }
        
        System.out.print("Email [" + currentUser.getEmail() + "]: ");
        String email = sc.nextLine().trim();
        if (!email.isEmpty()) {
            currentUser.setEmail(email);
        }
        
        System.out.print("Новый пароль (оставьте пустым, чтобы не менять): ");
        String password = sc.nextLine().trim();
        if (!password.isEmpty()) {
            currentUser.setPassword(password);
        }
        
        try {
            boolean success = false;
            if (dataSource.equals("PostgreSQL")) {
                success = dataProviderPSQL.updateUser(currentUser);
            } else {
                success = dataProviderCsv.updateUser(currentUser);
            }
            
            if (success) {
                System.out.println("\nПрофиль успешно обновлен!");
            } else {
                System.out.println("\nОшибка при обновлении профиля. Попробуйте еще раз.");
            }
        } catch (Exception e) {
            logger.error("Ошибка при обновлении профиля: " + e.getMessage());
            System.out.println("Ошибка при обновлении профиля.");
        }
    }
    
    /**
     * Получение целочисленного ввода с обработкой ошибок
     * @return введенное целое число
     */
    private static int getIntInput() {
        try {
            String input = sc.nextLine();
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            return 0; // Возвращаем 0 при некорректном вводе
        }
    }
}
