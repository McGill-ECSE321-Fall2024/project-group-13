package group_13.game_store.integration;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.Date;
import java.time.LocalDate;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import group_13.game_store.dto.ReviewRequestDto;
import group_13.game_store.dto.ReviewResponseDto;
import group_13.game_store.model.Customer;
import group_13.game_store.model.Game;
import group_13.game_store.model.GameCategory;
import group_13.game_store.model.GameCopy;
import group_13.game_store.model.Owner;
import group_13.game_store.model.Order;
import group_13.game_store.repository.CustomerRepository;
import group_13.game_store.repository.GameCategoryRepository;
import group_13.game_store.repository.GameCopyRepository;
import group_13.game_store.repository.GameRepository;
import group_13.game_store.repository.OrderRepository;
import group_13.game_store.repository.ReviewRepository;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestMethodOrder(OrderAnnotation.class)
@TestInstance(Lifecycle.PER_CLASS)
public class ReviewIntegrationTests {
    @Autowired
    private TestRestTemplate client;
    
    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private GameCopyRepository gameCopyRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private GameCategoryRepository gameCategoryRepository;

    @AfterAll
	public void clearDatabase() {
		reviewRepository.deleteAll();
        gameCopyRepository.deleteAll();
        orderRepository.deleteAll();
        gameRepository.deleteAll();
        customerRepository.deleteAll();
        gameCategoryRepository.deleteAll();
	}

    // Declare instance variables
    private GameCategory gameCategory1;
    private GameCategory gameCategory2;
    private GameCategory gameCategory3;
    private GameCategory gameCategory4;
    private GameCategory gameCategory5;

    private Customer customer1;
    private Customer customer2;
    private Customer customer3;
    private Customer customer4;
    private Customer customer5;
    private Owner owner;

    private Game game1;
    private Game game2;
    private Game game3;
    private Game game4;
    private Game game5;

    private Order order1;
    private GameCopy gameCopy1;



    @BeforeEach
    public void setup() {
        //Settup function to provide me with objects in my tests

        //Save a game category into our database
        gameCategory1 = new GameCategory("Shooter game in the first person", GameCategory.VisibilityStatus.Visible, "FPS");
        
        //Save a customer into our database
        customer1 = new Customer("Tim", "tim_roma", "tim@roma.ca", "tim123", "123-456-7890");

        //Save a game into our database
        game1 = new Game("Call of Duty", "Shoot 'em Up", "GameImg", 100, 80, "14+", Game.VisibilityStatus.Visible, gameCategory1);
        
        //Order to link the customer to some games
        order1 = new Order(Date.valueOf(LocalDate.now()),null, customer1);       
    
        

        // gameCategory2 = new GameCategory("Strategy game,", GameCategory.VisibilityStatus.Visible, "Strat");
        // gameCategory3 = new GameCategory("Adventure game", GameCategory.VisibilityStatus.Visible, "Adv");
        // gameCategory4 = new GameCategory("Racing game", GameCategory.VisibilityStatus.Visible, "Race");
        // gameCategory5 = new GameCategory("Puzzle game", GameCategory.VisibilityStatus.Visible, "Puz");


        // customer2 = new Customer("John", "john_doe", "john_does@mail,cou", "john123", "987-654-3210");
        // customer3 = new Customer("Jane", "jane_doe", "jane_does@mail,cou", "jane123", "456-789-0123");
        // customer4 = new Customer("Alice", "alice_wonderland", "alice@mail.com", "alice123", "789-012-3456");
        // customer5 = new Customer("Bob", "bob_builder", "bob@mail.com", "bob123", "012-345-6789");        
        
        // game2 = new Game("Age of Empires", "Build and Conquer", "GameImg", 50, 40, "10+", Game.VisibilityStatus.Visible, gameCategory2);
        // game3 = new Game("Uncharted", "Adventure", "GameImg", 60, 50, "12+", Game.VisibilityStatus.Visible, gameCategory3);
        // game4 = new Game("Need for Speed", "Racing", "GameImg", 70, 60, "10+", Game.VisibilityStatus.Visible, gameCategory4);
        // game5 = new Game("Tetris", "Puzzle", "GameImg", 20, 10, "E", Game.VisibilityStatus.Visible, gameCategory5);

    }

    @Test
    @org.junit.jupiter.api.Order(1)
    public void CreateReview() {
        gameCategoryRepository.save(gameCategory1);
        customerRepository.save(customer1);
        gameRepository.save(game1); // game1 now has a generated ID
        orderRepository.save(order1); // order1 now has a generated ID

        gameCopy1 = new GameCopy(order1, game1); // Create a game copy
        gameCopyRepository.save(gameCopy1); //Customer1 should now own the game1

        Integer savedGameId = game1.getGameID();

        // Create a review
        ReviewRequestDto reviewRequestDto = new ReviewRequestDto("Great game!", 5);

        ResponseEntity<ReviewResponseDto> response = client.postForEntity("/games/" + savedGameId + "/reviews?loggedInUsername=tim_roma", reviewRequestDto, ReviewResponseDto.class);

        // Assert
		assertNotNull(response);
		assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Great game!", response.getBody().getDescription());
        assertEquals(5, response.getBody().getScore());
        assertEquals(0, response.getBody().getLikes());
        assertEquals("tim_roma", response.getBody().getReviewerUsername());
        assertNotNull(response.getBody().getDate());

        assertEquals(LocalDate.now(), response.getBody().getDate());
    }

    // @Test
    // @org.junit.jupiter.api.Order(2)
    // public void GetReview() {
    //     // Get a review

    // }

    // @Test
    // @org.junit.jupiter.api.Order(3)
    // public void UpdateReview() {
    //     // Update a review

    // }

    // @Test
    // @org.junit.jupiter.api.Order(4)
    // public void DeleteReview() {
    //     // Delete a review

    // }

    // @Test
    // @org.junit.jupiter.api.Order(5)
    // public void GetReviews() {
    //     // Get all reviews

    // }
    
}
