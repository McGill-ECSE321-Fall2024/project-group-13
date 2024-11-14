package group_13.game_store.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import group_13.game_store.model.Game;
import group_13.game_store.model.GameCategory;
import group_13.game_store.model.Promotion;
import group_13.game_store.model.Customer;
import group_13.game_store.model.Employee;
import group_13.game_store.repository.GameCategoryRepository;
import group_13.game_store.repository.GameRepository;
import group_13.game_store.repository.PromotionRepository;
import group_13.game_store.repository.EmployeeRepository;
import group_13.game_store.repository.CustomerRepository;

@SpringBootTest
public class GameStoreManagementTest {

    @Mock
    private GameRepository gameRepository;

    @Mock
    private GameCategoryRepository gameCategoryRepository;

    @Mock
    private PromotionRepository promotionRepository;

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private AccountService accountService;

    @InjectMocks
    private GameStoreManagementService gameStoreManagementService;

    private GameCategory category1;
    private Game game1;
    private Game game2;
    private Promotion promotion1;
    private Date startDate;
    private Date endDate;
    
    private LocalDate today = LocalDate.now();
    private LocalDate tomorrow = today.plusDays(1);


    @BeforeEach
    public void setup() {
        // Setup sample data
        category1 = new GameCategory("CategoryDescription1", GameCategory.VisibilityStatus.Visible, "CategoryName1");
        category1.setCategoryID(1);

        game1 = new Game("Game1", "Description1", "img1", 10, 10.0, "PG", Game.VisibilityStatus.Visible, category1);
        game1.setGameID(1);

        game2 = new Game("Game2", "Description2", "img2", 20, 20.0, "PG", Game.VisibilityStatus.PendingArchive, category1);
        game2.setGameID(2);

        startDate = Date.valueOf(today);
        endDate = Date.valueOf(tomorrow);

        promotion1 = new Promotion(10, startDate, endDate, "Holiday Sale", "10% off for the holidays");
        promotion1.setPromotionID(1);
    }

    // ************************** GAME MANAGEMENT TESTS **************************

    @Test
    public void testAddGameValid() {
        // Arrange
        when(gameCategoryRepository.findById(1)).thenReturn(Optional.of(category1));

        // Act
        Game result = gameStoreManagementService.addGame("Game1", "Description1", "img1", 10, 10.0, "PG", Game.VisibilityStatus.Visible, 1);

        // Assert
        assertNotNull(result);
        assertEquals("Game1", result.getTitle());
        assertEquals("Description1", result.getDescription());
        assertEquals(10.0, result.getPrice());
        verify(gameRepository, times(1)).save(result);
    }

    @Test
    public void testAddGameInvalidCategory() {
        // Arrange
        when(gameCategoryRepository.findById(999)).thenReturn(Optional.empty());

        // Act and Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> 
            gameStoreManagementService.addGame("Game1", "Description1", "img1", 10, 10.0, "PG", Game.VisibilityStatus.Visible, 999));

        // Assert Status Code and Message
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("Invalid category ID.", exception.getReason());

