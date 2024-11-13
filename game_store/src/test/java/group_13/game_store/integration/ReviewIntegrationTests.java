package group_13.game_store.integration;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

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

import group_13.game_store.dto.ReviewListResponseDto;
import group_13.game_store.dto.ReviewRequestDto;
import group_13.game_store.dto.ReviewResponseDto;
import group_13.game_store.model.Customer;
import group_13.game_store.model.Game;
import group_13.game_store.model.GameCategory;
import group_13.game_store.model.GameCopy;
import group_13.game_store.model.Owner;
import group_13.game_store.model.Reply;
import group_13.game_store.model.Review;
import group_13.game_store.model.Order;
import group_13.game_store.repository.CustomerRepository;
import group_13.game_store.repository.GameCategoryRepository;
import group_13.game_store.repository.GameCopyRepository;
import group_13.game_store.repository.GameRepository;
import group_13.game_store.repository.OrderRepository;
import group_13.game_store.repository.OwnerRepository;
import group_13.game_store.repository.ReviewRepository;
import group_13.game_store.service.ReviewService;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestMethodOrder(OrderAnnotation.class)
@TestInstance(Lifecycle.PER_CLASS)
public class ReviewIntegrationTests {
    @Autowired
    private TestRestTemplate client;
    
    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private ReviewService reviewService;

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

    @Autowired
    private OwnerRepository ownerRepository;

    @AfterAll
	public void clearDatabase() {
		reviewRepository.deleteAll();
        gameCopyRepository.deleteAll();
        orderRepository.deleteAll();
        gameRepository.deleteAll();
        customerRepository.deleteAll();
        gameCategoryRepository.deleteAll();
        ownerRepository.deleteAll();
	}

    // Declare instance variables
    private GameCategory gameCategory;

    private Customer customer1;
    private Customer customer2;
    private Customer customer3;
    private Customer customer4;
    private Customer customer5;

    private Game game1;
    private Game game2;
    private Game game3;
    private Game game4;
    private Game game5;

    private int review1ID;
    private int review2ID;


    @BeforeEach
    public void setup() {
        //Settup function to provide me with objects in my tests

        //Create a template game categort
        gameCategory = new GameCategory("Shooter game in the first person", GameCategory.VisibilityStatus.Visible, "FPS");
        
        //Create some template customers
        customer1 = new Customer("Tim", "tim_roma", "tim@roma.ca", "tim123", "123-456-7890");
        customer2 = new Customer("John", "john_doe", "john_does@mail,cou", "john123", "987-654-3210");
        customer3 = new Customer("Jane", "jane_doe", "jane_does@mail,cou", "jane123", "456-789-0123");

        Owner owner = new Owner("bob", "owner", "owner@mail.com,", "owner123", "123-456-7890");
        ownerRepository.save(owner);

        //Create some template games
        game1 = new Game("Call of Duty", "Shoot 'em Up", "GameImg", 100, 80, "14+", Game.VisibilityStatus.Visible, gameCategory);
        game2 = new Game("Age of Empires", "Build and Conquer", "GameImg", 50, 40, "10+", Game.VisibilityStatus.Visible, gameCategory);


        //Create some orders
        Order order1 = new Order(Date.valueOf(LocalDate.now()),null, customer1); // Order to link customer 1 to game 1     
        Order order2 = new Order(Date.valueOf(LocalDate.now()),null, customer2); // Order to link customer 2 to game 2
        Order order3 = new Order(Date.valueOf(LocalDate.now()),null, customer3); // Order to link customer 3 to game 2


        //Save the objects to the database
        gameCategoryRepository.save(gameCategory);

        customerRepository.save(customer1);
        customerRepository.save(customer2);
        customerRepository.save(customer3);

        gameRepository.save(game1); // game1 now has a generated ID
        gameRepository.save(game2); // game2 now has a generated ID

        orderRepository.save(order1);
        orderRepository.save(order2); 
        orderRepository.save(order3);


        GameCopy gameCopy1 = new GameCopy(order1, game1); // Game copy to link customer 1 to game 1
        GameCopy gameCopy2 = new GameCopy(order2, game2); // Game copy to link customer 2 to game 2
        GameCopy gameCopy3 = new GameCopy(order3, game2); // Game copy to link customer 3 to game 2


        gameCopyRepository.save(gameCopy1); //Customer1 should now own the game1
        gameCopyRepository.save(gameCopy2); //Customer2 should now own the game2
        gameCopyRepository.save(gameCopy3); //Customer3 should now own the game2

        Integer Game2Id = game2.getGameID();

        // Create a review for game 2 by customer 2
        Review review1 = reviewService.createReview("Great game!", 5, "john_doe", Game2Id);
        review1ID = review1.getReviewID();

        // Create a review for game 2 by customer 3
        Review review2 = reviewService.createReview("Bad game.", 1, "jane_doe", Game2Id);
        review2ID = review2.getReviewID();
    }

