package group_13.game_store.service;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import group_13.game_store.model.CartItem;
import group_13.game_store.model.Customer;
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

    private GameCategory category2 = new GameCategory("CategoryDescription2",
            group_13.game_store.model.GameCategory.VisibilityStatus.Visible, "SecondCategory");

    private Game game1 = new Game("Game1", "Description1", "img1", 10, 10.0, "PG", VisibilityStatus.Visible, category1);

    private Game game2 = new Game("Game2", "Description2", "img2", 20, 20.0, "PG", VisibilityStatus.PendingArchive,
            category1);

    private Game game3 = new Game("ThirdGame", "Description3", "img3", 30, 30.0, "PG", VisibilityStatus.Archived,
            category2);

    private Game game4 = new Game("FourthGame", "Description4", "img4", 0, 30.0, "PG", VisibilityStatus.PendingArchive,
            category2);

    private Customer customer1 = new Customer("username1", "password1", "email1", "firstName1", "lastName1");

    private Customer customer2 = new Customer("username2", "password2", "email2", "firstName2", "lastName2");

    private Customer customer3 = new Customer("username3", "password3", "email3", "firstName3", "lastName3");

    private CartItem cartItem1Customer1 = new CartItem(new CartItem.Key(customer1, game1), 1);

    private CartItem cartItem2Customer1 = new CartItem(new CartItem.Key(customer1, game2), 2);

    private CartItem cartItem1Customer2 = new CartItem(new CartItem.Key(customer2, game1), 0);

    // Setup mock data
    @BeforeEach
    public void setup() {
        category1.setCategoryID(1);
        game1.setGameID(1);
        game2.setGameID(2);
        game3.setGameID(3);
        game4.setGameID(4);

        category1.setCategoryID(1);
        category2.setCategoryID(2);
    }

    // @@@@@@@@@@@@@@@@@@@@@@@@@@@ Owner and Employee Browsing
    // @@@@@@@@@@@@@@@@@@@@@@@@@@@

    // ************************** getAllGames Tests **************************
    @Test
    public void testGetAllGamesWhenGamesExist() {
        // Arrange
        when(gameRepository.findAll()).thenReturn(List.of(game1, game2, game3, game4));

        // Act
        Iterable<Game> serviceResult = browsingService.getAllGames();

        // Assert non-null and non-empty list
        assertNotNull(serviceResult, "Games should not be null");
        assertTrue(serviceResult.iterator().hasNext(), "Games should not be empty");

        // Convert Iterable to List for easier testing
        List<Game> games = (List<Game>) serviceResult;

        // Assert that the list contains the expected games
        assertEquals(4, games.size(), "There should be 4 games in the list");

        // Assert that the list contains the expected games
        assertTrue(games.contains(game1), "Game1 should be in the list");
        assertTrue(games.contains(game2), "Game2 should be in the list");
        assertTrue(games.contains(game3), "Game3 should be in the list");
        assertTrue(games.contains(game4), "Game4 should be in the list");

        // verify that the repository method was called
        verify(gameRepository, times(1)).findAll();

    }

    @Test
    public void testGetAllGamesWhenNoGamesExist() {
        // Arrange (no games in the repository)
        when(gameRepository.findAll()).thenReturn(List.of());

        // Act and Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> browsingService.getAllGames());

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

    // ************************** getGamesByCategoryName Tests
    // **************************
    @Test
    public void testGetGamesByCategoryNameWhenGamesExist() {
        // Arrange
        String validCategoryName = "CategoryName1";
        when(gameRepository.findByCategory_Name(validCategoryName)).thenReturn(List.of(game1, game2));

        // Act
        List<Game> serviceResult = browsingService.getGamesByCategoryName(validCategoryName);

        // Assert
        assertNotNull(serviceResult);

        // Assert that the list contains the expected games
        assertTrue(serviceResult.contains(game1), "Game1 should be in the list");
        assertTrue(serviceResult.contains(game2), "Game2 should be in the list");
        assertTrue(!serviceResult.contains(game3), "Game3 should not be in the list");
        assertTrue(!serviceResult.contains(game4), "Game4 should not be in the list");

        // verify that the repository method was called
        verify(gameRepository, times(1)).findByCategory_Name(validCategoryName);
    }

    @Test
    public void testGetGamesByCategoryNameWhenNoGamesExist() {
        // Arrange
        String invalidCategoryName = "InvalidCategory";
        when(gameRepository.findByCategory_Name(invalidCategoryName)).thenReturn(List.of());

        // Act and Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> browsingService.getGamesByCategoryName(invalidCategoryName));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("No games found in this category", exception.getReason());

        // verify that the repository method was called
        verify(gameRepository, times(1)).findByCategory_Name(invalidCategoryName);
    }

    // Act

    // ************************** getGamesByTitleStartingWith Tests
    // **************************
    @Test
    public void testGetGamesByTitleStartingWithWhenSingleGame() {
        // Arange
        String validSingleGameStartTitle = "Third";
        when(gameRepository.findByTitleStartingWith(validSingleGameStartTitle)).thenReturn(List.of(game3));

        // Act
        List<Game> serviceResult = browsingService.getGamesByTitleStartingWith(validSingleGameStartTitle);

        // Assert
        assertNotNull(serviceResult);
        assertEquals(1, serviceResult.size());

        // Assert that the list contains the expected games
        assertTrue(serviceResult.contains(game3), "Game3 should be in the list");
        assertTrue(!serviceResult.contains(game1), "Game1 should not be in the list");
        assertTrue(!serviceResult.contains(game2), "Game2 should not be in the list");
        assertTrue(!serviceResult.contains(game4), "Game4 should not be in the list");

        // verify that the repository method was called
        verify(gameRepository, times(1)).findByTitleStartingWith(validSingleGameStartTitle);

    }

    @Test
    public void testGetGamesByTitleStartingWithWhenMultipleGames() {
        // Arrange
        String validMultipleGameStartTitle = "Game";
        when(gameRepository.findByTitleStartingWith(validMultipleGameStartTitle)).thenReturn(List.of(game1, game2));

        // Act
        List<Game> serviceResult = browsingService.getGamesByTitleStartingWith(validMultipleGameStartTitle);

        // Assert
        assertNotNull(serviceResult);
        assertEquals(2, serviceResult.size());

        // Assert that the list contains the expected games
        assertTrue(serviceResult.contains(game1), "Game1 should be in the list");
        assertTrue(serviceResult.contains(game2), "Game2 should be in the list");
        assertTrue(!serviceResult.contains(game3), "Game3 should not be in the list");
        assertTrue(!serviceResult.contains(game4), "Game4 should not be in the list");

        // verify that the repository method was called
        verify(gameRepository, times(1)).findByTitleStartingWith(validMultipleGameStartTitle);
    }

    @Test
    public void testGetGamesByTitleStartingWithWhenNoGames() {
        // Arrange
        String invalidGameStartTitle = "Invalid";
        when(gameRepository.findByTitleStartingWith(invalidGameStartTitle)).thenReturn(List.of());

        // Act and Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> browsingService.getGamesByTitleStartingWith(invalidGameStartTitle));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("No games found with this title", exception.getReason());

        // verify that the repository method was called
        verify(gameRepository, times(1)).findByTitleStartingWith(invalidGameStartTitle);
    }

    // @@@@@@@@@@@@@@@@@@@@@@@@@@@ Customer Browsing @@@@@@@@@@@@@@@@@@@@@@@@@@@

    // ************************** getAllAvailableGames Tests **********************
    @Test
    public void testGetAllAvailableGamesWithGamesAvailable() {
        // Arrange
        when(gameRepository.findByStockGreaterThanAndStatusIn(0,
                List.of(Game.VisibilityStatus.Visible, Game.VisibilityStatus.PendingArchive)))
                .thenReturn(List.of(game1, game2));

        // Act
        List<Game> serviceResult = browsingService.getAllAvailableGames();

        // Assert
        assertNotNull(serviceResult);
        assertEquals(2, serviceResult.size());

        // Assert that the list contains the expected games
        assertTrue(serviceResult.contains(game1), "Game1 should be in the list");
        assertTrue(serviceResult.contains(game2), "Game2 should be in the list");
        assertTrue(!serviceResult.contains(game3), "Game3 should not be in the list");
        assertTrue(!serviceResult.contains(game4), "Game4 should not be in the list");

        // verify that the repository method was called
        verify(gameRepository, times(1)).findByStockGreaterThanAndStatusIn(0,
                List.of(Game.VisibilityStatus.Visible, Game.VisibilityStatus.PendingArchive));
    }

    @Test
    public void testGetAllAvailableGamesWithNoGamesAvailable() {
        // Arrange
        when(gameRepository.findByStockGreaterThanAndStatusIn(0,
                List.of(Game.VisibilityStatus.Visible, Game.VisibilityStatus.PendingArchive)))
                .thenReturn(List.of());

        // Act and Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> browsingService.getAllAvailableGames());

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("No games available", exception.getReason());

        // verify that the repository method was called
        verify(gameRepository, times(1)).findByStockGreaterThanAndStatusIn(0,
                List.of(Game.VisibilityStatus.Visible, Game.VisibilityStatus.PendingArchive));
    }

    // ************************** getAvailableGameById Tests **************************
    @Test
    public void testGetAvailableGameByIdWithGameAvailable() {
        // Arrange
        int validAvailableGameId = 1;
        when(gameRepository.findByGameIDAndStockGreaterThanAndStatusIn(validAvailableGameId, 0,
                List.of(Game.VisibilityStatus.Visible, Game.VisibilityStatus.PendingArchive)))
                .thenReturn(game1);

        // Act
        Game serviceResult = browsingService.getAvailableGameById(validAvailableGameId);

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

        // verify that the repository method was called
        verify(gameRepository, times(1)).findByGameIDAndStockGreaterThanAndStatusIn(validAvailableGameId, 0,
                List.of(Game.VisibilityStatus.Visible, Game.VisibilityStatus.PendingArchive));
    }

    public void testGetAvailableGameByIdWithGameUnavailable() {
        // Arrange
        int invalidAvailableGameId = 3;
        when(gameRepository.findByGameIDAndStockGreaterThanAndStatusIn(invalidAvailableGameId, 0,
                List.of(Game.VisibilityStatus.Visible, Game.VisibilityStatus.PendingArchive)))
                .thenReturn(null);

        // Act and Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> browsingService.getAvailableGameById(invalidAvailableGameId));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("Game not available", exception.getReason());

        // verify that the repository method was called
        verify(gameRepository, times(1)).findByGameIDAndStockGreaterThanAndStatusIn(invalidAvailableGameId, 0,
                List.of(Game.VisibilityStatus.Visible, Game.VisibilityStatus.PendingArchive));
    }

    // ************************** getAvailableGamesByCategoryName Tests **************************
    @Test
    public void testGetAvailableGamesByCategoryNameWhenGamesExist() {
        // Arrange
        String validAvailabCategoryName = "CategoryName1";
        when(gameRepository.findByCategory_NameAndStockGreaterThanAndStatusIn(validAvailabCategoryName, 0,
                List.of(Game.VisibilityStatus.Visible, Game.VisibilityStatus.PendingArchive)))
                .thenReturn(List.of(game1, game2));

        // Act
        List<Game> serviceResult = browsingService.getAvailableGamesByCategoryName(validAvailabCategoryName);

        // Assertle
        assertNotNull(serviceResult);

        // Assert that the list contains the expected games
        assertTrue(serviceResult.contains(game1), "Game1 should be in the list");
        assertTrue(serviceResult.contains(game2), "Game2 should be in the list");
        assertTrue(!serviceResult.contains(game3), "Game3 should not be in the list");
        assertTrue(!serviceResult.contains(game4), "Game4 should not be in the list");

        // verify that the repository method was called
        verify(gameRepository, times(1)).findByCategory_NameAndStockGreaterThanAndStatusIn(validAvailabCategoryName, 0,
                List.of(Game.VisibilityStatus.Visible, Game.VisibilityStatus.PendingArchive));
    }

    @Test
    public void testGetAvailableGamesByCategoryNameWhenNoGamesExist() {
        // Arrange
        String invalidAvailableCategoryName = "InvalidCategory";
        when(gameRepository.findByCategory_NameAndStockGreaterThanAndStatusIn(invalidAvailableCategoryName, 0,
                List.of(Game.VisibilityStatus.Visible, Game.VisibilityStatus.PendingArchive)))
                .thenReturn(List.of());

        // Act and Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> browsingService.getAvailableGamesByCategoryName(invalidAvailableCategoryName));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("No games available in this category", exception.getReason());

        // verify that the repository method was called
        verify(gameRepository, times(1)).findByCategory_NameAndStockGreaterThanAndStatusIn(invalidAvailableCategoryName,
                0, List.of(Game.VisibilityStatus.Visible, Game.VisibilityStatus.PendingArchive));
    }

    // ************************** getAvailableGamesByTitleStartingWith Tests **************************
    @Test
    public void testGetAvailableGamesByTitleStartingWithWhenSingleGame() {
        // Arange
        String validSingleAvailableGameStartTitle = "Game2";
        when(gameRepository.findByTitleStartingWithAndStockGreaterThanAndStatusIn(validSingleAvailableGameStartTitle, 0,
                List.of(Game.VisibilityStatus.Visible, Game.VisibilityStatus.PendingArchive)))
                .thenReturn(List.of(game2));

        // Act
        List<Game> serviceResult = browsingService
                .getAvailableGamesByTitleStartingWith(validSingleAvailableGameStartTitle);

        // Assert
        assertNotNull(serviceResult);
        assertEquals(1, serviceResult.size());
        assertTrue(serviceResult.contains(game2), "Game2 should be in the list");

        // verify that the repository method was called
        verify(gameRepository, times(1)).findByTitleStartingWithAndStockGreaterThanAndStatusIn(
                validSingleAvailableGameStartTitle, 0,
                List.of(Game.VisibilityStatus.Visible, Game.VisibilityStatus.PendingArchive));
    }

    @Test
    public void testGetAvailableGamesByTitleStartingWithWhenMultipleGames() {
        // Arrange
        String validMultipleAvailableGameStartTitle = "Game";
        when(gameRepository.findByTitleStartingWithAndStockGreaterThanAndStatusIn(validMultipleAvailableGameStartTitle,
                0, List.of(Game.VisibilityStatus.Visible, Game.VisibilityStatus.PendingArchive)))
                .thenReturn(List.of(game1, game2));

        // Act
        List<Game> serviceResult = browsingService
                .getAvailableGamesByTitleStartingWith(validMultipleAvailableGameStartTitle);

        // Assert
        assertNotNull(serviceResult);
        assertEquals(2, serviceResult.size());
        assertTrue(serviceResult.contains(game1), "Game1 should be in the list");
        assertTrue(serviceResult.contains(game2), "Game2 should be in the list");

        // verify that the repository method was called
        verify(gameRepository, times(1)).findByTitleStartingWithAndStockGreaterThanAndStatusIn(
                validMultipleAvailableGameStartTitle, 0,
                List.of(Game.VisibilityStatus.Visible, Game.VisibilityStatus.PendingArchive));

    }

    @Test
    public void testGetAvailableGamesByTitleStartingWithWhenNoGames() {
        // Arrange
        String invalidAvailableGameStartTitle = "Third";
        when(gameRepository.findByTitleStartingWithAndStockGreaterThanAndStatusIn(invalidAvailableGameStartTitle, 0,
                List.of(Game.VisibilityStatus.Visible, Game.VisibilityStatus.PendingArchive)))
                .thenReturn(List.of());

        // Act and Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> browsingService.getAvailableGamesByTitleStartingWith(invalidAvailableGameStartTitle));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("No available games with this title", exception.getReason());

        // verify that the repository method was called
        verify(gameRepository, times(1)).findByTitleStartingWithAndStockGreaterThanAndStatusIn(
                invalidAvailableGameStartTitle, 0,
                List.of(Game.VisibilityStatus.Visible, Game.VisibilityStatus.PendingArchive));
    }

    // @@@@@@@@@@@@@@@@@@@@@@@@@@@ Customer Cart @@@@@@@@@@@@@@@@@@@@@@@@@@@

    // ************************** getCustomerCartByUsername Tests **************************
    @Test
    public void testGetCustomerCartByUsernameWhenCartItemsExist() {
        // Arrange
        String validCustomerUsername = "username1";
        when(cartItemRepository.findByKeyCustomerAccountUsername(validCustomerUsername))
                .thenReturn(List.of(cartItem1Customer1, cartItem2Customer1));

        // Act
        List<CartItem> serviceResult = browsingService.getCustomerCartByUsername(validCustomerUsername);

        // Assert
        assertNotNull(serviceResult);
        assertEquals(2, serviceResult.size());
        assertTrue(serviceResult.contains(cartItem1Customer1), "CartItem1 should be in the list");
        assertTrue(serviceResult.contains(cartItem2Customer1), "CartItem2 should be in the list");

        // verify that the repository method was called
        verify(cartItemRepository, times(1)).findByKeyCustomerAccountUsername(validCustomerUsername);

    }

    @Test
    public void testGetCustomerCartByUsernameWhenNoCartItemsExist() {
        // Arrange
        String invalidCustomerUsername = "username3";
        when(cartItemRepository.findByKeyCustomerAccountUsername(invalidCustomerUsername)).thenReturn(List.of());

        // Act
        List<CartItem> serviceResult = browsingService.getCustomerCartByUsername(invalidCustomerUsername);

        // Assert
        assertNotNull(serviceResult);
        assertEquals(0, serviceResult.size()); // empty list for no cart items

        // verify that the repository method was called
        verify(cartItemRepository, times(1)).findByKeyCustomerAccountUsername(invalidCustomerUsername);
    }

    // Note don't need to validate the username because it is done in the controller

    // ************************** addGameToCart Tests **************************
    @Test
    public void testAddGameToCartWhenGameAvailable() {
        // Arrange
        String validCustomerUsername = "username1";
        int validGameId = 1;
        when(gameRepository.findByGameIDAndStockGreaterThanAndStatusIn(validGameId, 0,
                List.of(Game.VisibilityStatus.Visible, Game.VisibilityStatus.PendingArchive)))
                .thenReturn(game1);

        // Act
        boolean isSuccesful = browsingService.addGameToCart(validGameId, validCustomerUsername, validGameId);

        // Assert
        assertTrue(isSuccesful);

        // verify that the repository method was called
        verify(gameRepository, times(1)).findByGameIDAndStockGreaterThanAndStatusIn(validGameId, 0,
                List.of(Game.VisibilityStatus.Visible, Game.VisibilityStatus.PendingArchive));
    }

    @Test
    public void testAddGameToCartWhenInvalidQuantity() {
        // Arrange
        String validCustomerUsername = "username1";
        int validGameId = 1;
        int invalidQuantity = -1;
        when(gameRepository.findByGameIDAndStockGreaterThanAndStatusIn(validGameId, 0,
                List.of(Game.VisibilityStatus.Visible, Game.VisibilityStatus.PendingArchive)))
                .thenReturn(game1);

        // Act and Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> browsingService.addGameToCart(validGameId, validCustomerUsername, invalidQuantity));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("Invalid quantity", exception.getReason());

        // verify that the repository method was called
        verify(gameRepository, times(1)).findByGameIDAndStockGreaterThanAndStatusIn(validGameId, 0,
                List.of(Game.VisibilityStatus.Visible, Game.VisibilityStatus.PendingArchive));

    }

}
