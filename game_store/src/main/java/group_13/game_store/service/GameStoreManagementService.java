package group_13.game_store.service;

import java.util.List;
import java.sql.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import group_13.game_store.model.Game;
import group_13.game_store.model.GameCategory;
import group_13.game_store.model.Promotion;
import group_13.game_store.model.Employee;
import group_13.game_store.model.Customer;

import group_13.game_store.repository.EmployeeRepository;
import group_13.game_store.repository.GameCategoryRepository;
import group_13.game_store.repository.GameRepository;
import group_13.game_store.repository.PromotionRepository;
import group_13.game_store.repository.CustomerRepository;

@Service
public class GameStoreManagementService {

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private GameCategoryRepository gameCategoryRepository;

    @Autowired
    private PromotionRepository promotionRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private AccountService accountService;

    // ************************** GAME MANAGEMENT **************************

    // Add a new game -- Permission req (Only Owner)
    @Transactional
    public Game addGame(String title, String description, String img, int stock, double price,
                    String parentalRating, Game.VisibilityStatus status, int categoryId) {

        // Validate fields
        if (title == null || title.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Title cannot be null or empty.");
        }
        if (description == null || description.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Description cannot be null or empty.");
        }
        if (img == null || img.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Image URL cannot be null or empty.");
        }
        if (stock < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Stock cannot be negative.");
        }
        if (price <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Price must be greater than zero.");
        }
        if (parentalRating == null || parentalRating.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Parental rating cannot be null or empty.");
        }
        if (!gameCategoryRepository.findById(categoryId).isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Invalid category ID.");
        }

        // Create and save the game with the provided status
        Game game = new Game(title, description, img, stock, price, parentalRating, status,
                             gameCategoryRepository.findById(categoryId).get());
        gameRepository.save(game);

        return game; // Return the created game
    }

    // Archive an existing game
    @Transactional
    public void archiveGame(int gameId) {
        // Set status of the game to Archived
        Game game = gameRepository.findByGameID(gameId);
        if (game != null) {
            game.setStatus(Game.VisibilityStatus.Archived);
            gameRepository.save(game);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Game with ID " + gameId + " not found.");
        }
    }

    // Retrieve all games
    public List<Game> getAllGames() {
        return (List<Game>) gameRepository.findAll();
    }

    // Update an existing game
    @Transactional
    public Game updateGame(int gameId, String title, String description, String img, int stock, double price,
            String parentalRating, Game.VisibilityStatus status, int categoryId) {

        // Validate fields
        if (title == null || title.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Title cannot be null or empty.");
        }
        if (description == null || description.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Description cannot be null or empty.");
        }
        if (img == null || img.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Image URL cannot be null or empty.");
        }
        if (stock < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Stock cannot be negative.");
        }
        if (price <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Price must be greater than zero.");
        }
        if (parentalRating == null || parentalRating.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Parental rating cannot be null or empty.");
        }
        if (!gameCategoryRepository.findById(categoryId).isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Invalid category ID.");
        }

        Game game = gameRepository.findByGameID(gameId);
        if (game != null) {
            game.setTitle(title);
            game.setDescription(description);
            game.setImg(img);
            game.setStock(stock);
            game.setPrice(price);
            game.setParentalRating(parentalRating);
            game.setStatus(status);
            game.setCategory(gameCategoryRepository.findById(categoryId).get());
            gameRepository.save(game);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Game with ID " + gameId + " not found.");
        }

        return game; 
    }

    // ************************** CATEGORY MANAGEMENT **************************

    // Add a new category -- Permission req (Only Owner)
    @Transactional
    public GameCategory addCategory(String name, String description) {

        // Validate fields
        if (name == null || name.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Category name cannot be null or empty.");
        }
        if (description == null || description.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Category description cannot be null or empty.");
        }

        GameCategory category = new GameCategory(description, GameCategory.VisibilityStatus.Visible, name);
        gameCategoryRepository.save(category);
        
        return category;
    }

    // Archive an existing category -- Permission req (Only Owner)
    @Transactional
    public GameCategory archiveCategory(int categoryId, String username){
        GameCategory category = gameCategoryRepository.findByCategoryID(categoryId);
        if (category != null) {
            if (accountService.hasPermission(username, 3))
            {
                category.setStatus(GameCategory.VisibilityStatus.Archived);
            }
            else
            {
                category.setStatus(GameCategory.VisibilityStatus.PendingArchive);
            }
            gameCategoryRepository.save(category);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Category with ID " + categoryId + " not found.");
        }
        
        return category;
    }

