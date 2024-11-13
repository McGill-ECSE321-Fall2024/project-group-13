package group_13.game_store.integration;

import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.server.ResponseStatusException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.AfterAll;

import group_13.game_store.repository.GameRepository;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import group_13.game_store.model.Game;
import group_13.game_store.repository.CustomerRepository;
import group_13.game_store.repository.GameCategoryRepository;
import group_13.game_store.model.GameCategory;
import group_13.game_store.dto.GameListResponseDto;
import group_13.game_store.dto.GameResponseDto;
import group_13.game_store.model.Customer;
import group_13.game_store.model.Employee;
import group_13.game_store.repository.EmployeeRepository;

import java.util.Arrays;
import java.util.List;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT) // random port for testing
@TestMethodOrder(OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class GameIntegrationTests {
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

    // Private fields for expected GameResponseDto instances
    private GameResponseDto expectedGame1;
    private GameResponseDto expectedGame2;
    private GameResponseDto expectedGame3;
    private GameResponseDto expectedGame4;
    private GameResponseDto expectedGame5;
    private GameResponseDto expectedGame6;
    private GameResponseDto expectedGame7;

    @BeforeAll
    public void setup() {

        // Clear the databases just in case
        gameRepo.deleteAll();
        gameCategoryRepo.deleteAll();
        customerRepo.deleteAll();
        employeeRepo.deleteAll();

        // ***** Game Categories *****

        // Game Category 1: Visible
        GameCategory gameCategory1 = new GameCategory("Description 1", GameCategory.VisibilityStatus.Visible,
                "GameCategory1");
        gameCategoryRepo.save(gameCategory1);

        // Game Category 2: Visible
        GameCategory gameCategory2 = new GameCategory("Description 2", GameCategory.VisibilityStatus.Visible,
                "GameCategory2");
        gameCategoryRepo.save(gameCategory2);

        // ***** Games *****

        // Game 1: In stock and visible --> Available
        Game game1 = new Game("Game 1", "Description 1", "Image 1", 10, 9.99, "18+", Game.VisibilityStatus.Visible,
                gameCategory1);
        gameRepo.save(game1);

        // Game 2: Out of stock and visible
        Game game2 = new Game("Game 2", "Description 2", "Image 2", 0, 19.99, "18+", Game.VisibilityStatus.Visible,
                gameCategory1);
        gameRepo.save(game2);

        // Game 3: In stock and archived
        Game game3 = new Game("Game 3", "Description 3", "Image 3", 5, 29.99, "18+", Game.VisibilityStatus.Archived,
                gameCategory1);
        gameRepo.save(game3);

        // Game 4: Out of stock and archived
        Game game4 = new Game("Game 4", "Description 4", "Image 4", 0, 39.99, "18+", Game.VisibilityStatus.Archived,
                gameCategory1);
        gameRepo.save(game4);

        // Game 5: In stock and pending archive --> Available
        Game game5 = new Game("Game 5", "Description 5", "Image 5", 3, 49.99, "18+",
                Game.VisibilityStatus.PendingArchive, gameCategory1);
        gameRepo.save(game5);

        // Game 6: Out of stock and pending archive
        Game game6 = new Game("Game 6", "Description 6", "Image 6", 0, 59.99, "18+",
                Game.VisibilityStatus.PendingArchive, gameCategory1);
        gameRepo.save(game6);

        // Game 7: In stock and visible --> Available
        Game game7 = new Game("TitleThatDoesNotStartWithGame", "Description 7", "Image 7", 10, 9.99, "18+",
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
                game1.getCategory().getName(),
                null);

        expectedGame2 = new GameResponseDto(
                game2.getGameID(),
                game2.getTitle(),
                game2.getDescription(),
                game2.getImg(),
                game2.getStock(),
                game2.getPrice(),
                game2.getParentalRating(),
                game2.getStatus().toString(),
                game2.getCategory().getName(),
                null);

        expectedGame3 = new GameResponseDto(
                game3.getGameID(),
                game3.getTitle(),
                game3.getDescription(),
                game3.getImg(),
                game3.getStock(),
                game3.getPrice(),
                game3.getParentalRating(),
                game3.getStatus().toString(),
                game3.getCategory().getName(),
                null);

        expectedGame4 = new GameResponseDto(
                game4.getGameID(),
                game4.getTitle(),
                game4.getDescription(),
                game4.getImg(),
                game4.getStock(),
                game4.getPrice(),
                game4.getParentalRating(),
                game4.getStatus().toString(),
                game4.getCategory().getName(),
                null);

        expectedGame5 = new GameResponseDto(
                game5.getGameID(),
                game5.getTitle(),
                game5.getDescription(),
                game5.getImg(),
                game5.getStock(),
                game5.getPrice(),
                game5.getParentalRating(),
                game5.getStatus().toString(),
                game5.getCategory().getName(),
                null);

        expectedGame6 = new GameResponseDto(
                game6.getGameID(),
                game6.getTitle(),
                game6.getDescription(),
                game6.getImg(),
                game6.getStock(),
                game6.getPrice(),
                game6.getParentalRating(),
                game6.getStatus().toString(),
                game6.getCategory().getName(),
                null);

        expectedGame7 = new GameResponseDto(
                game7.getGameID(),
                game7.getTitle(),
                game7.getDescription(),
                game7.getImg(),
                game7.getStock(),
                game7.getPrice(),
                game7.getParentalRating(),
                game7.getStatus().toString(),
                game7.getCategory().getName(),
                null);

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
    }

    @AfterAll
    public void cleanup() {
        gameRepo.deleteAll();
        gameCategoryRepo.deleteAll();
        customerRepo.deleteAll();
        employeeRepo.deleteAll();
    }

    // **** GET /games Tests ****
    @Test
    @Order(1)
    public void testGetGamesAsCustomer() {
        // Arrange
        String url = "/games?loggedInUsername=Customer1Username";
        System.out.println(String.format("URL: %s", url));

        // Act
        ResponseEntity<GameListResponseDto> response = client.getForEntity(url, GameListResponseDto.class);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        List<GameResponseDto> actualGames = response.getBody().getGames();
        assertNotNull(actualGames);
        assertEquals(3, actualGames.size());

        // Expected games
        List<GameResponseDto> expectedGames = Arrays.asList(expectedGame1, expectedGame5, expectedGame7);

        // Assert equality
        assertTrue(actualGames.containsAll(expectedGames));
    }

    @Test
    @Order(2)
    public void testGetGamesAsGuest() {
        // Arrange
        String url = "/games?loggedInUsername=Customer1Username";
        System.out.println(String.format("URL: %s", url));

        // Act
        ResponseEntity<GameListResponseDto> response = client.getForEntity(url, GameListResponseDto.class);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        List<GameResponseDto> actualGames = response.getBody().getGames();
        assertNotNull(actualGames);
        assertEquals(3, actualGames.size());

        // Expected games
        List<GameResponseDto> expectedGames = Arrays.asList(expectedGame1, expectedGame5, expectedGame7);

        // Assert equality
        assertTrue(actualGames.containsAll(expectedGames));
    }

    @Test
    @Order(3)
    public void testGetAllGamesAsEmployee() {
        // Arrange
        String url = "/games?loggedInUsername=Employee1Username";
        System.out.println(String.format("URL: %s", url));

        // Act
        ResponseEntity<GameListResponseDto> response = client.getForEntity(url, GameListResponseDto.class);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        List<GameResponseDto> actualGames = response.getBody().getGames();
        assertNotNull(actualGames);
        assertEquals(7, actualGames.size());

        // Expected games
        List<GameResponseDto> expectedGames = Arrays.asList(
                expectedGame1, expectedGame2, expectedGame3, expectedGame4,
                expectedGame5, expectedGame6, expectedGame7);

        // Assert equality
        assertTrue(actualGames.containsAll(expectedGames));
    }

    @Test
    @Order(4)
    public void testGetAllGamesAsOwner() {
        // Arrange
        String url = "/games?loggedInUsername=owner";
        System.out.println(String.format("URL: %s", url));

        // Act
        ResponseEntity<GameListResponseDto> response = client.getForEntity(url, GameListResponseDto.class);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        List<GameResponseDto> actualGames = response.getBody().getGames();
        assertNotNull(actualGames);
        assertEquals(7, actualGames.size());

        // Expected games (same as for employee)
        List<GameResponseDto> expectedGames = Arrays.asList(
                expectedGame1, expectedGame2, expectedGame3, expectedGame4,
                expectedGame5, expectedGame6, expectedGame7);

        // Assert equality
        assertTrue(actualGames.containsAll(expectedGames));
    }

    @Test
    @Order(5)
    public void testGetAllGamesStartingWithGameAsCustomerOrGuest() {
        // Arrange
        String url = "/games?loggedInUsername=Customer1Username&title=Game";
        System.out.println(String.format("URL: %s", url));

        // Act
        ResponseEntity<GameListResponseDto> response = client.getForEntity(url, GameListResponseDto.class);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        List<GameResponseDto> actualGames = response.getBody().getGames();
        assertNotNull(actualGames);
        assertEquals(2, actualGames.size());

        // Expected games (expectedGame1 and expectedGame5)
        List<GameResponseDto> expectedGames = Arrays.asList(expectedGame1, expectedGame5);

        // Assert equality
        assertTrue(actualGames.containsAll(expectedGames));
    }

    @Test
    @Order(6)
    public void testGetAllGamesStartingWithGameAsEmployeeOrOwner() {
        // Arrange
        String url = "/games?loggedInUsername=Employee1Username&title=Game";
        System.out.println(String.format("URL: %s", url));

        // Act
        ResponseEntity<GameListResponseDto> response = client.getForEntity(url, GameListResponseDto.class);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        List<GameResponseDto> actualGames = response.getBody().getGames();
        assertNotNull(actualGames);
        assertEquals(6, actualGames.size());

        // Expected games (all games starting with "Game")
        List<GameResponseDto> expectedGames = Arrays.asList(
                expectedGame1, expectedGame2, expectedGame3,
                expectedGame4, expectedGame5, expectedGame6);

        // Assert equality
        assertTrue(actualGames.containsAll(expectedGames));
    }

    @Test
    @Order(7)
    public void testGetAllGamesWithCategoryAsCustomerOrGuest() {
        // Arrange
        String url = "/games?loggedInUsername=Customer1Username&category=GameCategory2";
        System.out.println(String.format("URL: %s", url));

        // Act
        ResponseEntity<GameListResponseDto> response = client.getForEntity(url, GameListResponseDto.class);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        List<GameResponseDto> actualGames = response.getBody().getGames();
        assertNotNull(actualGames);
        assertEquals(1, actualGames.size());

        // Expected game (expectedGame7)
        List<GameResponseDto> expectedGames = Arrays.asList(expectedGame7);

        // Assert equality
        assertTrue(actualGames.containsAll(expectedGames));
    }

    @Test
    @Order(8)
    public void testGetAllGamesWithCategoryAsEmployeeOrOwner() {
        // Arrange
        String url = "/games?loggedInUsername=Employee1Username&category=GameCategory1";
        System.out.println(String.format("URL: %s", url));

        // Act
        ResponseEntity<GameListResponseDto> response = client.getForEntity(url, GameListResponseDto.class);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        List<GameResponseDto> actualGames = response.getBody().getGames();
        assertNotNull(actualGames);
        assertEquals(6, actualGames.size());

        // Expected games (all games in GameCategory1)
        List<GameResponseDto> expectedGames = Arrays.asList(
                expectedGame1, expectedGame2, expectedGame3,
                expectedGame4, expectedGame5, expectedGame6);

        // Assert equality
        assertTrue(actualGames.containsAll(expectedGames));
    }

        @Test
        @Order(9)
        public void testGetAllGamesNoGamesFoundException() {
                // Arrange
                String url = "/games?loggedInUsername=Employee1Username&category=GameCategory3";
                System.out.println(String.format("URL: %s", url));

                // Act
                ResponseEntity<String> response = client.getForEntity(url, String.class);

                // Assert
                try {
                        // Parse the response body as JSON
            org.json.JSONObject json = new org.json.JSONObject(response.getBody());
            assertEquals(404, json.getInt("status"));
            assertEquals("Not Found", json.getString("error"));
            assertEquals("No games found in this category", json.getString("message"));
        } catch (org.json.JSONException e) {
            fail("Response body is not a valid JSON");
        }

        }


        // **** GET /games{gameID} Tests ****
    @Test
    @Order(10)
    public void testGetGameByIdAsCustomerOrGuest() {
        // Arrange
        long game1Id = expectedGame1.getGameID();
        String url = String.format("/games/%d?loggedInUsername=Customer1Username", game1Id);
        System.out.println(String.format("URL: %s", url));

        // Act
        ResponseEntity<GameResponseDto> response = client.getForEntity(url, GameResponseDto.class);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        GameResponseDto actualGame = response.getBody();

        // Assert equality
        assertEquals(expectedGame1, actualGame);
    }

    @Test
    @Order(11)
    public void testGetGameByIdAsEmployeeOrOwner() {
        // Arrange
        long game2Id = expectedGame2.getGameID();
        String url = String.format("/games/%d?loggedInUsername=Employee1Username", game2Id);
        System.out.println(String.format("URL: %s", url));

        // Act
        ResponseEntity<GameResponseDto> response = client.getForEntity(url, GameResponseDto.class);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        GameResponseDto actualGame = response.getBody();

        // Assert equality
        assertEquals(expectedGame2, actualGame);
    }

}
