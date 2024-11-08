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

    // Should I verify every attribute or do we assume that it's done on the model
    // side or on the REACT side ?

    // Add a new game -- Permission req (Only Owner)
    @Transactional
    public Game addGame(String owner_username, String title, String description, String img, int stock, double price,
                    String parentalRating, Game.VisibilityStatus status, int categoryId) {
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
    if (!gameCategoryRepository.findById(categoryId).isPresent()) {
        throw new IllegalArgumentException("Invalid category ID.");
    }

    // Create and save the game with the provided status
    Game game = new Game(title, description, img, stock, price, parentalRating, status,
                         gameCategoryRepository.findById(categoryId).get());
    gameRepository.save(game);

    return game; // Return the created game
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

    // Update an existing game
    @Transactional
    public Game updateGame(int gameId, String title, String description, String img, int stock, double price,
            String parentalRating, Game.VisibilityStatus status, int categoryId, String username) {
        // Check if the user has permission to update a game
        if (!accountService.hasPermission(username, 2)) {
            throw new IllegalArgumentException("User does not have permission to update a game.");
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
        if (!gameCategoryRepository.findById(categoryId).isPresent()) {
            throw new IllegalArgumentException("Invalid category ID.");
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
            throw new IllegalArgumentException("Game with ID " + gameId + " not found.");
        }

        return game; 
    }

    // ************************** CATEGORY MANAGEMENT **************************

    // Add a new category -- Permission req (Only Owner)
    @Transactional
    public GameCategory addCategory(String owner_username, String name, String description) {
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
        
        return category;
    }

    // Archive an existing category -- Permission req (Only Owner)
    @Transactional
    public void archiveCategory(int categoryId, String username){
        if (accountService.hasPermission(username, 2)) {
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
                throw new IllegalArgumentException("Category with ID " + categoryId + " not found.");
            }
        } else {
            throw new IllegalArgumentException("User does not have permission to archive a category.");
        }
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
            //idicate no game category was found
        }
        return gameCategory;
    }

    // Retrieve all visible categories
    public List<GameCategory> getAllPendingArchiveCategories(){
        List<GameCategory.VisibilityStatus> pendingArchive = List.of(GameCategory.VisibilityStatus.PendingArchive);
        return (List<GameCategory>) gameCategoryRepository.findByStatusIn(pendingArchive);
    }

    // ************************** PROMOTION MANAGEMENT **************************

    // Add a new promotion -- Permission req (Only Owner)
    @Transactional
    public Promotion addPromotion(String owner_username, int percentage, Date startDate, Date endDate, String title,
            String description) {
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
        return promotionRepository.save(promotion);
    }

    // Update an existing promotion
    @Transactional
    public Promotion updatePromotion(int promotionID, String owner_username, int percentage, Date startDate, Date endDate,
            String title, String description) {
        // Check if the user has permission to update a game
        if (!accountService.hasPermission(owner_username, 3)) {
            throw new IllegalArgumentException("User does not have permission to update promotions.");
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
            throw new IllegalArgumentException("Review with ID " + promotionID + " not found.");
        }

        return updatedPromotion;
    }

    // Update an existing promotion
    @Transactional
    public void deletePromotion(int promotionId){
        //Look for the promotion in the promotion repo
        Promotion promotion = promotionRepository.findByPromotionID(promotionId);

        //Make sure a promotion was found. Throw an error otherwise
        if(promotion == null) { 
            throw new IllegalArgumentException("Promotion with ID " + promotionId + " not found.");
        }

        promotionRepository.delete(promotion);
    }

    // Retrieve all promotions
    public List<Promotion> getAllPromotions() {
        return (List<Promotion>) promotionRepository.findAll();
    }

    // Retrieve all promotions
    public List<Promotion> getAllGamePromotions(int gameID) {
        return (List<Promotion>) promotionRepository.findByGame_GameID(gameID);
    }

    // Retrieve a promotion by promotionID
    public Promotion getPromotion(int promotionID) {
        return promotionRepository.findByPromotionID(promotionID);
    }

    // ************************** DASHBOARD FUNCTIONALITY **************************

    // Retrieve all employees
    public List<Employee> getAllEmployees() {
        return (List<Employee>) employeeRepository.findAll();
    }

    // Retrieve game archive requests
    public List<Game> getGameArchiveRequests() {
        List<Game.VisibilityStatus> pendingArchive = List.of(Game.VisibilityStatus.PendingArchive);
        return (List<Game>) gameRepository.findByStatusIn(pendingArchive);
    }

    // ************************** GAME ARCHIVING **************************

    // Request a game to be archived (for employees) -- Permission req
    @Transactional
    public void archiveGameRequest(int gameId, String username) {
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

    // Approve an archive request-- Permission req (Only Owner)
    @Transactional
    public void approveArchiveRequest(int requestId, String username) {
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

    // ************************** EMPLOYEE ACCOUNT MANAGEMENT
    // **************************

    // Add a new employee account -- Permission req (Only Owner)
    @Transactional
    public void addEmployee(String owner_username, String name, String username, String email, String password,
            String phoneNumber, boolean isActive) {
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
    // @Transactional
    // void evaluateEmployee(Long employeeId);

    // Archive an employee account -- Permission req (Only Owner)
    @Transactional
    public void archiveEmployeeAccount(String employee_username, String username) {
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