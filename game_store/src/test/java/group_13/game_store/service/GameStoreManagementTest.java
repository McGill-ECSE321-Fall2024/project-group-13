package group_13.game_store.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import group_13.game_store.model.Game;
import group_13.game_store.model.GameCategory;
import group_13.game_store.model.Promotion;
import group_13.game_store.model.Employee;
import group_13.game_store.repository.GameCategoryRepository;
import group_13.game_store.repository.GameRepository;
import group_13.game_store.repository.PromotionRepository;
import group_13.game_store.repository.EmployeeRepository;

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
    private AccountService accountService;

    @InjectMocks
    private GameStoreManagementService gameStoreManagementService;

    private GameCategory category1;
    private Game game1;
    private Game game2;
    private Promotion promotion1;
    private Date startDate;
    private Date endDate;

    @BeforeEach
    public void setup() {
        // Setup sample data
        category1 = new GameCategory("CategoryDescription1", GameCategory.VisibilityStatus.Visible, "CategoryName1");
        category1.setCategoryID(1);

        game1 = new Game("Game1", "Description1", "img1", 10, 10.0, "PG", Game.VisibilityStatus.Visible, category1);
        game1.setGameID(1);

        game2 = new Game("Game2", "Description2", "img2", 20, 20.0, "PG", Game.VisibilityStatus.PendingArchive, category1);
        game2.setGameID(2);

        startDate = Date.valueOf("2024-01-01");
        endDate = Date.valueOf("2024-12-31");

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
        Exception exception = assertThrows(IllegalArgumentException.class, () -> 
            gameStoreManagementService.addGame("Game1", "Description1", "img1", 10, 10.0, "PG", Game.VisibilityStatus.Visible, 999));

        assertEquals("Invalid category ID.", exception.getMessage());
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
        Exception exception = assertThrows(IllegalArgumentException.class, () -> 
            gameStoreManagementService.archiveGame(999));
        assertEquals("Game with ID 999 not found.", exception.getMessage());
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
        Exception exception = assertThrows(IllegalArgumentException.class, () -> 
            gameStoreManagementService.addPromotion(10, endDate, startDate, "Holiday Sale", "10% off"));

        assertEquals("End date cannot be before start date.", exception.getMessage());
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
        Exception exception = assertThrows(IllegalArgumentException.class, () -> 
            gameStoreManagementService.deletePromotion(999));
        assertEquals("Promotion with ID 999 not found.", exception.getMessage());
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
}