        // Verify that no game is saved in the repository
        verify(gameRepository, never()).save(any(Game.class));
    }

    @Test
    public void testArchiveGameValid() {
        // Arrange
        when(gameRepository.findByGameID(1)).thenReturn(game1);

        // Act
        gameStoreManagementService.archiveGame(1);

        // Assert
        assertEquals(Game.VisibilityStatus.Archived, game1.getStatus());
        verify(gameRepository, times(1)).save(game1);
    }

    @Test
    public void testArchiveGameNonexistent() {
        // Arrange
        when(gameRepository.findByGameID(999)).thenReturn(null);
    
        // Act and Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> 
            gameStoreManagementService.archiveGame(999));
        
        // Assert Status Code and Message
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("Game with ID 999 not found.", exception.getReason());
    
        // Verify that no game is saved in the repository
        verify(gameRepository, never()).save(any(Game.class));
    }

    @Test
    public void testGetAllGames() {
        // Arrange
        when(gameRepository.findAll()).thenReturn(List.of(game1, game2));

        // Act
        List<Game> result = gameStoreManagementService.getAllGames();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(gameRepository, times(1)).findAll();
    }

    // ************************** CATEGORY MANAGEMENT TESTS **************************

    @Test
    public void testAddCategoryValid() {
        // Act
        GameCategory result = gameStoreManagementService.addCategory("New Category", "Description");

        // Assert
        assertNotNull(result);
        assertEquals("New Category", result.getName());
        verify(gameCategoryRepository, times(1)).save(result);
    }

    @Test
    public void testArchiveCategoryValid() {
        // Arrange
        when(gameCategoryRepository.findByCategoryID(1)).thenReturn(category1);

        // Act
        gameStoreManagementService.archiveCategory(1, "admin");

        // Assert
        assertEquals(GameCategory.VisibilityStatus.PendingArchive, category1.getStatus());
        verify(gameCategoryRepository, times(1)).save(category1);
    }

    @Test
    public void testGetAllCategories() {
        // Arrange
        when(gameCategoryRepository.findAll()).thenReturn(List.of(category1));

        // Act
        List<GameCategory> result = gameStoreManagementService.getAllCategories();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(gameCategoryRepository, times(1)).findAll();
    }

    // ************************** PROMOTION MANAGEMENT TESTS **************************

    @Test
    public void testAddPromotionValid() {
        // Act
        Promotion result = gameStoreManagementService.addPromotion(10, startDate, endDate, "Holiday Sale", "10% off");

        // Assert
        assertNotNull(result);
        assertEquals("Holiday Sale", result.getTitle());
        assertEquals(10, result.getPercentage());
        verify(promotionRepository, times(1)).save(result);
    }

    @Test
    public void testAddPromotionInvalidDate() {
        // Act and Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> 
            gameStoreManagementService.addPromotion(10, endDate, startDate, "Holiday Sale", "10% off"));
    
        // Assert Status Code and Message
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("End date cannot be before start date.", exception.getReason());
    
        // Verify that no promotion is saved in the repository
        verify(promotionRepository, never()).save(any(Promotion.class));
    }

    @Test
    public void testDeletePromotionValid() {
        // Arrange
        when(promotionRepository.findByPromotionID(1)).thenReturn(promotion1);

        // Act
        gameStoreManagementService.deletePromotion(1);

        // Assert
        verify(promotionRepository, times(1)).delete(promotion1);
    }

    @Test
    public void testDeletePromotionNonexistent() {
        // Arrange
        when(promotionRepository.findByPromotionID(999)).thenReturn(null);
    
        // Act and Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> 
            gameStoreManagementService.deletePromotion(999));
    
        // Assert Status Code and Message
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("Promotion with ID 999 not found.", exception.getReason());
    
        // Verify that no promotion is deleted in the repository
        verify(promotionRepository, never()).delete(any(Promotion.class));
    }

    // ************************** DASHBOARD FUNCTIONALITY TESTS **************************

    @Test
    public void testGetAllEmployees() {
        // Act
        List<Employee> result = gameStoreManagementService.getAllEmployees();

        // Assert
        assertNotNull(result);
        verify(employeeRepository, times(1)).findAll();
    }

    @Test
    public void testGetGameArchiveRequests() {
        // Arrange
        when(gameRepository.findByStatusIn(List.of(Game.VisibilityStatus.PendingArchive)))
                .thenReturn(List.of(game2));

        // Act
        List<Game> result = gameStoreManagementService.getGameArchiveRequests();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(Game.VisibilityStatus.PendingArchive, result.get(0).getStatus());
        verify(gameRepository, times(1)).findByStatusIn(any());
    }

    // ************************** CATEGORY MANAGEMENT TESTS **************************

    @Test
    public void testGetCategoryById() {
        // Arrange
        when(gameCategoryRepository.findByCategoryID(1)).thenReturn(category1);

        // Act
        GameCategory result = gameStoreManagementService.getCategoryById(1);

        // Assert
        assertNotNull(result);
        assertEquals("CategoryName1", result.getName());
        verify(gameCategoryRepository, times(1)).findByCategoryID(1);
    }

    // ************************** EMPLOYEE MANAGEMENT TESTS **************************

    @Test
    public void testGetEmployeeByUsername() {
        // Arrange
        Employee employee = new Employee("John Doe", "johndoe", "johndoe@example.com", "password", "123-456-7890", true);
        when(employeeRepository.findByUsername("johndoe")).thenReturn(employee);

        // Act
        Employee result = gameStoreManagementService.getEmployeeByUsername("johndoe");

        // Assert
        assertNotNull(result);
        assertEquals("johndoe", result.getUsername());
        verify(employeeRepository, times(1)).findByUsername("johndoe");
    }

    // ************************** CATEGORY STATUS MANAGEMENT TESTS **************************

    @Test
    public void testGetAllPendingArchiveCategories() {
        // Arrange
        category1.setStatus(GameCategory.VisibilityStatus.PendingArchive);
        when(gameCategoryRepository.findByStatusIn(List.of(GameCategory.VisibilityStatus.PendingArchive)))
                .thenReturn(List.of(category1));
    
        // Act
        List<GameCategory> result = gameStoreManagementService.getAllPendingArchiveCategories();
    
        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(GameCategory.VisibilityStatus.PendingArchive, result.get(0).getStatus());
        verify(gameCategoryRepository, times(1)).findByStatusIn(any());
    }

    // ************************** PROMOTION MANAGEMENT TESTS **************************

    @Test
    public void testGetAllPromotions() {
        // Arrange
        when(promotionRepository.findAll()).thenReturn(List.of(promotion1));

        // Act
        List<Promotion> result = gameStoreManagementService.getAllPromotions();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Holiday Sale", result.get(0).getTitle());
        verify(promotionRepository, times(1)).findAll();
    }

    @Test
    public void testGetAllGamePromotions() {
        // Arrange
        when(promotionRepository.findByGame_GameID(1)).thenReturn(List.of(promotion1));

        // Act
        List<Promotion> result = gameStoreManagementService.getAllGamePromotions(1);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Holiday Sale", result.get(0).getTitle());
        verify(promotionRepository, times(1)).findByGame_GameID(1);
    }

    @Test
    public void testGetPromotion() {
        // Arrange
        when(promotionRepository.findByPromotionID(1)).thenReturn(promotion1);

        // Act
        Promotion result = gameStoreManagementService.getPromotion(1);

        // Assert
        assertNotNull(result);
        assertEquals("Holiday Sale", result.getTitle());
        verify(promotionRepository, times(1)).findByPromotionID(1);
    }

    // ************************** CUSTOMER MANAGEMENT TESTS **************************

    @Test
    public void testGetAllCustomers() {
        // Arrange
        Customer customer1 = new Customer("Nico", "user1", "email@1.ca", "password1", "514-311-2222");
        Customer customer2 = new Customer("Garrett", "User2", "email@2.ca", "password2", "432-221-2222");
        when(customerRepository.findAll()).thenReturn(List.of(customer1, customer2));

        // Act
        List<Customer> result = gameStoreManagementService.getAllCustomers();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("user1", result.get(0).getUsername());
        verify(customerRepository, times(1)).findAll();
    }

    // ************************** GAME ARCHIVING TESTS **************************

    @Test
    public void testArchiveGameRequestValid() {
        // Arrange
        when(gameRepository.findByGameID(1)).thenReturn(game1);

        // Act
        gameStoreManagementService.archiveGameRequest(1);

        // Assert
        assertEquals(Game.VisibilityStatus.PendingArchive, game1.getStatus());
        verify(gameRepository, times(1)).save(game1);
    }

    @Test
    public void testArchiveGameRequestNonexistent() {
        // Arrange
        when(gameRepository.findByGameID(999)).thenReturn(null);
    
        // Act and Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> 
            gameStoreManagementService.archiveGameRequest(999));
    
        // Assert Status Code and Message
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("Game with ID 999 not found.", exception.getReason());
    
        // Verify that no game is saved in the repository
        verify(gameRepository, never()).save(any(Game.class));
    }

    // ************************** EMPLOYEE ACCOUNT ARCHIVING TESTS **************************

    @Test
    public void testArchiveEmployeeAccountValid() {
        // Arrange
        Employee employee = new Employee("John Doe", "johndoe", "johndoe@example.com", "password", "123-456-7890", true);
        when(employeeRepository.findByUsername("johndoe")).thenReturn(employee);
    
        // Act
        gameStoreManagementService.archiveEmployeeAccount("johndoe");
    
        // Assert
        assertFalse(employee.getIsActive());
        verify(employeeRepository, times(1)).save(employee);
    }
    
    @Test
    public void testArchiveEmployeeAccountNonexistent() {
        // Arrange
        when(employeeRepository.findByUsername("nonexistent_user")).thenReturn(null);
    
        // Act and Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> 
            gameStoreManagementService.archiveEmployeeAccount("nonexistent_user"));
    
        // Assert Status Code and Message
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("Employee with username nonexistent_user not found.", exception.getReason());
    
        // Verify that no employee is saved in the repository
        verify(employeeRepository, never()).save(any(Employee.class));
    }
    
    @Test
    public void testUpdateGameValid() {
        // Arrange
        when(gameRepository.findByGameID(1)).thenReturn(game1);
        when(gameCategoryRepository.findById(1)).thenReturn(Optional.of(category1));
    
        // Act
        Game updatedGame = gameStoreManagementService.updateGame(1, "Updated Title", "Updated Description", "updated_img",
            15, 12.0, "PG-13", Game.VisibilityStatus.Visible, 1);
    
        // Assert
        assertNotNull(updatedGame);
        assertEquals("Updated Title", updatedGame.getTitle());
        assertEquals("Updated Description", updatedGame.getDescription());
        assertEquals("updated_img", updatedGame.getImg());
        assertEquals(15, updatedGame.getStock());
        assertEquals(12.0, updatedGame.getPrice());
        assertEquals("PG-13", updatedGame.getParentalRating());
        assertEquals(Game.VisibilityStatus.Visible, updatedGame.getStatus());
        assertEquals(category1, updatedGame.getCategory());
        verify(gameRepository, times(1)).save(updatedGame);
    }
    
    @Test
    public void testUpdateGameNonexistentGame() {
        // Arrange
        when(gameRepository.findByGameID(999)).thenReturn(null);
        when(gameCategoryRepository.findById(1)).thenReturn(Optional.of(new GameCategory("Category", GameCategory.VisibilityStatus.Visible, "CategoryName")));
    
        // Act and Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> 
            gameStoreManagementService.updateGame(999, "Title", "Description", "img", 10, 10.0, "PG", 
                Game.VisibilityStatus.Visible, 1));  // Use a valid category ID here
    
        // Assert Status Code and Message
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("Game with ID 999 not found.", exception.getReason());
    
        // Verify that no game is saved in the repository
        verify(gameRepository, never()).save(any(Game.class));
    }
    
    @Test
    public void testUpdateGameInvalidCategory() {
        // Arrange
        when(gameRepository.findByGameID(1)).thenReturn(game1);
        when(gameCategoryRepository.findById(999)).thenReturn(Optional.empty());
    
        // Act and Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> 
            gameStoreManagementService.updateGame(1, "Title", "Description", "img", 10, 10.0, "PG", 
                Game.VisibilityStatus.Visible, 999));
    
        // Assert Status Code and Message
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("Invalid category ID.", exception.getReason());
        verify(gameRepository, never()).save(any(Game.class));
    }
    
    @Test
    public void testUpdateGameNegativeStock() {
        // Arrange
        when(gameRepository.findByGameID(1)).thenReturn(game1);
        when(gameCategoryRepository.findById(1)).thenReturn(Optional.of(category1));
    
        // Act and Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> 
            gameStoreManagementService.updateGame(1, "Title", "Description", "img", -1, 10.0, "PG", 
                Game.VisibilityStatus.Visible, 1));
    
        // Assert Status Code and Message
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("Stock cannot be negative.", exception.getReason());
        verify(gameRepository, never()).save(any(Game.class));
    }
    
    @Test
    public void testUpdateGameZeroOrNegativePrice() {
        // Arrange
        when(gameRepository.findByGameID(1)).thenReturn(game1);
        when(gameCategoryRepository.findById(1)).thenReturn(Optional.of(category1));
    
        // Act and Assert for zero price
        ResponseStatusException zeroPriceException = assertThrows(ResponseStatusException.class, () -> 
            gameStoreManagementService.updateGame(1, "Title", "Description", "img", 10, 0.0, "PG", 
                Game.VisibilityStatus.Visible, 1));
        
        // Assert Status Code and Message for zero price
        assertEquals(HttpStatus.BAD_REQUEST, zeroPriceException.getStatusCode());
        assertEquals("Price must be greater than zero.", zeroPriceException.getReason());
        verify(gameRepository, never()).save(any(Game.class));
    
        // Act and Assert for negative price
        ResponseStatusException negativePriceException = assertThrows(ResponseStatusException.class, () -> 
            gameStoreManagementService.updateGame(1, "Title", "Description", "img", 10, -5.0, "PG", 
                Game.VisibilityStatus.Visible, 1));
    
        // Assert Status Code and Message for negative price
        assertEquals(HttpStatus.BAD_REQUEST, negativePriceException.getStatusCode());
        assertEquals("Price must be greater than zero.", negativePriceException.getReason());
        verify(gameRepository, never()).save(any(Game.class));
    }

    @Test
    public void testUpdateGameNullTitle() {
        // Arrange
        when(gameRepository.findByGameID(1)).thenReturn(game1);
        when(gameCategoryRepository.findById(1)).thenReturn(Optional.of(category1));
    
        // Act and Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> 
            gameStoreManagementService.updateGame(1, null, "Description", "img", 10, 10.0, "PG", 
                Game.VisibilityStatus.Visible, 1));
    
        // Assert Status Code and Message
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("Title cannot be null or empty.", exception.getReason());
        verify(gameRepository, never()).save(any(Game.class));
    }
    
    @Test
    public void testUpdateGameEmptyTitle() {
        // Arrange
        when(gameRepository.findByGameID(1)).thenReturn(game1);
        when(gameCategoryRepository.findById(1)).thenReturn(Optional.of(category1));
    
        // Act and Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> 
            gameStoreManagementService.updateGame(1, "", "Description", "img", 10, 10.0, "PG", 
                Game.VisibilityStatus.Visible, 1));
    
        // Assert Status Code and Message
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("Title cannot be null or empty.", exception.getReason());
        verify(gameRepository, never()).save(any(Game.class));
    }


    @Test
    public void testUpdatePromotionValid() {
        // Arrange
        when(promotionRepository.findByPromotionID(1)).thenReturn(promotion1);
        
        Date newStartDate = Date.valueOf(today.plusDays(1));
        Date newEndDate = Date.valueOf(today.plusDays(10));
    
        // Act
        Promotion updatedPromotion = gameStoreManagementService.updatePromotion(1, 20, newStartDate, newEndDate, "New Title", "New Description");
    
        // Assert
        assertNotNull(updatedPromotion);
        assertEquals(20, updatedPromotion.getPercentage());
        assertEquals(newStartDate, updatedPromotion.getStartDate());
        assertEquals(newEndDate, updatedPromotion.getEndDate());
        assertEquals("New Title", updatedPromotion.getTitle());
        assertEquals("New Description", updatedPromotion.getDescription());
        verify(promotionRepository, times(1)).save(updatedPromotion);
    }
    
    @Test
    public void testUpdatePromotionNonexistentPromotion() {
        // Arrange
        when(promotionRepository.findByPromotionID(999)).thenReturn(null);
    
        // Act and Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> 
            gameStoreManagementService.updatePromotion(999, 10, startDate, endDate, "Title", "Description"));
    
        // Assert Status Code and Message
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("Promotion with ID 999 not found.", exception.getReason());
        verify(promotionRepository, never()).save(any(Promotion.class));
    }
    
    @Test
    public void testUpdatePromotionInvalidPercentage() {
        // Arrange
        when(promotionRepository.findByPromotionID(1)).thenReturn(promotion1);
    
        // Act and Assert for zero percentage
        ResponseStatusException zeroPercentageException = assertThrows(ResponseStatusException.class, () -> 
            gameStoreManagementService.updatePromotion(1, 0, startDate, endDate, "Title", "Description"));
    
        // Assert Status Code and Message for zero percentage
        assertEquals(HttpStatus.BAD_REQUEST, zeroPercentageException.getStatusCode());
        assertEquals("Percentage must be between 1 and 100.", zeroPercentageException.getReason());
        verify(promotionRepository, never()).save(any(Promotion.class));
    
        // Act and Assert for percentage greater than 100
        ResponseStatusException overPercentageException = assertThrows(ResponseStatusException.class, () -> 
            gameStoreManagementService.updatePromotion(1, 150, startDate, endDate, "Title", "Description"));
    
        // Assert Status Code and Message for percentage greater than 100
        assertEquals(HttpStatus.BAD_REQUEST, overPercentageException.getStatusCode());
        assertEquals("Percentage must be between 1 and 100.", overPercentageException.getReason());
        verify(promotionRepository, never()).save(any(Promotion.class));
    }
    
    @Test
    public void testUpdatePromotionNullTitle() {
        // Arrange
        when(promotionRepository.findByPromotionID(1)).thenReturn(promotion1);
    
        // Act and Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> 
            gameStoreManagementService.updatePromotion(1, 10, startDate, endDate, null, "Description"));
    
        // Assert Status Code and Message
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("Title cannot be null or empty.", exception.getReason());
        verify(promotionRepository, never()).save(any(Promotion.class));
    }
    
    @Test
    public void testUpdatePromotionEmptyTitle() {
        // Arrange
        when(promotionRepository.findByPromotionID(1)).thenReturn(promotion1);
    
        // Act and Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> 
            gameStoreManagementService.updatePromotion(1, 10, startDate, endDate, "", "Description"));
    
        // Assert Status Code and Message
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("Title cannot be null or empty.", exception.getReason());
        verify(promotionRepository, never()).save(any(Promotion.class));
    }
    
    @Test
    public void testUpdatePromotionInvalidDates() {
        // Arrange
        when(promotionRepository.findByPromotionID(1)).thenReturn(promotion1);
    
        Date invalidStartDate = Date.valueOf(today.plusDays(10));
        Date invalidEndDate = Date.valueOf(today);
    
        // Act and Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> 
            gameStoreManagementService.updatePromotion(1, 10, invalidStartDate, invalidEndDate, "Title", "Description"));
    
        // Assert Status Code and Message
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("End date cannot be before start date.", exception.getReason());
        verify(promotionRepository, never()).save(any(Promotion.class));
    }

    @Test
    public void testAddEmployeeValid() {
        // Act
        gameStoreManagementService.addEmployee("John Doe", "johndoe", "johndoe@example.com", "password123", "123-456-7890", true);

        // Assert
        ArgumentCaptor<Employee> captor = ArgumentCaptor.forClass(Employee.class);
        verify(employeeRepository, times(1)).save(captor.capture());
        Employee savedEmployee = captor.getValue();

        assertEquals("John Doe", savedEmployee.getName());
        assertEquals("johndoe", savedEmployee.getUsername());
        assertEquals("johndoe@example.com", savedEmployee.getEmail());
        assertEquals("password123", savedEmployee.getPassword());
        assertEquals("123-456-7890", savedEmployee.getPhoneNumber());
        assertTrue(savedEmployee.getIsActive());
    }
    
    @Test
    public void testAddEmployeeNullName() {
        // Act and Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> 
            gameStoreManagementService.addEmployee(null, "johndoe", "johndoe@example.com", "password123", "123-456-7890", true));
    
        // Assert Status Code and Message
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("Name cannot be null or empty.", exception.getReason());
        verify(employeeRepository, never()).save(any(Employee.class));
    }
    
    @Test
    public void testAddEmployeeEmptyName() {
        // Act and Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> 
            gameStoreManagementService.addEmployee("", "johndoe", "johndoe@example.com", "password123", "123-456-7890", true));
    
        // Assert Status Code and Message
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("Name cannot be null or empty.", exception.getReason());
        verify(employeeRepository, never()).save(any(Employee.class));
    }
    
    @Test
    public void testAddEmployeeNullUsername() {
        // Act and Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> 
            gameStoreManagementService.addEmployee("John Doe", null, "johndoe@example.com", "password123", "123-456-7890", true));
    
        // Assert Status Code and Message
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("Username cannot be null or empty.", exception.getReason());
        verify(employeeRepository, never()).save(any(Employee.class));
    }
    
    @Test
    public void testAddEmployeeEmptyUsername() {
        // Act and Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> 
            gameStoreManagementService.addEmployee("John Doe", "", "johndoe@example.com", "password123", "123-456-7890", true));
    
        // Assert Status Code and Message
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("Username cannot be null or empty.", exception.getReason());
        verify(employeeRepository, never()).save(any(Employee.class));
    }
    
    @Test
    public void testAddEmployeeInvalidEmail() {
        // Act and Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> 
            gameStoreManagementService.addEmployee("John Doe", "johndoe", "invalid-email", "password123", "123-456-7890", true));
    
        // Assert Status Code and Message
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("Invalid email format.", exception.getReason());
        verify(employeeRepository, never()).save(any(Employee.class));
    }
    
    @Test
    public void testAddEmployeeShortPassword() {
        // Act and Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> 
            gameStoreManagementService.addEmployee("John Doe", "johndoe", "johndoe@example.com", "short", "123-456-7890", true));
    
        // Assert Status Code and Message
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("Password must be at least 8 characters long.", exception.getReason());
        verify(employeeRepository, never()).save(any(Employee.class));
    }
    
    @Test
    public void testAddEmployeeInvalidPhoneNumber() {
        // Act and Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> 
            gameStoreManagementService.addEmployee("John Doe", "johndoe", "johndoe@example.com", "password123", "invalid-phone", true));
    
        // Assert Status Code and Message
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("Phone number must be in the format xxx-xxx-xxxx.", exception.getReason());
        verify(employeeRepository, never()).save(any(Employee.class));
    }
    
    @Test
    public void testAddGameNullTitle() {
        // Arrange
        when(gameCategoryRepository.findById(1)).thenReturn(Optional.of(category1));
    
        // Act and Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () ->
            gameStoreManagementService.addGame(null, "Description", "img", 10, 10.0, "PG", Game.VisibilityStatus.Visible, 1));
    
        // Assert Status Code and Message
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("Title cannot be null or empty.", exception.getReason());
        verify(gameRepository, never()).save(any(Game.class));
    }
    
    @Test
    public void testAddGameEmptyTitle() {
        // Arrange
        when(gameCategoryRepository.findById(1)).thenReturn(Optional.of(category1));
    
        // Act and Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () ->
            gameStoreManagementService.addGame("", "Description", "img", 10, 10.0, "PG", Game.VisibilityStatus.Visible, 1));
    
        // Assert Status Code and Message
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("Title cannot be null or empty.", exception.getReason());
        verify(gameRepository, never()).save(any(Game.class));
    }
    

    @Test
    public void testAddGameNullDescription() {
        // Arrange
        when(gameCategoryRepository.findById(1)).thenReturn(Optional.of(category1));
    
        // Act and Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () ->
            gameStoreManagementService.addGame("Game", null, "img", 10, 10.0, "PG", Game.VisibilityStatus.Visible, 1));
    
        // Assert Status Code and Message
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("Description cannot be null or empty.", exception.getReason());
        verify(gameRepository, never()).save(any(Game.class));
    }
    
    @Test
    public void testAddGameEmptyDescription() {
        // Arrange
        when(gameCategoryRepository.findById(1)).thenReturn(Optional.of(category1));
    
        // Act and Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () ->
            gameStoreManagementService.addGame("Game", "", "img", 10, 10.0, "PG", Game.VisibilityStatus.Visible, 1));
    
        // Assert Status Code and Message
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("Description cannot be null or empty.", exception.getReason());
        verify(gameRepository, never()).save(any(Game.class));
    }
    
    @Test
    public void testAddGameNullImageURL() {
        // Arrange
        when(gameCategoryRepository.findById(1)).thenReturn(Optional.of(category1));
    
        // Act and Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () ->
            gameStoreManagementService.addGame("Game", "Description", null, 10, 10.0, "PG", Game.VisibilityStatus.Visible, 1));
    
        // Assert Status Code and Message
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("Image URL cannot be null or empty.", exception.getReason());
        verify(gameRepository, never()).save(any(Game.class));
    }
    
    @Test
    public void testAddGameEmptyImageURL() {
        // Arrange
        when(gameCategoryRepository.findById(1)).thenReturn(Optional.of(category1));
    
        // Act and Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () ->
            gameStoreManagementService.addGame("Game", "Description", "", 10, 10.0, "PG", Game.VisibilityStatus.Visible, 1));
    
        // Assert Status Code and Message
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("Image URL cannot be null or empty.", exception.getReason());
        verify(gameRepository, never()).save(any(Game.class));
    }
    
    @Test
    public void testAddGameNegativeStock() {
        // Arrange
        when(gameCategoryRepository.findById(1)).thenReturn(Optional.of(category1));
    
        // Act and Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () ->
            gameStoreManagementService.addGame("Game", "Description", "img", -1, 10.0, "PG", Game.VisibilityStatus.Visible, 1));
    
        // Assert Status Code and Message
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("Stock cannot be negative.", exception.getReason());
        verify(gameRepository, never()).save(any(Game.class));
    }
    
    @Test
    public void testAddGameZeroPrice() {
        // Arrange
        when(gameCategoryRepository.findById(1)).thenReturn(Optional.of(category1));
    
        // Act and Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () ->
            gameStoreManagementService.addGame("Game", "Description", "img", 10, 0.0, "PG", Game.VisibilityStatus.Visible, 1));
    
        // Assert Status Code and Message
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("Price must be greater than zero.", exception.getReason());
        verify(gameRepository, never()).save(any(Game.class));
    }
    
    @Test
    public void testAddGameNegativePrice() {
        // Arrange
        when(gameCategoryRepository.findById(1)).thenReturn(Optional.of(category1));
    
        // Act and Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () ->
            gameStoreManagementService.addGame("Game", "Description", "img", 10, -5.0, "PG", Game.VisibilityStatus.Visible, 1));
    
        // Assert Status Code and Message
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("Price must be greater than zero.", exception.getReason());
        verify(gameRepository, never()).save(any(Game.class));
    }
    
    @Test
    public void testAddGameNullParentalRating() {
        // Arrange
        when(gameCategoryRepository.findById(1)).thenReturn(Optional.of(category1));
    
        // Act and Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () ->
            gameStoreManagementService.addGame("Game", "Description", "img", 10, 10.0, null, Game.VisibilityStatus.Visible, 1));
    
        // Assert Status Code and Message
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("Parental rating cannot be null or empty.", exception.getReason());
        verify(gameRepository, never()).save(any(Game.class));
    }

    @Test
    public void testAddGameEmptyParentalRating() {
        // Arrange
        when(gameCategoryRepository.findById(1)).thenReturn(Optional.of(category1));
    
        // Act and Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () ->
            gameStoreManagementService.addGame("Game", "Description", "img", 10, 10.0, "", Game.VisibilityStatus.Visible, 1));
    
        // Assert Status Code and Message
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("Parental rating cannot be null or empty.", exception.getReason());
        verify(gameRepository, never()).save(any(Game.class));
    }
    
    @Test
    public void testAddGameInvalidCategoryId() {
        // Arrange
        when(gameCategoryRepository.findById(999)).thenReturn(Optional.empty());
    
        // Act and Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () ->
            gameStoreManagementService.addGame("Game", "Description", "img", 10, 10.0, "PG", Game.VisibilityStatus.Visible, 999));
    
        // Assert Status Code and Message
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("Invalid category ID.", exception.getReason());
        verify(gameRepository, never()).save(any(Game.class));
    }
    
    @Test
    public void testAddPromotionZeroPercentage() {
        // Arrange & Act
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () ->
            gameStoreManagementService.addPromotion(0, startDate, endDate, "Title", "Description"));
    
        // Assert Status Code and Message
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("Percentage must be between 1 and 100.", exception.getReason());
        verify(promotionRepository, never()).save(any(Promotion.class));
    }
    
    @Test
    public void testAddPromotionOver100Percentage() {
        // Arrange & Act
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () ->
            gameStoreManagementService.addPromotion(150, startDate, endDate, "Title", "Description"));
    
        // Assert Status Code and Message
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("Percentage must be between 1 and 100.", exception.getReason());
        verify(promotionRepository, never()).save(any(Promotion.class));
    }
    
    @Test
    public void testAddPromotionNullTitle() {
        // Arrange & Act
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () ->
            gameStoreManagementService.addPromotion(10, startDate, endDate, null, "Description"));
    
        // Assert Status Code and Message
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("Title cannot be null or empty.", exception.getReason());
        verify(promotionRepository, never()).save(any(Promotion.class));
    }
    
    @Test
    public void testAddPromotionEmptyTitle() {
        // Arrange & Act
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () ->
            gameStoreManagementService.addPromotion(10, startDate, endDate, "", "Description"));
    
        // Assert Status Code and Message
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("Title cannot be null or empty.", exception.getReason());
        verify(promotionRepository, never()).save(any(Promotion.class));
    }
    
    @Test
    public void testAddPromotionNullDescription() {
        // Arrange & Act
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () ->
            gameStoreManagementService.addPromotion(10, startDate, endDate, "Title", null));
    
        // Assert Status Code and Message
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("Description cannot be null or empty.", exception.getReason());
        verify(promotionRepository, never()).save(any(Promotion.class));
    }
    
    @Test
    public void testAddPromotionEmptyDescription() {
        // Arrange & Act
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () ->
            gameStoreManagementService.addPromotion(10, startDate, endDate, "Title", ""));
    
        // Assert Status Code and Message
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("Description cannot be null or empty.", exception.getReason());
        verify(promotionRepository, never()).save(any(Promotion.class));
    }
    
    @Test
    public void testAddPromotionNullStartDate() {
        // Arrange & Act
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () ->
            gameStoreManagementService.addPromotion(10, null, endDate, "Title", "Description"));
    
        // Assert Status Code and Message
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("Start and end dates cannot be null.", exception.getReason());
        verify(promotionRepository, never()).save(any(Promotion.class));
    }
    
    @Test
    public void testAddPromotionNullEndDate() {
        // Arrange & Act
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () ->
            gameStoreManagementService.addPromotion(10, startDate, null, "Title", "Description"));
    
        // Assert Status Code and Message
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("Start and end dates cannot be null.", exception.getReason());
        verify(promotionRepository, never()).save(any(Promotion.class));
    }
    
    @Test
    public void testAddPromotionEndDateBeforeStartDate() {
        // Arrange
        Date invalidEndDate = Date.valueOf(today.minusDays(1));
    
        // Act and Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () ->
            gameStoreManagementService.addPromotion(10, startDate, invalidEndDate, "Title", "Description"));
    
        // Assert Status Code and Message
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("End date cannot be before start date.", exception.getReason());
        verify(promotionRepository, never()).save(any(Promotion.class));
    }
    
    @Test
    public void testUpdateGameNullDescription() {
        // Arrange
        when(gameRepository.findByGameID(1)).thenReturn(game1);
        when(gameCategoryRepository.findById(1)).thenReturn(Optional.of(category1));
    
        // Act and Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () ->
            gameStoreManagementService.updateGame(1, "Title", null, "img", 10, 10.0, "PG", 
                Game.VisibilityStatus.Visible, 1));
    
        // Assert Status Code and Message
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("Description cannot be null or empty.", exception.getReason());
        verify(gameRepository, never()).save(any(Game.class));
    }
    
    @Test
    public void testUpdateGameEmptyDescription() {
        // Arrange
        when(gameRepository.findByGameID(1)).thenReturn(game1);
        when(gameCategoryRepository.findById(1)).thenReturn(Optional.of(category1));
    
        // Act and Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () ->
            gameStoreManagementService.updateGame(1, "Title", "", "img", 10, 10.0, "PG", 
                Game.VisibilityStatus.Visible, 1));
    
        // Assert Status Code and Message
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("Description cannot be null or empty.", exception.getReason());
        verify(gameRepository, never()).save(any(Game.class));
    }
    
    @Test
    public void testUpdateGameNullImg() {
        // Arrange
        when(gameRepository.findByGameID(1)).thenReturn(game1);
        when(gameCategoryRepository.findById(1)).thenReturn(Optional.of(category1));
    
        // Act and Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () ->
            gameStoreManagementService.updateGame(1, "Title", "Description", null, 10, 10.0, "PG", 
                Game.VisibilityStatus.Visible, 1));
    
        // Assert Status Code and Message
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("Image URL cannot be null or empty.", exception.getReason());
        verify(gameRepository, never()).save(any(Game.class));
    }
    
    @Test
    public void testUpdateGameEmptyImg() {
        // Arrange
        when(gameRepository.findByGameID(1)).thenReturn(game1);
        when(gameCategoryRepository.findById(1)).thenReturn(Optional.of(category1));
    
        // Act and Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () ->
            gameStoreManagementService.updateGame(1, "Title", "Description", "", 10, 10.0, "PG", 
                Game.VisibilityStatus.Visible, 1));
    
        // Assert Status Code and Message
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("Image URL cannot be null or empty.", exception.getReason());
        verify(gameRepository, never()).save(any(Game.class));
    }
    
    @Test
    public void testUpdateGameNullParentalRating() {
        // Arrange
        when(gameRepository.findByGameID(1)).thenReturn(game1);
        when(gameCategoryRepository.findById(1)).thenReturn(Optional.of(category1));
    
        // Act and Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () ->
            gameStoreManagementService.updateGame(1, "Title", "Description", "img", 10, 10.0, null, 
                Game.VisibilityStatus.Visible, 1));
    
        // Assert Status Code and Message
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("Parental rating cannot be null or empty.", exception.getReason());
        verify(gameRepository, never()).save(any(Game.class));
    }
    
    @Test
    public void testUpdateGameEmptyParentalRating() {
        // Arrange
        when(gameRepository.findByGameID(1)).thenReturn(game1);
        when(gameCategoryRepository.findById(1)).thenReturn(Optional.of(category1));
    
        // Act and Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () ->
            gameStoreManagementService.updateGame(1, "Title", "Description", "img", 10, 10.0, "", 
                Game.VisibilityStatus.Visible, 1));
    
        // Assert Status Code and Message
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("Parental rating cannot be null or empty.", exception.getReason());
        verify(gameRepository, never()).save(any(Game.class));
    }
    
    @Test
    public void testArchiveCategoryWithPermission() {
        // Arrange
        when(gameCategoryRepository.findByCategoryID(1)).thenReturn(category1);
        when(accountService.hasPermission("admin", 3)).thenReturn(true);
    
        // Act
        GameCategory result = gameStoreManagementService.archiveCategory(1, "admin");
    
        // Assert
        assertNotNull(result);
        assertEquals(GameCategory.VisibilityStatus.Archived, result.getStatus());
        verify(gameCategoryRepository, times(1)).save(category1);
    }
    
    @Test
    public void testArchiveCategoryWithoutPermission() {
        // Arrange
        when(gameCategoryRepository.findByCategoryID(1)).thenReturn(category1);
        when(accountService.hasPermission("user", 3)).thenReturn(false);
    
        // Act
        GameCategory result = gameStoreManagementService.archiveCategory(1, "user");
    
        // Assert
        assertNotNull(result);
        assertEquals(GameCategory.VisibilityStatus.PendingArchive, result.getStatus());
        verify(gameCategoryRepository, times(1)).save(category1);
    }
    
    @Test
    public void testArchiveCategoryNonexistent() {
        // Arrange
        when(gameCategoryRepository.findByCategoryID(999)).thenReturn(null);
    
        // Act and Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () ->
            gameStoreManagementService.archiveCategory(999, "admin"));
    
        // Assert Status Code and Message
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("Category with ID 999 not found.", exception.getReason());
        verify(gameCategoryRepository, never()).save(any(GameCategory.class));
    }
    

    @Test
    public void testAddPromotionToGame_Success() {
        // Arrange
        when(gameRepository.findByGameID(1)).thenReturn(game1);
        when(promotionRepository.findByPromotionID(1)).thenReturn(promotion1);
        // Mock the save method to return the game object passed to it
        when(gameRepository.save(any(Game.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Promotion result = gameStoreManagementService.addPromotionToGame(1, 1);

        // Assert
        assertNotNull(result, "The returned Promotion should not be null.");
        assertEquals(promotion1, result, "The returned Promotion should match the promotion1.");

        // Capture the Game object passed to save
        ArgumentCaptor<Game> gameCaptor = ArgumentCaptor.forClass(Game.class);
        verify(gameRepository, times(1)).save(gameCaptor.capture());
        Game savedGame = gameCaptor.getValue();

        // Verify that the promotion was set correctly
        assertEquals(promotion1, savedGame.getPromotion(), "The promotion should be set correctly on the game.");
    }

    @Test
    public void testAddPromotionToGame_PromotionNotFound() {
        // Arrange
        when(gameRepository.findByGameID(1)).thenReturn(game1);
        when(promotionRepository.findByPromotionID(999)).thenReturn(null); // Non-existent promotion

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            gameStoreManagementService.addPromotionToGame(999, 1);
        }, "Expected addPromotionToGame to throw, but it didn't");

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode(), "Exception status should be 404 Not Found");
        assertEquals("Promotion with ID 999 not found.", exception.getReason(), "Exception reason message mismatch");

        // Verify that save was never called
        verify(gameRepository, never()).save(any(Game.class));
    }

    @Test
    public void testAddPromotionToGame_GameNotFound() {
        // Arrange
        when(gameRepository.findByGameID(999)).thenReturn(null); // Non-existent game
        when(promotionRepository.findByPromotionID(1)).thenReturn(promotion1);

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            gameStoreManagementService.addPromotionToGame(1, 999);
        }, "Expected addPromotionToGame to throw, but it didn't");

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode(), "Exception status should be 404 Not Found");
        assertEquals("Game with ID 999 not found.", exception.getReason(), "Exception reason message mismatch");

        // Verify that save was never called
        verify(gameRepository, never()).save(any(Game.class));
    }

    @Test
    public void removePromotionFromGame_Success() {
        // Arrange
        when(gameRepository.findByGameID(1)).thenReturn(game1);
        // Associate promotion with game
        game1.setPromotion(promotion1);
        when(gameRepository.save(any(Game.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Promotion result = gameStoreManagementService.removePromotionFromGame(1, 1);

        // Assert
        assertNull(result, "The returned Promotion should be null after removal.");
        assertNull(game1.getPromotion(), "The game's promotion should be set to null.");

        // Capture the Game object passed to save
        ArgumentCaptor<Game> gameCaptor = ArgumentCaptor.forClass(Game.class);
        verify(gameRepository, times(1)).save(gameCaptor.capture());
        Game savedGame = gameCaptor.getValue();

        // Verify that the promotion was removed
        assertNull(savedGame.getPromotion(), "The saved game should have no promotion associated.");
    }

    @Test
    public void removePromotionFromGame_GameHasNoPromotion() {
        // Arrange
        when(gameRepository.findByGameID(1)).thenReturn(game1);
        // Ensure the game has no promotion
        game1.setPromotion(null);

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            gameStoreManagementService.removePromotionFromGame(1, 1);
        }, "Expected removePromotionFromGame to throw, but it didn't");

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode(), "Exception status should be 404 Not Found");
        assertEquals("Game with ID 1 does not have a promotion.", exception.getReason(), "Exception reason message mismatch");

        // Verify that save was never called
        verify(gameRepository, never()).save(any(Game.class));
    }

}