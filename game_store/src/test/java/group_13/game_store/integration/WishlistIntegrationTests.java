package group_13.game_store.integration;

import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;

import group_13.game_store.dto.GameResponseDto;
import group_13.game_store.dto.GameListResponseDto;
import group_13.game_store.model.WishlistItem;
import group_13.game_store.model.Customer;
import group_13.game_store.model.Employee;
import group_13.game_store.model.Game;
import group_13.game_store.model.GameCategory;
import group_13.game_store.repository.WishlistItemRepository;
import group_13.game_store.repository.CustomerRepository;
import group_13.game_store.repository.EmployeeRepository;
import group_13.game_store.repository.GameCategoryRepository;
import group_13.game_store.repository.GameRepository;
import group_13.game_store.repository.PromotionRepository;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

import group_13.game_store.model.Promotion;

import org.springframework.http.ResponseEntity;
import org.junit.jupiter.api.Order;
import static org.junit.jupiter.api.Assertions.*;

import org.springframework.http.HttpStatus;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT) // random port for testing
@TestMethodOrder(OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class WishlistIntegrationTests {
    @Autowired
    private TestRestTemplate client;
    @Autowired
    private GameRepository gameRepo;
    @Autowired
    private GameCategoryRepository gameCategoryRepo;
    @Autowired
    private CustomerRepository customerRepo;
    @Autowired
    private EmployeeRepository employeeRepo;
    @Autowired
    private WishlistItemRepository wishlistItemRepo;
    @Autowired
    private PromotionRepository promotionRepo;

    // Private fields for expected GameResponseDto instances
    private GameResponseDto expectedGame1;
    private GameResponseDto expectedGame2;
    private GameResponseDto expectedGame3;

    private GameCategory gameCategory1;
    private Game game1;
    private Game game2;
    private Game game3;
    private Game game4;
    private Game game5;
    private Game game6;
    private Game game7;

    private WishlistItem customer1Item1;
    private WishlistItem customer1Item2;

    private WishlistItem customer2Item1;
    private WishlistItem customer2Item2;

    @BeforeAll
    public void setup() {

        // Clear the databases just in case
        wishlistItemRepo.deleteAll();
        gameRepo.deleteAll();
        gameCategoryRepo.deleteAll();
        customerRepo.deleteAll();
        employeeRepo.deleteAll();

        // ***** Game Categories *****

        // Game Category 1: Visible
        gameCategory1 = new GameCategory("Description 1", GameCategory.VisibilityStatus.Visible,
                "GameCategory1");
        gameCategoryRepo.save(gameCategory1);

        // Game Category 2: Visible
        GameCategory gameCategory2 = new GameCategory("Description 2", GameCategory.VisibilityStatus.Visible,
                "GameCategory2");
        gameCategoryRepo.save(gameCategory2);

        // ***** Games *****

        // Game 1: In stock and visible --> Available
        game1 = new Game("Game 1", "Description 1", "Image 1", 10, 9.99, "18+", Game.VisibilityStatus.Visible,
                gameCategory1);
        gameRepo.save(game1);

        // Game 2: In stock and visible
        game2 = new Game("Game 2", "Description 2", "Image 2", 234, 19.99, "18+", Game.VisibilityStatus.Visible,
                gameCategory1);
        gameRepo.save(game2);

        // Game 3: In stock and Pending Archive --> Available
        game3 = new Game("Game 3", "Description 3", "Image 3", 5, 29.99, "18+", Game.VisibilityStatus.PendingArchive,
                gameCategory1);
        gameRepo.save(game3);

        // Game 4: Out of stock and archived
        game4 = new Game("Game 4", "Description 4", "Image 4", 0, 39.99, "18+", Game.VisibilityStatus.Archived,
                gameCategory1);
        gameRepo.save(game4);

        // Game 5: In stock and pending archive --> Available
        game5 = new Game("Game 5", "Description 5", "Image 5", 3, 49.99, "18+",
                Game.VisibilityStatus.PendingArchive, gameCategory1);
        gameRepo.save(game5);

        // Game 6: Out of stock and pending archive
        game6 = new Game("Game 6", "Description 6", "Image 6", 0, 59.99, "18+",
                Game.VisibilityStatus.PendingArchive, gameCategory1);
        gameRepo.save(game6);

        // Game 7: In stock and visible --> Available
        game7 = new Game("TitleThatDoesNotStartWithGame", "Description 7", "Image 7", 10, 9.99, "18+",
                Game.VisibilityStatus.Visible, gameCategory2);
        gameRepo.save(game7);

        // Initialize expected GameResponseDto instances
        expectedGame1 = new GameResponseDto(
                game1.getGameID(),
                game1.getTitle(),
                game1.getDescription(),
                game1.getImg(),
                game1.getStock(),
                game1.getPrice(),
                game1.getParentalRating(),
                game1.getStatus().toString(),
                game1.getCategory().getCategoryID(),
                "",
                "",
                0,
                0);

        expectedGame2 = new GameResponseDto(
                game2.getGameID(),
                game2.getTitle(),
                game2.getDescription(),
                game2.getImg(),
                game2.getStock(),
                game2.getPrice(),
                game2.getParentalRating(),
                game2.getStatus().toString(),
                game2.getCategory().getCategoryID(),
                "",
                "",
                0,
                0);

        expectedGame3 = new GameResponseDto(
                game3.getGameID(),
                game3.getTitle(),
                game3.getDescription(),
                game3.getImg(),
                game3.getStock(),
                game3.getPrice(),
                game3.getParentalRating(),
                game3.getStatus().toString(),
                game3.getCategory().getCategoryID(),
                "",
                "",
                0,
                0);

        // ***** Users *****

        // Customer 1:
        Customer customer1 = new Customer("Customer1Name", "Customer1Username", "Customer1Email", "Customer1Password",
                "Customer1PhoneNumber");
        customerRepo.save(customer1);

        // Customer 2:
        Customer customer2 = new Customer("Customer2Name", "Customer2Username", "Customer2Email", "Customer2Password",
                "Customer2PhoneNumber");
        customerRepo.save(customer2);

        // Employee 1:
        Employee employee1 = new Employee("Employee1Name", "Employee1Username", "Employee1Email", "Employee1Password",
                "Employee1PhoneNumber", true);
        employeeRepo.save(employee1);
        // Owner is already created in the database

        // ***** Wishlist Items *****

        // Customer 1's Wishlist Items
        customer1Item1 = new WishlistItem(new WishlistItem.Key(customer1, game1));
        wishlistItemRepo.save(customer1Item1);
        customer1Item2 = new WishlistItem(new WishlistItem.Key(customer1, game2));
        wishlistItemRepo.save(customer1Item2);

        // Customer 2's Wishlist Items
        customer2Item1 = new WishlistItem(new WishlistItem.Key(customer2, game1));
        wishlistItemRepo.save(customer2Item1);
        customer2Item2 = new WishlistItem(new WishlistItem.Key(customer2, game2));
        wishlistItemRepo.save(customer2Item2);

    }

    @AfterAll
    public void cleanup() {
        wishlistItemRepo.deleteAll();
        gameRepo.deleteAll();
        gameCategoryRepo.deleteAll();
        customerRepo.deleteAll();
        employeeRepo.deleteAll();
    }

    // Tests for /customers/{customerUsername}/wishlist GET (get the wishlist)

    @Test
    @Order(1)
    public void testGetCustomerWishlistWhenSuccess(){
        // arrange
        String url = String.format("/customers/%s/wishlist", "Customer1Username");

        // act
        ResponseEntity<GameListResponseDto> response = client.getForEntity(url, GameListResponseDto.class);

        // assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        GameListResponseDto responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals(2, responseBody.getGames().size());

        // Check if the games are correct by comparing the fields
        GameResponseDto game1Response = responseBody.getGames().get(0);
        GameResponseDto game2Response = responseBody.getGames().get(1);

        // check the ids and fields
        assertEquals(expectedGame1.getGameID(), game1Response.getGameID());
        assertEquals(expectedGame2.getGameID(), game2Response.getGameID());
        assertEquals(expectedGame1.getTitle(), game1Response.getTitle());
        assertEquals(expectedGame2.getTitle(), game2Response.getTitle());
        assertEquals(expectedGame1.getDescription(), game1Response.getDescription());
        assertEquals(expectedGame2.getDescription(), game2Response.getDescription());
        assertEquals(expectedGame1.getImg(), game1Response.getImg());
        assertEquals(expectedGame2.getImg(), game2Response.getImg());
        assertEquals(expectedGame1.getStock(), game1Response.getStock());
        assertEquals(expectedGame2.getStock(), game2Response.getStock());
        assertEquals(expectedGame1.getPrice(), game1Response.getPrice());
        assertEquals(expectedGame2.getPrice(), game2Response.getPrice());
        assertEquals(expectedGame1.getParentalRating(), game1Response.getParentalRating());
        assertEquals(expectedGame2.getParentalRating(), game2Response.getParentalRating());
        assertEquals(expectedGame1.getStatus(), game1Response.getStatus());
        assertEquals(expectedGame2.getStatus(), game2Response.getStatus());
        assertEquals(expectedGame1.getCategoryId(), game1Response.getCategoryId());
        assertEquals(expectedGame2.getCategoryId(), game2Response.getCategoryId());
        assertEquals(expectedGame1.getPromotionName(), game1Response.getPromotionName());
        assertEquals(expectedGame2.getPromotionName(), game2Response.getPromotionName());
    }

    @Test
    @Order(2)
    public void testGetCustomerWishlistWhenPermissionDenied(){
        // arrange
        String url = String.format("/customers/%s/wishlist", "Employee1Username");

        // act
        ResponseEntity<String> response = client.getForEntity(url, String.class);

        // assert
        try {
            // Parse the response body as JSON
            org.json.JSONObject json = new org.json.JSONObject(response.getBody());
            assertEquals(403, json.getInt("status"));
            assertEquals("Forbidden", json.getString("error"));
            assertEquals("only customers can access their wishlists", json.getString("message"));
        } catch (org.json.JSONException e) {
            fail("Response body is not a valid JSON");
        }
    }

    // Tests for /customers/{customerUsername}/wishlist DELETE (clears the whole wishlist)
    @Test
    @Order(3)
    public void testClearCustomerWishlistWhenSuccess(){
        // arrange
        String url = String.format("/customers/%s/wishlist", "Customer1Username");

        // give one of the games a promotion with null title edge case
        Date startDate = Date.valueOf(LocalDate.now().minusDays(1));
        Date endDate = Date.valueOf(LocalDate.now().plusDays(1));
        Promotion promotion1 = new Promotion(10, startDate, endDate, null, "Promotion1Description");
        promotionRepo.save(promotion1);
        game1.setPromotion(promotion1);
        gameRepo.save(game1);
        

        // act
        ResponseEntity<GameListResponseDto> response = client.exchange(url, org.springframework.http.HttpMethod.DELETE, null, GameListResponseDto.class);

        // assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        GameListResponseDto responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals(0, responseBody.getGames().size());

        // Check the database
        List<WishlistItem> wishlist = wishlistItemRepo.findByKey_CustomerAccount_Username("Customer1Username");
        assertEquals(0, wishlist.size());
    }

    @Test
    @Order(4)
    public void testClearCustomerWishlistWhenPermissionDenied(){
        // arrange
        String url = String.format("/customers/%s/wishlist", "Employee1Username");

        // act
        ResponseEntity<String> response = client.exchange(url, org.springframework.http.HttpMethod.DELETE, null, String.class);

        // assert
        try {
            // Parse the response body as JSON
            org.json.JSONObject json = new org.json.JSONObject(response.getBody());
            assertEquals(403, json.getInt("status"));
            assertEquals("Forbidden", json.getString("error"));
            assertEquals("only customers can access their wishlists", json.getString("message"));
        } catch (org.json.JSONException e) {
            fail("Response body is not a valid JSON");
        }
    }

    // Tests for /customers/{customerUsername}/wishlist/{gameID} PUT (add a game to the wishlist)

    @Test
    @Order(5)
    public void testAddGameToCustomerWishlistWhenSuccess(){
        // arrange
        String url = String.format("/customers/%s/wishlist/%d", "Customer1Username", game3.getGameID());

        // act
        ResponseEntity<GameResponseDto> response = client.exchange(url, org.springframework.http.HttpMethod.PUT, null, GameResponseDto.class);

        // assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        GameResponseDto responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals(expectedGame3.getGameID(), responseBody.getGameID());

        // ensure in the database
        List<WishlistItem> wishlist = wishlistItemRepo.findByKey_CustomerAccount_Username("Customer1Username");

        assertEquals(1, wishlist.size());
        // Check that game3 is in the wishlist
        boolean found = false;
        for (WishlistItem item : wishlist) {
            if (item.getKey().getGame().getGameID() == game3.getGameID()) {
                found = true;
                break;
            }
        }
        assertTrue(found);
    }

    @Test
    @Order(6)
    public void testAddGameToCustomerWishlistWhenPermissionDenied(){
        // arrange
        String url = String.format("/customers/%s/wishlist/%d", "Employee1Username", game3.getGameID());

        // act
        ResponseEntity<String> response = client.exchange(url, org.springframework.http.HttpMethod.PUT, null, String.class);

        // assert
        try {
            // Parse the response body as JSON
            org.json.JSONObject json = new org.json.JSONObject(response.getBody());
            assertEquals(403, json.getInt("status"));
            assertEquals("Forbidden", json.getString("error"));
            assertEquals("only customers can access their wishlists", json.getString("message"));
        } catch (org.json.JSONException e) {
            fail("Response body is not a valid JSON");
        }
    }

    // Tests for /customers/{customerUsername}/wishlist/{gameID} DELETE (remove a game from the wishlist)
    @Test
    @Order(7)
    public void testRemoveGameFromWishlistWhenSuccess(){
        // arrange
        String url = String.format("/customers/%s/wishlist/%d", "Customer2Username", game2.getGameID());

        // act
        ResponseEntity<GameResponseDto> response = client.exchange(url, org.springframework.http.HttpMethod.DELETE, null, GameResponseDto.class);

        // assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        GameResponseDto responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals(expectedGame2.getGameID(), responseBody.getGameID());

        // ensure in the database
        List<WishlistItem> wishlist = wishlistItemRepo.findByKey_CustomerAccount_Username("Customer2Username");

        assertEquals(1, wishlist.size());
        // Check that game2 is not in the wishlist
        for (WishlistItem item : wishlist) {
            assertNotEquals(game2.getGameID(), item.getKey().getGame().getGameID());
        }
    }

    @Test
    @Order(8)
    public void testRemoveGameFromWishlistWhenPermissionDenied(){
        // arrange
        String url = String.format("/customers/%s/wishlist/%d", "Employee1Username", game1.getGameID());

        // act
        ResponseEntity<String> response = client.exchange(url, org.springframework.http.HttpMethod.DELETE, null, String.class);

        // assert
        try {
            // Parse the response body as JSON
            org.json.JSONObject json = new org.json.JSONObject(response.getBody());
            assertEquals(403, json.getInt("status"));
            assertEquals("Forbidden", json.getString("error"));
            assertEquals("only customers can access their wishlists", json.getString("message"));
        } catch (org.json.JSONException e) {
            fail("Response body is not a valid JSON");
        }
    }

}
