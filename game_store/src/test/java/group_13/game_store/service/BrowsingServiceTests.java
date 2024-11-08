package group_13.game_store.service;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import group_13.game_store.model.Game;
import group_13.game_store.model.GameCategory;
import group_13.game_store.model.Game.VisibilityStatus;
import group_13.game_store.repository.CartItemRepository;
import group_13.game_store.repository.CustomerRepository;
import group_13.game_store.repository.GameRepository;
import group_13.game_store.repository.PromotionRepository;
import group_13.game_store.repository.WishlistItemRepository;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import org.springframework.web.server.ResponseStatusException;

import org.junit.jupiter.api.Test;

@SpringBootTest
public class BrowsingServiceTests {
    // Repo Mocks
    @Mock
    private GameRepository gameRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private CartItemRepository cartItemRepository;

    @Mock
    private WishlistItemRepository wishlistItemRepository;

    @Mock
    private PromotionRepository promotionRepository;

    // Service
    @InjectMocks
    private BrowsingService browsingService;

    // Sample data
    private GameCategory category1 = new GameCategory("CategoryDescription1",
            group_13.game_store.model.GameCategory.VisibilityStatus.Visible, "CategoryName1");

    private Game game1 = new Game("Game1", "Description1", "img1", 10, 10.0, "PG", VisibilityStatus.Visible, category1);

    private Game game2 = new Game("Game2", "Description2", "img2", 20, 20.0, "PG", VisibilityStatus.PendingArchive,
            category1);

    private Game game3 = new Game("Game3", "Description3", "img3", 30, 30.0, "PG", VisibilityStatus.Archived,
            category1);

    // Setup mock data
    @BeforeEach
    public void setup() {
        category1.setCategoryID(1);
        game1.setGameID(1);
        game2.setGameID(2);
        game3.setGameID(3);

        category1.setCategoryID(1);
    }

    // ************************** getAllGames Tests **************************
    @Test
    public void testGetAllGamesWhenGamesExist() {
        // Arrange
        when(gameRepository.findAll()).thenReturn(List.of(game1, game2, game3));

        // Act
        Iterable<Game> serviceResult = browsingService.getAllGames();

        // Assert non-null and non-empty list
        assertNotNull(serviceResult, "Games should not be null");
        assertTrue(serviceResult.iterator().hasNext(), "Games should not be empty");

        // Convert Iterable to List for easier testing
        List<Game> games = (List<Game>) serviceResult;

        // Assert that the list contains the expected games
        assertEquals(3, games.size(), "There should be 3 games in the list");

        // Assert that the list contains the expected games
        assertTrue(games.contains(game1), "Game1 should be in the list");
        assertTrue(games.contains(game2), "Game2 should be in the list");
        assertTrue(games.contains(game3), "Game3 should be in the list");

        // verify that the repository method was called
        verify(gameRepository, times(1)).findAll();

    }

    @Test
    public void testGetAllGamesWhenNoGamesExist() {
        // Arrange (no games in the repository)
        when(gameRepository.findAll()).thenReturn(List.of());

        // Act and Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> browsingService.getAllGames());

        assertEquals(exception.getStatusCode(), HttpStatus.NOT_FOUND);
        assertEquals(exception.getReason(), "No games found");

        // verify that the repository method was called
        verify(gameRepository, times(1)).findAll();
    }

    // ************************** getGameById Tests *****************************

    @Test
    public void testGetGameByIdWithValidId() {
        // Arrange
        int validGameID = 1;
        when(gameRepository.findByGameID(validGameID)).thenReturn(game1);

        // Act
        Game serviceResult = browsingService.getGameById(validGameID);

        // Assert
        assertNotNull(serviceResult);
        assertEquals(serviceResult.getGameID(), game1.getGameID());
        assertEquals(serviceResult.getTitle(), game1.getTitle());
        assertEquals(serviceResult.getDescription(), game1.getDescription());
        assertEquals(serviceResult.getImg(), game1.getImg());
        assertEquals(serviceResult.getStock(), game1.getStock());
        assertEquals(serviceResult.getPrice(), game1.getPrice());
        assertEquals(serviceResult.getParentalRating(), game1.getParentalRating());
        assertEquals(serviceResult.getStatus(), game1.getStatus());
        assertEquals(serviceResult.getCategory().getCategoryID(), game1.getCategory().getCategoryID());

        // verify that the repository method was called
        verify(gameRepository, times(1)).findByGameID(validGameID);
    }

    @Test
    public void testGetGameByIdWithInvalidId() {
        // Arrange
        int invalidGameId = 999;
        when(gameRepository.findByGameID(invalidGameId)).thenReturn(null);

        // Act and Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> browsingService.getGameById(invalidGameId));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("Game not found", exception.getReason());

        // verify that the repository method was called
        verify(gameRepository, times(1)).findByGameID(invalidGameId);
    }

    // ************************** getGamesByTitleStartingWith Tests **************************

    // ************************** getGamesByCategoryName Tests **************************

}
