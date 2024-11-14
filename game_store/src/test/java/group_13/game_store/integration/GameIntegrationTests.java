package group_13.game_store.integration;

import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertNotNull;
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
import group_13.game_store.dto.GameRequestDto;
import group_13.game_store.dto.GameResponseDto;
import group_13.game_store.model.Customer;
import group_13.game_store.model.Employee;
import group_13.game_store.repository.EmployeeRepository;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpEntity;

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

    private GameCategory gameCategory1;
    private Game game1;
    private Game game2;
    private Game game3;
    private Game game4;
    private Game game5;
    private Game game6;
    private Game game7;

    @BeforeAll
    public void setup() {

        // Clear the databases just in case
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

        // Game 2: Out of stock and visible
        game2 = new Game("Game 2", "Description 2", "Image 2", 0, 19.99, "18+", Game.VisibilityStatus.Visible,
                gameCategory1);
        gameRepo.save(game2);

        // Game 3: In stock and archived
        game3 = new Game("Game 3", "Description 3", "Image 3", 5, 29.99, "18+", Game.VisibilityStatus.Archived,
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
                game2.getCategory().getCategoryID(),
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
                game3.getCategory().getCategoryID(),
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
                game4.getCategory().getCategoryID(),
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
                game5.getCategory().getCategoryID(),
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
                game6.getCategory().getCategoryID(),
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
                game7.getCategory().getCategoryID(),
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

    // **** GET /games Tests (Gets All Games Depending On Permission and Filters) ****
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


        // **** GET /games{gameID} Tests (Gets Game By Id and By Permission Level) ****
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

    @Order(12)
        public void testGetGameByIdGameNotFound() {
                // Arrange
                long gameID = 1000;
                String url = String.format("/games/%d?loggedInUsername=Employee1Username", gameID);
                System.out.println(String.format("URL: %s", url));
        
                // Act
                ResponseEntity<String> response = client.getForEntity(url, String.class);

                // Assert
                try {
                        // Parse the response body as JSON
                org.json.JSONObject json = new org.json.JSONObject(response.getBody());
                assertEquals(404, json.getInt("status"));
                assertEquals("Not Found", json.getString("error"));
                assertEquals("Game not found", json.getString("message"));
                } catch (org.json.JSONException e) {
                        fail("Response body is not a valid JSON");
                }

        }

                // **** POST /games Tests (Creates a game only Owner) ****

        @Test
        @Order(13) 
        public void testAddGameAsAnOwner() {
                // Arrange
                String url = "/games?loggedInUsername=owner";
                GameRequestDto gameToAdd = new GameRequestDto(
                        "Game 8",
                        "Description 8",
                        "Image 8",
                        10,
                        9.99,
                        "18+",
                        "Visible",
                        gameCategory1.getCategoryID()
                        
                );

                // Act
                ResponseEntity<GameResponseDto> response = client.postForEntity(url, gameToAdd, GameResponseDto.class);

                // Assert
                assertNotNull(response);
                assertEquals(HttpStatus.OK, response.getStatusCode());
                assertNotNull(response.getBody());
                GameResponseDto actualGame = response.getBody();

                // Assert equality
                assertEquals(gameToAdd.getTitle(), actualGame.getTitle());
                assertEquals(gameToAdd.getDescription(), actualGame.getDescription());
                assertEquals(gameToAdd.getImg(), actualGame.getImg());
                assertEquals(gameToAdd.getStock(), actualGame.getStock());
                assertEquals(gameToAdd.getPrice(), actualGame.getPrice());
                assertEquals(gameToAdd.getParentalRating(), actualGame.getParentalRating());
                assertEquals(gameToAdd.getStatus(), actualGame.getStatus());
                assertEquals(gameToAdd.getCategoryId(), actualGame.getCategoryId());

        }

        @Test
        @Order(14) 
        public void testAddGamePermissionDenied() {
                // Arrange
                String url = "/games?loggedInUsername=Customer1Username";
                GameRequestDto gameToAdd = new GameRequestDto(
                        "Game 9",
                        "Description 9",
                        "Image 9",
                        10,
                        9.99,
                        "18+",
                        "Visible",
                        gameCategory1.getCategoryID()
                        
                );

                // Act
                ResponseEntity<String> response = client.postForEntity(url, gameToAdd, String.class);

                // Assert
                try {
                        // Parse the response body as JSON
                org.json.JSONObject json = new org.json.JSONObject(response.getBody());
                assertEquals(403, json.getInt("status"));
                assertEquals("Forbidden", json.getString("error"));
                assertEquals("You do not have permission to add games.", json.getString("message"));
                } catch (org.json.JSONException e) {
                        fail("Response body is not a valid JSON");
                }

        }

        @Test
        @Order(15)
        public void testAddGameInvalidCategory() {
                // Arrange
                String url = "/games?loggedInUsername=owner";
                GameRequestDto gameToAdd = new GameRequestDto(
                        "Game 10",
                        "Description 10",
                        "Image 10",
                        10,
                        9.99,
                        "18+",
                        "Visible",
                        1000
                );

                // Act
                ResponseEntity<String> response = client.postForEntity(url, gameToAdd, String.class);

                // Assert
                try {
                        // Parse the response body as JSON
                org.json.JSONObject json = new org.json.JSONObject(response.getBody());
                assertEquals(404, json.getInt("status"));
                assertEquals("Not Found", json.getString("error"));
                assertEquals("Invalid category ID.", json.getString("message"));
                } catch (org.json.JSONException e) {
                        fail("Response body is not a valid JSON");
                }

        }

        // **** PUT /games{gameID} Tests (Updates a game only Owner) ****

        @Test
        @Order(16)
        public void testUpdateGameAsOwner() {
                // Arrange
                int gameID = game1.getGameID();
                String url = String.format("/games/%d?loggedInUsername=owner", gameID);
                GameRequestDto gameToUpdate = new GameRequestDto(
                        "Game 1 Updated",
                        "Description 1 Updated",
                        "Image 1 Updated",
                        20,
                        19.99,
                        "18+",
                        "Visible",
                        gameCategory1.getCategoryID()
                );

               // Set up the request entity with headers
               HttpHeaders headers = new HttpHeaders();
               headers.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);
               HttpEntity<GameRequestDto> requestEntity = new HttpEntity<>(gameToUpdate, headers);

                // Act --> PUT request does not return a response body
                ResponseEntity<GameResponseDto> response = client.exchange(url, org.springframework.http.HttpMethod.PUT, requestEntity, GameResponseDto.class);

                // Assert (compare ressponse dto with updated game) FRONTEND
                assertEquals(HttpStatus.OK, response.getStatusCode());
                GameResponseDto updatedGame = response.getBody();
                assertNotNull(updatedGame);
                assertNotNull(response.getBody());
                assertEquals(gameToUpdate.getTitle(), response.getBody().getTitle());
                assertEquals(gameToUpdate.getDescription(), response.getBody().getDescription());
                assertEquals(gameToUpdate.getImg(), response.getBody().getImg());
                assertEquals(gameToUpdate.getStock(), response.getBody().getStock());
                assertEquals(gameToUpdate.getPrice(), response.getBody().getPrice());
                assertEquals(gameToUpdate.getParentalRating(), response.getBody().getParentalRating());
                assertEquals(gameToUpdate.getStatus(), response.getBody().getStatus());
                assertEquals(gameToUpdate.getCategoryId(), response.getBody().getCategoryId());

                // Assert (compare updated game with game in the database) BACKEND
                Game updatedGameInDB = gameRepo.findById(gameID).get();
                assertEquals(gameToUpdate.getTitle(), updatedGameInDB.getTitle());
                assertEquals(gameToUpdate.getDescription(), updatedGameInDB.getDescription());
                assertEquals(gameToUpdate.getImg(), updatedGameInDB.getImg());
                assertEquals(gameToUpdate.getStock(), updatedGameInDB.getStock());
                assertEquals(gameToUpdate.getPrice(), updatedGameInDB.getPrice());
                assertEquals(gameToUpdate.getParentalRating(), updatedGameInDB.getParentalRating());
                assertEquals(gameToUpdate.getStatus(), updatedGameInDB.getStatus().toString());
                assertEquals(gameToUpdate.getCategoryId(), updatedGameInDB.getCategory().getCategoryID());

        }

        @Test
        @Order(17)
        public void testUpdateGamePermissionDenied() {
                // Arrange
                int gameID = game1.getGameID();
                String url = String.format("/games/%d?loggedInUsername=Customer1Username", gameID);
                GameRequestDto gameToUpdate = new GameRequestDto(
                        "Game 1 Updated",
                        "Description 1 Updated",
                        "Image 1 Updated",
                        20,
                        19.99,
                        "18+",
                        "Visible",
                        gameCategory1.getCategoryID()
                );

               // Set up the request entity with headers
               HttpHeaders headers = new HttpHeaders();
               headers.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);
               HttpEntity<GameRequestDto> requestEntity = new HttpEntity<>(gameToUpdate, headers);

                // Act
                ResponseEntity<String> response = client.exchange(url, org.springframework.http.HttpMethod.PUT, requestEntity, String.class);

                // Assert
                try {
                        // Parse the response body as JSON
                org.json.JSONObject json = new org.json.JSONObject(response.getBody());
                assertEquals(403, json.getInt("status"));
                assertEquals("Forbidden", json.getString("error"));
                assertEquals("You do not have permission to update games.", json.getString("message"));
                } catch (org.json.JSONException e) {
                        fail("Response body is not a valid JSON");
                }

        }

        @Test
        @Order(18)
        public void testUpdateGamePermissionInvalidId() {
                // Arrange
                int gameID = 1000;
                String url = String.format("/games/%d?loggedInUsername=owner", gameID);
                GameRequestDto gameToUpdate = new GameRequestDto(
                        "Game 1 Updated",
                        "Description 1 Updated",
                        "Image 1 Updated",
                        20,
                        19.99,
                        "18+",
                        "Visible",
                        gameCategory1.getCategoryID()
                );

               // Set up the request entity with headers
               HttpHeaders headers = new HttpHeaders();
               headers.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);
               HttpEntity<GameRequestDto> requestEntity = new HttpEntity<>(gameToUpdate, headers);

                // Act
                ResponseEntity<String> response = client.exchange(url, org.springframework.http.HttpMethod.PUT, requestEntity, String.class);

                // Assert
                try {
                        // Parse the response body as JSON
                org.json.JSONObject json = new org.json.JSONObject(response.getBody());
                assertEquals(404, json.getInt("status"));
                assertEquals("Not Found", json.getString("error"));
                assertEquals("Game with ID 1000 not found.", json.getString("message"));
                } catch (org.json.JSONException e) {
                        fail("Response body is not a valid JSON");
                }
        }

        // **** DELETE /games{gameID} Tests Archives or Requests an Archive for a Game ****

        @Test
        @Order(19)
        public void testArchiveGameAsOwner() {
                // Arrange
                int gameID = game1.getGameID();
                String url = String.format("/games/%d?loggedInUsername=owner", gameID);
                System.out.println(String.format("URL: %s", url));

                // Act
                ResponseEntity<Void> response = client.exchange(url, org.springframework.http.HttpMethod.DELETE, null, Void.class);

                // Compare the request to the database
                Game archivedGame = gameRepo.findByGameID(gameID);
                assertEquals(Game.VisibilityStatus.Archived, archivedGame.getStatus());
                assertEquals(gameID, archivedGame.getGameID());
        }

        @Test
        @Order(20)
        public void testArchiveGameAsEmployee() {
                // Arrange
                int gameID = game2.getGameID();
                String url = String.format("/games/%d?loggedInUsername=Employee1Username", gameID);
                System.out.println(String.format("URL: %s", url));

                // Act
                ResponseEntity<Void> response = client.exchange(url, org.springframework.http.HttpMethod.DELETE, null, Void.class);

                // Compare the request to the database
                Game archivedGame = gameRepo.findByGameID(gameID);
                assertEquals(Game.VisibilityStatus.PendingArchive, archivedGame.getStatus());
                assertEquals(gameID, archivedGame.getGameID());
        }

        @Test
        @Order(21)
        public void testArchiveGamePermissionDenied() {
                // Arrange
                int gameID = game1.getGameID();
                String url = String.format("/games/%d?loggedInUsername=Customer1Username", gameID);
                System.out.println(String.format("URL: %s", url));

                // Act
                ResponseEntity<String> response = client.exchange(url, org.springframework.http.HttpMethod.DELETE, null, String.class);

                // Assert
                try {
                        // Parse the response body as JSON
                org.json.JSONObject json = new org.json.JSONObject(response.getBody());
                assertEquals(403, json.getInt("status"));
                assertEquals("Forbidden", json.getString("error"));
                assertEquals("You do not have permission to archive games.", json.getString("message"));
                } catch (org.json.JSONException e) {
                        fail("Response body is not a valid JSON");
                }
        }

        @Test
        @Order(22)
        public void testArchiveGameThatDoesNotExist() {
                // Arrange
                int gameID = 1000;
                String url = String.format("/games/%d?loggedInUsername=owner", gameID);
                System.out.println(String.format("URL: %s", url));

                // Act
                ResponseEntity<String> response = client.exchange(url, org.springframework.http.HttpMethod.DELETE, null, String.class);

                // Assert
                try {
                        // Parse the response body as JSON
                org.json.JSONObject json = new org.json.JSONObject(response.getBody());
                assertEquals(404, json.getInt("status"));
                assertEquals("Not Found", json.getString("error"));
                assertEquals("Game with ID 1000 not found.", json.getString("message"));
                } catch (org.json.JSONException e) {
                        fail("Response body is not a valid JSON");
                }
        }


        // **** GET /games/archive-requests Tests (Gets All Games with Pending Archive Status) only owner ****
        @Test
        @Order(23)
        public void testGetAllGamesWithPendingArchiveAsOwner() {
                // Arrange
                String url = "/games/archive-requests?loggedInUsername=owner";
                System.out.println(String.format("URL: %s", url));

                // set all the games to pending archive except game 1 and game 2
                game1.setStatus(Game.VisibilityStatus.Visible);
                game2.setStatus(Game.VisibilityStatus.Archived);
                game3.setStatus(Game.VisibilityStatus.PendingArchive);
                game4.setStatus(Game.VisibilityStatus.PendingArchive);
                game5.setStatus(Game.VisibilityStatus.PendingArchive);
                game6.setStatus(Game.VisibilityStatus.PendingArchive);
                game7.setStatus(Game.VisibilityStatus.PendingArchive);

                // update the games in the database
                gameRepo.save(game1);
                gameRepo.save(game2);
                gameRepo.save(game3);
                gameRepo.save(game4);
                gameRepo.save(game5);
                gameRepo.save(game6);
                gameRepo.save(game7);                

                // Act
                ResponseEntity<GameListResponseDto> response = client.getForEntity(url, GameListResponseDto.class);

                // Assert
                assertNotNull(response);
                assertEquals(HttpStatus.OK, response.getStatusCode());
                assertNotNull(response.getBody());
                List<GameResponseDto> actualGames = response.getBody().getGames();
                assertNotNull(actualGames);
                assertEquals(5, actualGames.size());

                // Assert the status of the games
                for (GameResponseDto game : actualGames) {
                        assertEquals(Game.VisibilityStatus.PendingArchive.toString(), game.getStatus());
                }

                // ASsert the game ids
                List<Integer> gameIds = Arrays.asList(game3.getGameID(), game4.getGameID(), game5.getGameID(), game6.getGameID(), game7.getGameID());
                for (GameResponseDto game : actualGames) {
                        assertTrue(gameIds.contains(game.getGameID()));
                }
        }

        @Test
        @Order(24)
        public void testGetAllGamesWithPendingArchivePermissionDenied() {
                // Arrange
                String url = "/games/archive-requests?loggedInUsername=Employee1Username";
                System.out.println(String.format("URL: %s", url));

                // Act
                ResponseEntity<String> response = client.getForEntity(url, String.class);

                // Assert
                try {
                        // Parse the response body as JSON
                org.json.JSONObject json = new org.json.JSONObject(response.getBody());
                assertEquals(403, json.getInt("status"));
                assertEquals("Forbidden", json.getString("error"));
                assertEquals("You do not have permission to view pending archive requests.", json.getString("message"));
                } catch (org.json.JSONException e) {
                        fail("Response body is not a valid JSON");
                }
        }


}