    @Test
    @org.junit.jupiter.api.Order(1)
    public void testCreateReview_Success() {
        int gameId1 = game1.getGameID();

        // Create a review
        ReviewRequestDto reviewRequestDto = new ReviewRequestDto("Great game!", 5);

        ResponseEntity<ReviewResponseDto> response = client.postForEntity("/games/" + gameId1 + "/reviews?loggedInUsername=tim_roma", reviewRequestDto, ReviewResponseDto.class);

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



    @Test
    @org.junit.jupiter.api.Order(2)
    public void testGetReviewByID_Success() {
        // Get a review by its ID
        ResponseEntity<ReviewResponseDto> response = client.getForEntity("/games/reviews/" + review1ID, ReviewResponseDto.class);
        
        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        assertEquals("Great game!", response.getBody().getDescription());
        assertEquals(5, response.getBody().getScore());
        assertEquals(0, response.getBody().getLikes());
        assertEquals("john_doe", response.getBody().getReviewerUsername());
        assertNotNull(response.getBody().getDate());

        assertEquals(LocalDate.now(), response.getBody().getDate());
    }



    @Test
    @org.junit.jupiter.api.Order(3)
    public void testGetReviewsByGame_Success() {
        Integer savedGameId = game2.getGameID();

        // Now perform the GET request to get all reviews
        ResponseEntity<ReviewListResponseDto> response = client.getForEntity(
            "/games/" + savedGameId + "/reviews",
            ReviewListResponseDto.class
        );

        // Assert
        assertNotNull(response, "Response should not be null");
        assertEquals(HttpStatus.OK, response.getStatusCode(), "HTTP status should be 200 OK");
        assertNotNull(response.getBody(), "Response body should not be null");

        ReviewListResponseDto responseBody = response.getBody();
        List<ReviewResponseDto> reviews = responseBody.getReviews();

        assertNotNull(reviews, "Reviews list should not be null");
        assertFalse(reviews.isEmpty(), "Reviews list should not be empty");

        // Additional assertions to verify the content
        ReviewResponseDto review = reviews.get(0);
        assertEquals("Great game!", review.getDescription(), "Review description should match");
        assertEquals(5, review.getScore(), "Review score should be 5");
        assertEquals("john_doe", review.getReviewerUsername(), "Reviewer username should be 'tim_roma'");

        ReviewResponseDto review2 = reviews.get(1);
        assertEquals("Bad game.", review2.getDescription(), "Review description should match");
        assertEquals(1, review2.getScore(), "Review score should be 1");
        assertEquals("jane_doe", review2.getReviewerUsername(), "Reviewer username should be 'tim_roma'");
    }

    @Test 
    @org.junit.jupiter.api.Order(4)
    public void testUpdateReview_Success() {
        // Update a review
        ReviewRequestDto reviewRequestDto = new ReviewRequestDto("Great game! I love it!", 4);

        ResponseEntity<ReviewResponseDto> response = client.postForEntity(
            "/games/reviews/" + review1ID + "?loggedInUsername=john_doe",
            reviewRequestDto,
            ReviewResponseDto.class
        );

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Great game! I love it!", response.getBody().getDescription());
        assertEquals(4, response.getBody().getScore());
        assertEquals("john_doe", response.getBody().getReviewerUsername());
        assertNotNull(response.getBody().getDate());
        assertEquals(LocalDate.now(), response.getBody().getDate());
    } 

    // @Test
    // @org.junit.jupiter.api.Order(4)
    // public void testGetReviewsEndpoint() {
    //     // Save entities
    //     gameCategoryRepository.save(gameCategory1);
    //     customerRepository.save(customer1);
    //     gameRepository.save(game1);
    //     orderRepository.save(order1);

    //     // Create and save GameCopy
    //     gameCopy1 = new GameCopy(order1, game1);
    //     gameCopyRepository.save(gameCopy1);

    //     // Create reviews
    //     ReviewRequestDto reviewRequestDto1 = new ReviewRequestDto("Great game!", 5);
    //     ResponseEntity<ReviewResponseDto> response1 = client.postForEntity(
    //         "/games/" + game1.getGameID() + "/reviews?loggedInUsername=" + customer1.getUsername(),
    //         reviewRequestDto1,
    //         ReviewResponseDto.class
    //     );

    //     assertEquals(HttpStatus.OK, response1.getStatusCode());
    //     assertNotNull(response1.getBody());
    //     Integer reviewId1 = response1.getBody().getReviewID();

    //     // Create another review
    //     ReviewRequestDto reviewRequestDto2 = new ReviewRequestDto("Needs improvement.", 3);
    //     ResponseEntity<ReviewResponseDto> response2 = client.postForEntity(
    //         "/games/" + game1.getGameID() + "/reviews?loggedInUsername=" + customer1.getUsername(),
    //         reviewRequestDto2,
    //         ReviewResponseDto.class
    //     );

    //     assertEquals(HttpStatus.OK, response2.getStatusCode());
    //     assertNotNull(response2.getBody());
    //     Integer reviewId2 = response2.getBody().getReviewID();

    //     // Add a reply to the first review
    //     Optional<Review> optionalReview1 = reviewRepository.findById(reviewId1);
    //     assertTrue(optionalReview1.isPresent());
    //     Review review1 = optionalReview1.get();

    //     // Test getting all reviews as admin
    //     ResponseEntity<ReviewListResponseDto> responseAllReviews = client.getForEntity(
    //         "/games/reviews?isPendingReply=false&loggedInUsername=owner",
    //         ReviewListResponseDto.class
    //     );

    //     // Assert
    //     assertNotNull(responseAllReviews);
    //     assertEquals(HttpStatus.OK, responseAllReviews.getStatusCode());
    //     assertNotNull(responseAllReviews.getBody());
    //     List<ReviewResponseDto> allReviews = responseAllReviews.getBody().getReviews();
    //     assertNotNull(allReviews);
    //     assertEquals(4, allReviews.size(), "Should return all reviews");

    //     // Test getting unanswered reviews as admin
    //     ResponseEntity<ReviewListResponseDto> responsePendingReviews = client.getForEntity(
    //         "/games/reviews?isPendingReply=true&loggedInUsername=owner",
    //         ReviewListResponseDto.class
    //     );

    //     // Assert
    //     assertNotNull(responsePendingReviews);
    //     assertEquals(HttpStatus.OK, responsePendingReviews.getStatusCode());
    //     assertNotNull(responsePendingReviews.getBody());
    //     List<ReviewResponseDto> pendingReviews = responsePendingReviews.getBody().getReviews();
    //     assertNotNull(pendingReviews);
    //     assertEquals(1, pendingReviews.size(), "Should return only unanswered reviews");

    //     // Verify that the unanswered review is the one without a reply
    //     ReviewResponseDto pendingReview = pendingReviews.get(0);
    //     assertEquals("Needs improvement.", pendingReview.getDescription());

    //     // Test unauthorized access by a customer
    //     ResponseEntity<String> unauthorizedResponse = client.getForEntity(
    //         "/games/reviews?isPendingReply=false&loggedInUsername=" + customer1.getUsername(),
    //         String.class
    //     );

    //     // Since the controller throws IllegalArgumentException, it will result in HTTP 400 Bad Request
    //     assertEquals(HttpStatus.BAD_REQUEST, unauthorizedResponse.getStatusCode());
    //     assertTrue(unauthorizedResponse.getBody().contains("User does not have permission to see all reviews."));
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
