package group_13.game_store.service;

import java.util.List;
import java.sql.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import group_13.game_store.model.Game;
import group_13.game_store.model.GameCategory;
import group_13.game_store.model.Promotion;
import group_13.game_store.model.Employee;

import group_13.game_store.repository.EmployeeRepository;
import group_13.game_store.repository.GameCategoryRepository;
import group_13.game_store.repository.GameRepository;
import group_13.game_store.repository.PromotionRepository;


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
    private AccountService accountService;


    // ************************** GAME MANAGEMENT **************************


    //Should I verify every attribute or do we assume that it's done on the model side or on the REACT side ? 

    // Add a new game -- Permission req (Only Owner)
    @Transactional
    public void addGame(String owner_username, String title, String description, String img, int stock, double price, String parentalRating, GameCategory category) {
        // Check if the user has permission to add a game
        if (!accountService.hasPermission(owner_username, 2)) {
            throw new IllegalArgumentException("User does not have permission to add a game.");
        }
    
        // Validate fields
        if (title == null || title.isEmpty()) {
            throw new IllegalArgumentException("Title cannot be null or empty.");
        }
        if (description == null || description.isEmpty()) {
            throw new IllegalArgumentException("Description cannot be null or empty.");
        }
        if (img == null || img.isEmpty()) {
            throw new IllegalArgumentException("Image URL cannot be null or empty.");
        }
        if (stock < 0) {
            throw new IllegalArgumentException("Stock cannot be negative.");
        }
        if (price <= 0) {
            throw new IllegalArgumentException("Price must be greater than zero.");
        }
        if (parentalRating == null || parentalRating.isEmpty()) {
            throw new IllegalArgumentException("Parental rating cannot be null or empty.");
        }
        if (category == null) {
            throw new IllegalArgumentException("Category cannot be null.");
        }
    
        Game game = new Game(title, description, img, stock, price, parentalRating, Game.VisibilityStatus.Visible, category);
        gameRepository.save(game);
    }

    
    // Archive an existing game
    @Transactional
    public void archiveGame(int gameId, String username) {
        // Set status of the game to Archived
        // 
        if (accountService.hasPermission(username, 2)) {
            Game game = gameRepository.findByGameID(gameId);
            if (game != null) {
                game.setStatus(Game.VisibilityStatus.Archived);
                gameRepository.save(game);
            } else {
                throw new IllegalArgumentException("Game with ID " + gameId + " not found.");
            }
        } else {
            throw new IllegalArgumentException("User does not have permission to archive a game.");
        }
    }
     
    
    // Retrieve all games
    public List<Game> getAllGames() {
        return (List<Game>) gameRepository.findAll();
    }


    // ************************** CATEGORY MANAGEMENT **************************
    
    // Add a new category -- Permission req (Only Owner)
    @Transactional
    public void addCategory(String owner_username, String name, String description) {
        // Check if the user has permission to add a category
        if (!accountService.hasPermission(owner_username, 2)) {
            throw new IllegalArgumentException("User does not have permission to add a category.");
        }
    
        // Validate fields
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Category name cannot be null or empty.");
        }
        if (description == null || description.isEmpty()) {
            throw new IllegalArgumentException("Category description cannot be null or empty.");
        }
    
        GameCategory category = new GameCategory(description, GameCategory.VisibilityStatus.Visible, name);
        gameCategoryRepository.save(category);
    }

    // Archive an existing category -- Permission req (Only Owner)
    @Transactional
    void archiveCategory(int categoryId, String username){
        if (accountService.hasPermission(username, 2)) {
            GameCategory category = gameCategoryRepository.findByCategoryID(categoryId);
            if (category != null) {
                category.setStatus(GameCategory.VisibilityStatus.Archived);
                gameCategoryRepository.save(category);
            } else {
                throw new IllegalArgumentException("Category with ID " + categoryId + " not found.");
            }
        } else {    
            throw new IllegalArgumentException("User does not have permission to archive a category.");
        }
    }

    // Retrieve all categories
    List<GameCategory> getAllCategories(){
        return (List<GameCategory>) gameCategoryRepository.findAll();
    }

    // ************************** PROMOTION MANAGEMENT **************************

    // Add a new promotion -- Permission req (Only Owner)
    @Transactional
    public void addPromotion(String owner_username, int percentage, Date startDate, Date endDate, String title, String description) {
        // Check if the user has permission to add a promotion
        if (!accountService.hasPermission(owner_username, 3)) {
            throw new IllegalArgumentException("User does not have permission to add a promotion.");
        }
    
        // Validate fields
        if (percentage <= 0 || percentage > 100) {
            throw new IllegalArgumentException("Percentage must be between 1 and 100.");
        }
        if (title == null || title.isEmpty()) {
            throw new IllegalArgumentException("Title cannot be null or empty.");
        }
        if (description == null || description.isEmpty()) {
            throw new IllegalArgumentException("Description cannot be null or empty.");
        }
        if (startDate == null || endDate == null) {
            throw new IllegalArgumentException("Start and end dates cannot be null.");
        }
        if (endDate.before(startDate)) {
            throw new IllegalArgumentException("End date cannot be before start date.");
        }
    
        Promotion promotion = new Promotion(percentage, startDate, endDate, title, description);
        promotionRepository.save(promotion);
    }

    // Retrieve all promotions
    public List<Promotion> getAllPromotions(){
        return (List<Promotion>) promotionRepository.findAll();
    }

    // Retrieve all promotions
    public List<Promotion> getAllGamePromotions(int gameID){
        return (List<Promotion>) promotionRepository.findByGame_GameID(gameID);
    }

    // ************************** DASHBOARD FUNCTIONALITY **************************

    // Retrieve all employees
    List<Employee> getAllEmployees(){
        return (List<Employee>) employeeRepository.findAll();
    }
    
    // Retrieve game archive requests
    List<Game> getGameArchiveRequests(){
        List<Game.VisibilityStatus> pendingArchive = List.of(Game.VisibilityStatus.PendingArchive);
        return (List<Game>) gameRepository.findByStatusIn(pendingArchive);
    }

    // ************************** GAME ARCHIVING **************************
    
    // Request a game to be archived (for employees) -- Permission req
    @Transactional
    void archiveGameRequest(int gameId, String username){
        if (accountService.hasPermission(username, 1)) {
            Game game = gameRepository.findByGameID(gameId);
            if (game != null) {
                game.setStatus(Game.VisibilityStatus.PendingArchive);
                gameRepository.save(game);
            } else {
                throw new IllegalArgumentException("Game with ID " + gameId + " not found.");
            }
        } else {
            throw new IllegalArgumentException("User does not have permission to archive a game.");
        }
    }

    // Approve an archive request-- Permission req  (Only Owner)
    @Transactional
    void approveArchiveRequest(int requestId, String username){
        if (accountService.hasPermission(username, 2)) {
            Game game = gameRepository.findByGameID(requestId);
            if (game != null) {
                game.setStatus(Game.VisibilityStatus.Archived);
                gameRepository.save(game);
            } else {
                throw new IllegalArgumentException("Game with ID " + requestId + " not found.");
            }
        } else {
            throw new IllegalArgumentException("User does not have permission to approve an archive request.");
        }
    }

    // ************************** EMPLOYEE ACCOUNT MANAGEMENT **************************

    // Add a new employee account -- Permission req (Only Owner)
    @Transactional
    public void addEmployee(String owner_username, String name, String username, String email, String password, String phoneNumber, boolean isActive) {
        // Check if the user has permission to add an employee
        if (!accountService.hasPermission(owner_username, 2)) {
            throw new IllegalArgumentException("User does not have permission to add an employee.");
        }
    
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

        Employee employee = new Employee(name, username, email, password, phoneNumber, isActive);
        employeeRepository.save(employee);
    }

    // Evaluate an employee account - DO WE DO THIS ????
    //@Transactional
    //void evaluateEmployee(Long employeeId);

    // Archive an employee account -- Permission req  (Only Owner)
    @Transactional
    void archiveEmployeeAccount(String employee_username, String username){
        if (accountService.hasPermission(username, 2)) {
            Employee employee = employeeRepository.findByUsername(employee_username);
            if (employee != null) {
                employee.setIsActive(false);
                employeeRepository.save(employee);
            } else {
                throw new IllegalArgumentException("Employee with ID " + employee_username + " not found.");
            }
        } else {
            throw new IllegalArgumentException("User does not have permission to archive an employee account.");
        }
    }
}