    // Retrieve all categories
    public List<GameCategory> getAllCategories(){
        return (List<GameCategory>) gameCategoryRepository.findAll();
    }

    // Retrieve category by id
    public GameCategory getCategoryById(int id){
        GameCategory gameCategory = gameCategoryRepository.findByCategoryID(id);
        if (gameCategory == null)
        {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Category with ID " + id + " not found.");
        }
        return gameCategory;
    }

    // Retrieve all pending archive visibility categories
    public List<GameCategory> getAllPendingArchiveCategories(){
        List<GameCategory.VisibilityStatus> pendingArchive = List.of(GameCategory.VisibilityStatus.PendingArchive);
        return (List<GameCategory>) gameCategoryRepository.findByStatusIn(pendingArchive);
    }

    // ************************** PROMOTION MANAGEMENT **************************

    // Add a new promotion -- Permission req (Only Owner)
    @Transactional
    public Promotion addPromotion(int percentage, Date startDate, Date endDate, String title,
            String description) {

        // Validate fields
        if (percentage <= 0 || percentage > 100) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Percentage must be between 1 and 100.");
        }
        if (title == null || title.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Title cannot be null or empty.");
        }
        if (description == null || description.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Description cannot be null or empty.");
        }
        if (startDate == null || endDate == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Start and end dates cannot be null.");
        }
        if (endDate.before(startDate)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "End date cannot be before start date.");
        }

        Promotion promotion = new Promotion(percentage, startDate, endDate, title, description);
        promotionRepository.save(promotion);
        return promotion;
    }

    @Transactional
    public Promotion addPromotionToGame(int promotionID, int gameID) {
        Promotion promotion = promotionRepository.findByPromotionID(promotionID);
        Game game = gameRepository.findByGameID(gameID);

        if (promotion == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Promotion with ID " + promotionID + " not found.");
        }
        if (game == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Game with ID " + gameID + " not found.");
        }

        game.setPromotion(promotion);
        game = gameRepository.save(game);

        return game.getPromotion();
    }

    @Transactional
    public Promotion removePromotionFromGame(int gameID) {
        Game game = gameRepository.findByGameID(gameID);
        Promotion promotion = game.getPromotion();

        if (game == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Game with ID " + gameID + " not found.");
        }

        if(promotion == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Game with ID " + gameID + " does not have a promotion.");
        }

        game.setPromotion(null);
        game = gameRepository.save(game);

        return game.getPromotion();
    }

    // Update an existing promotion
    @Transactional
    public Promotion updatePromotion(int promotionID, int percentage, Date startDate, Date endDate,
            String title, String description) {
                
        // Validate fields
        if (percentage <= 0 || percentage > 100) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Percentage must be between 1 and 100.");
        }
        if (title == null || title.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Title cannot be null or empty.");
        }
        if (description == null || description.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Description cannot be null or empty.");
        }
        if (startDate == null || endDate == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Start and end dates cannot be null.");
        }
        if (endDate.before(startDate)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "End date cannot be before start date.");
        }

        Promotion updatedPromotion = promotionRepository.findByPromotionID(promotionID);
        if (updatedPromotion != null) {
            updatedPromotion.setPercentage(percentage);
            updatedPromotion.setStartDate(startDate);
            updatedPromotion.setEndDate(endDate);
            updatedPromotion.setTitle(title);
            updatedPromotion.setDescription(description);

            // Save the updated promotion
            promotionRepository.save(updatedPromotion);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Promotion with ID " + promotionID + " not found.");
        }

        return updatedPromotion;
    }

    // Delete an existing promotion
    @Transactional
    public void deletePromotion(int promotionId){
        // Look for the promotion in the promotion repo
        Promotion promotion = promotionRepository.findByPromotionID(promotionId);

        // Make sure a promotion was found. Throw an error otherwise
        if(promotion == null) { 
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Promotion with ID " + promotionId + " not found.");
        }

        promotionRepository.delete(promotion);
    }

    // Retrieve all promotions
    public List<Promotion> getAllPromotions() {
        return (List<Promotion>) promotionRepository.findAll();
    }

    // Retrieve all promotions for a specific game
    public List<Promotion> getAllGamePromotions(int gameID) {
        return (List<Promotion>) promotionRepository.findByGame_GameID(gameID);
    }

    // Retrieve a promotion by promotionID
    public Promotion getPromotion(int promotionID) {
        Promotion promotion = promotionRepository.findByPromotionID(promotionID);
        if (promotion == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Promotion with ID " + promotionID + " not found.");
        }
        return promotion;
    }

    // ************************** DASHBOARD FUNCTIONALITY **************************

    // Retrieve all employees
    public List<Employee> getAllEmployees() {
        return (List<Employee>) employeeRepository.findAll();
    }

    // Retrieve all customers
    public List<Customer> getAllCustomers() {
        return (List<Customer>) customerRepository.findAll();
    }

    // Retrieve game archive requests
    public List<Game> getGameArchiveRequests() {
        List<Game.VisibilityStatus> pendingArchive = List.of(Game.VisibilityStatus.PendingArchive);
        return (List<Game>) gameRepository.findByStatusIn(pendingArchive);
    }

    // ************************** GAME ARCHIVING **************************

    // Request a game to be archived (for employees) -- Permission req
    @Transactional
    public void archiveGameRequest(int gameId) {
        Game game = gameRepository.findByGameID(gameId);
        if (game != null) {
            game.setStatus(Game.VisibilityStatus.PendingArchive);
            gameRepository.save(game);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Game with ID " + gameId + " not found.");
        }
    }

    // ************************** EMPLOYEE ACCOUNT MANAGEMENT **************************

    // Add a new employee account -- Permission req (Only Owner)
    @Transactional
    public void addEmployee(String name, String username, String email, String password,
            String phoneNumber, boolean isActive) {
        // Validate fields
        if (name == null || name.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Name cannot be null or empty.");
        }
        if (username == null || username.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username cannot be null or empty.");
        }
        if (email == null || email.isEmpty() || !email.contains("@")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid email format.");
        }
        if (password == null || password.length() < 8) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Password must be at least 8 characters long.");
        }
        if (phoneNumber == null || !phoneNumber.matches("^\\d{3}-\\d{3}-\\d{4}$")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Phone number must be in the format xxx-xxx-xxxx.");
        }

        Employee employee = new Employee(name, username, email, password, phoneNumber, isActive);
        employeeRepository.save(employee);
    }


 // Add a new employee account -- Permission req (Only Owner)
 @Transactional
 public void updateEmployee(String name, String username, String email, String password,
         String phoneNumber, boolean isActive) {

     // Validate fields
     if (name == null || name.isEmpty()) {
         throw new IllegalArgumentException("Name cannot be null or empty.");
     }
     if (username == null || username.isEmpty()) {
         throw new IllegalArgumentException("Username cannot be null or empty.");
     }
     if (email == null || email.isEmpty() || !email.contains("@")) {
         throw new IllegalArgumentException("Invalid email format.");
     }
     if (password == null || password.length() < 8) {
         throw new IllegalArgumentException("Password must be at least 8 characters long.");
     }
     if (phoneNumber == null || !phoneNumber.matches("^\\d{3}-\\d{3}-\\d{4}$")) {
         throw new IllegalArgumentException("Phone number must be in the format xxx-xxx-xxxx.");
     }

     Employee employee = employeeRepository.findByUsername(username);
     if (employee != null)
     {
        employee.setName(name);
        employee.setEmail(email);
        employee.setPassword(password);
        employee.setPhoneNumber(phoneNumber);
     }
     employeeRepository.save(employee);
 }


    // Retrieve employee by username
    public Employee getEmployeeByUsername(String username){
        Employee employee = employeeRepository.findByUsername(username);
        if (employee == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee with username " + username + " not found.");
        }
        return employee;
    }

    // Archive an employee account -- Permission req (Only Owner)
    @Transactional
    public void archiveEmployeeAccount(String employee_username) {
        Employee employee = employeeRepository.findByUsername(employee_username);
        if (employee != null) {
            employee.setIsActive(false);
            employeeRepository.save(employee);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee with username " + employee_username + " not found.");
        }
    }
}