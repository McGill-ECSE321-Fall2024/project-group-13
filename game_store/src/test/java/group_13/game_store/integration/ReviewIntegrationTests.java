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

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import group_13.game_store.dto.ReplyRequestDto;
import group_13.game_store.dto.ReplyResponseDto;
import group_13.game_store.dto.ReviewListResponseDto;
import group_13.game_store.dto.ReviewRequestDto;
import group_13.game_store.dto.ReviewResponseDto;
import group_13.game_store.model.Customer;
import group_13.game_store.model.Employee;
import group_13.game_store.model.Game;
import group_13.game_store.model.GameCategory;
import group_13.game_store.model.GameCopy;
import group_13.game_store.model.Reply;
import group_13.game_store.model.Review;
import group_13.game_store.model.Order;
import group_13.game_store.repository.CustomerRepository;
import group_13.game_store.repository.EmployeeRepository;
import group_13.game_store.repository.GameCategoryRepository;
import group_13.game_store.repository.GameCopyRepository;
import group_13.game_store.repository.GameRepository;
import group_13.game_store.repository.OrderRepository;
import group_13.game_store.repository.ReplyRepository;
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
    private EmployeeRepository employeeRepository;

    @Autowired
    private ReplyRepository replyRepository;

    @AfterAll
	public void clearDatabase() {
        replyRepository.deleteAll();
		reviewRepository.deleteAll();
        gameCopyRepository.deleteAll();
        orderRepository.deleteAll();
        gameRepository.deleteAll();
        customerRepository.deleteAll();
        gameCategoryRepository.deleteAll();
        employeeRepository.deleteAll();
	}

    // Declare instance variables
    private GameCategory gameCategory;

    private Customer customer1;
    private Customer customer2;
    private Customer customer3;
    private Customer customer4;

    private Employee employee1;

    private Game game1;
    private Game game2;
    private Game game3;

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
        customer4 = new Customer("Alice", "alice_wonderland", "alice@mail.com", "alice123", "789-012-3456");

        employee1 = new Employee("Tom","tom_holland", "tom@mail.com", "tom123", "123-456-7890", true);

        //Create some template games
        game1 = new Game("Call of Duty", "Shoot 'em Up", "GameImg", 100, 80, "14+", Game.VisibilityStatus.Visible, gameCategory);
        game2 = new Game("Age of Empires", "Build and Conquer", "GameImg", 50, 40, "10+", Game.VisibilityStatus.Visible, gameCategory);
        game3 = new Game("Uncharted", "Adventure", "GameImg", 60, 50, "12+", Game.VisibilityStatus.Visible, gameCategory);

        //Create some orders
        Order order1 = new Order(Date.valueOf(LocalDate.now()),null, customer1); // Order to link customer 1 to game 1     
        Order order2 = new Order(Date.valueOf(LocalDate.now()),null, customer2); // Order to link customer 2 to game 2
        Order order3 = new Order(Date.valueOf(LocalDate.now()),null, customer3); // Order to link customer 3 to game 2


        //Save the objects to the database
        gameCategoryRepository.save(gameCategory);

        customerRepository.save(customer1);
        customerRepository.save(customer2);
        customerRepository.save(customer3);
        customerRepository.save(customer4);

        employeeRepository.save(employee1);

        gameRepository.save(game1); // game1 now has a generated ID
        gameRepository.save(game2); // game2 now has a generated ID
        gameRepository.save(game3); // game3 now has a generated ID

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
    @org.junit.jupiter.api.Order(1) //Import conflicsts with order so necessary to do it like this
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

        // Verify that the review has been saved in the database
        Review savedReview = reviewRepository.findById(response.getBody().getReviewID()).get();
        assertEquals("Great game!", savedReview.getDescription());
        assertEquals(5, savedReview.getScore());
    }

    @Test
    @org.junit.jupiter.api.Order(2)
    public void testCreateReview_CustomerDoesNotOwnGame() {
        int gameId3 = game3.getGameID();

        // Create a review
        ReviewRequestDto reviewRequestDto = new ReviewRequestDto("Great game!", 5);

        ResponseEntity<String> response = client.postForEntity("/games/" + gameId3 + "/reviews?loggedInUsername=john_doe", reviewRequestDto, String.class);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertTrue(response.getBody().contains("Customer does not have the game."));

        // Verify that the review has not been saved in the database
        List<Review> reviewList = reviewRepository.findByReviewedGame_GameID(gameId3);
        assertTrue(reviewList.isEmpty());
    }

    @Test
    @org.junit.jupiter.api.Order(3)
    public void testCreateReview__UserLacksPermission() {
        int gameId3 = game3.getGameID();

        // Create a review
        ReviewRequestDto reviewRequestDto = new ReviewRequestDto("Great game!", 5);

        ResponseEntity<String> response = client.postForEntity("/games/" + gameId3 + "/reviews?loggedInUsername=guest", reviewRequestDto, String.class);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertTrue(response.getBody().contains("User does not have permission to create/update reviews."));

        // Verify that the review has not been saved in the database
        List<Review> reviewList = reviewRepository.findByReviewedGame_GameID(gameId3);
        assertTrue(reviewList.isEmpty());
    }



    @Test
    @org.junit.jupiter.api.Order(4)
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
    @org.junit.jupiter.api.Order(5)
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
    @org.junit.jupiter.api.Order(6)
    public void testUpdateReview_Success() {
        // Update a review
        ReviewRequestDto reviewRequestDto = new ReviewRequestDto("Great game! I love it!", 4);
        HttpEntity<ReviewRequestDto> requestEntity = new HttpEntity<>(reviewRequestDto);


        ResponseEntity<ReviewResponseDto> response = client.exchange(
            "/games/reviews/" + review1ID + "?loggedInUsername=john_doe",
            HttpMethod.PUT,
            requestEntity,
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

        // Verify that the review has been updated in the database
        Review updatedReview = reviewRepository.findById(review1ID).get();
        assertEquals("Great game! I love it!", updatedReview.getDescription());
        assertEquals(4, updatedReview.getScore());
    } 

    @Test
    @org.junit.jupiter.api.Order(7)
    public void testUpdateReview_UserLacksPermission() {
        // Update a review
        ReviewRequestDto reviewRequestDto = new ReviewRequestDto("Great game! I love it!", 4);
        HttpEntity<ReviewRequestDto> requestEntity = new HttpEntity<>(reviewRequestDto);

        ResponseEntity<String> response = client.exchange(
            "/games/reviews/" + review1ID + "?loggedInUsername=guest",
            HttpMethod.PUT,
            requestEntity,
            String.class
        );

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertTrue(response.getBody().contains("User does not have permission to create/update reviews."));

        // Verify that the review has not been updated in the database
        Review updatedReview = reviewRepository.findById(review1ID).get();
        assertEquals("Great game!", updatedReview.getDescription());
        assertEquals(5, updatedReview.getScore());
    }

    @Test
    @org.junit.jupiter.api.Order(8)
    public void testUpdateReview_ReviewDoesNotExist() {
        // Update a review
        ReviewRequestDto reviewRequestDto = new ReviewRequestDto("Great game! I love it!", 4);
        HttpEntity<ReviewRequestDto> requestEntity = new HttpEntity<>(reviewRequestDto);

        ResponseEntity<String> response = client.exchange(
            "/games/reviews/999?loggedInUsername=john_doe",
            HttpMethod.PUT,
            requestEntity,
            String.class
        );

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertTrue(response.getBody().contains("Review not found."));

        // Verify that the review has not been updated in the database
        Review updatedReview = reviewRepository.findById(review1ID).get();
        assertEquals("Great game!", updatedReview.getDescription());
        assertEquals(5, updatedReview.getScore());
    }

    @Test
    @org.junit.jupiter.api.Order(9)
    public void testUpdateReview_InvalidDescription() {
        // Update a review
        ReviewRequestDto reviewRequestDto = new ReviewRequestDto("", 4);
        HttpEntity<ReviewRequestDto> requestEntity = new HttpEntity<>(reviewRequestDto);

        ResponseEntity<String> response = client.exchange(
            "/games/reviews/" + review1ID + "?loggedInUsername=john_doe",
            HttpMethod.PUT,
            requestEntity,
            String.class
        );

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().contains("Description cannot be null or empty."));

        // Verify that the review has not been updated in the database
        Review updatedReview = reviewRepository.findById(review1ID).get();
        assertEquals("Great game!", updatedReview.getDescription());
        assertEquals(5, updatedReview.getScore());
    }

    @Test
    @org.junit.jupiter.api.Order(10)
    public void testAddLike_Success() {
        Review review1 = reviewRepository.findById(review1ID).get();
        assertEquals(0, review1.getLikes());

        int gameId = game2.getGameID();
        int reviewId = review1ID; // Use the review created in setup
        String loggedInUsername = "alice_wonderland";

        // Perform the POST request to like the review
        ResponseEntity<ReviewResponseDto> response = client.postForEntity(
                "/games/" + gameId + "/reviews/" + reviewId + "/likes?loggedInUsername=" + loggedInUsername,
                null,
                ReviewResponseDto.class
        );

        // Assert the response
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        // Verify that the likes count has increased
        ReviewResponseDto responseBody = response.getBody();
        assertEquals(reviewId, responseBody.getReviewID());
        assertEquals("Great game!", responseBody.getDescription());
        assertEquals(5, responseBody.getScore());
        assertEquals("john_doe", responseBody.getReviewerUsername());
        assertEquals(1, responseBody.getLikes()); // Should be 1 after the like

        // Verify that the likes count has increased in the database
        Review updatedReview = reviewRepository.findById(reviewId).get();
        assertEquals(1, updatedReview.getLikes());
    }

    @Test
    @org.junit.jupiter.api.Order(11)
    public void testAddLike_UserLacksPermission() {
        Review review1 = reviewRepository.findById(review1ID).get();
        assertEquals(0, review1.getLikes());

        int gameId = game2.getGameID();
        int reviewId = review1ID;
        String loggedInUsername = "guest"; //Guests dont have permission to like stuff

        // Perform the POST request to like the review
        ResponseEntity<String> response = client.postForEntity(
                "/games/" + gameId + "/reviews/" + reviewId + "/likes?loggedInUsername=" + loggedInUsername,
                null,
                String.class
        );

        // Assert the response
        assertNotNull(response);
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertTrue(response.getBody().contains("User does not have permission to like a review."));
    }

    @Test
    @org.junit.jupiter.api.Order(12)
    public void testAddLike_UserAlreadyLikedReview() {
        int gameId = game2.getGameID();
        int reviewId = review1ID;
        String loggedInUsername = "alice_wonderland";

        // First, like the review
        ResponseEntity<ReviewResponseDto> firstLikeResponse = client.postForEntity(
                "/games/" + gameId + "/reviews/" + reviewId + "/likes?loggedInUsername=" + loggedInUsername,
                null,
                ReviewResponseDto.class
        );

        assertEquals(HttpStatus.OK, firstLikeResponse.getStatusCode());

        // Attempt to like the same review again
        ResponseEntity<String> secondLikeResponse = client.postForEntity(
                "/games/" + gameId + "/reviews/" + reviewId + "/likes?loggedInUsername=" + loggedInUsername,
                null,
                String.class
        );

        // Assert the response
        assertNotNull(secondLikeResponse);
        assertEquals(HttpStatus.FORBIDDEN, secondLikeResponse.getStatusCode());
        assertTrue(secondLikeResponse.getBody().contains("Customer has already liked the review."));
    }

    @Test
    @org.junit.jupiter.api.Order(13)
    public void testRemoveLike_Success() {
        Review review1 = reviewRepository.findById(review1ID).get();
        assertEquals(0, review1.getLikes());

        int gameId = game2.getGameID();
        int reviewId = review1ID; // Use the review created in setup
        String loggedInUsername = "alice_wonderland";

        // Perform the POST request to like the review
        client.postForEntity(
                "/games/" + gameId + "/reviews/" + reviewId + "/likes?loggedInUsername=" + loggedInUsername,
                null,
                ReviewResponseDto.class
        );

        // Verify that the likes count has increased in the database
        Review updatedReview = reviewRepository.findById(reviewId).get();
        assertEquals(1, updatedReview.getLikes());

        // Perform the DELETE request to remove the like
        ResponseEntity<ReviewResponseDto> response2 = client.exchange(
                "/games/" + gameId + "/reviews/" + reviewId + "/likes?loggedInUsername=" + loggedInUsername,
                HttpMethod.DELETE,
                null,
                ReviewResponseDto.class
        );

        // Assert the response
        assertNotNull(response2);
        assertEquals(HttpStatus.OK, response2.getStatusCode());
        assertNotNull(response2.getBody());

        // Verify that the likes count has decreased
        ReviewResponseDto responseBody = response2.getBody();
        assertEquals(reviewId, responseBody.getReviewID());
        assertEquals("Great game!", responseBody.getDescription());
        assertEquals(5, responseBody.getScore());
        assertEquals("john_doe", responseBody.getReviewerUsername());
        assertEquals(0, responseBody.getLikes()); // Should be 0 after the like is removed

        // Verify that the likes count has decreased in the database
        Review updatedReview2 = reviewRepository.findById(reviewId).get();
        assertEquals(0, updatedReview2.getLikes());
    }

    @Test
    @org.junit.jupiter.api.Order(14)
    public void testRemoveLike_UserLacksPermission() {
        Review review1 = reviewRepository.findById(review1ID).get();
        assertEquals(0, review1.getLikes());

        int gameId = game2.getGameID();
        int reviewId = review1ID;
        String loggedInUsername = "guest"; //Guests dont have permission to like stuff

        // Perform the POST request to like the review
        client.postForEntity(
                "/games/" + gameId + "/reviews/" + reviewId + "/likes?loggedInUsername=" + "alice_wonderland",
                null,
                ReviewResponseDto.class
        );

        // Verify that the likes count has increased in the database
        Review updatedReview = reviewRepository.findById(reviewId).get();
        assertEquals(1, updatedReview.getLikes());

        // Perform the DELETE request to remove the like
        ResponseEntity<String> response2 = client.exchange(
                "/games/" + gameId + "/reviews/" + reviewId + "/likes?loggedInUsername=" + loggedInUsername,
                HttpMethod.DELETE,
                null,
                String.class
        );

        // Assert the response
        assertNotNull(response2);
        assertEquals(HttpStatus.FORBIDDEN, response2.getStatusCode());
        assertNotNull(response2.getBody());
        assertTrue(response2.getBody().contains("User does not have permission to unlike a review."));

        // Verify that the likes count has not changed in the database
        Review updatedReview2 = reviewRepository.findById(reviewId).get();
        assertEquals(1, updatedReview2.getLikes());
    }

    @Test
    @org.junit.jupiter.api.Order(15)
    public void testRemoveLike_UserHasNotLikedReview() {
        Review review1 = reviewRepository.findById(review1ID).get();
        assertEquals(0, review1.getLikes());

        int gameId = game2.getGameID();
        int reviewId = review1ID;
        String loggedInUsername = "alice_wonderland";

        // Perform the DELETE request to remove the like
        ResponseEntity<String> response = client.exchange(
                "/games/" + gameId + "/reviews/" + reviewId + "/likes?loggedInUsername=" + loggedInUsername,
                HttpMethod.DELETE,
                null,
                String.class
        );

        // Assert the response
        assertNotNull(response);
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertTrue(response.getBody().contains("Customer has not liked the review."));
    }

    @Test
    @org.junit.jupiter.api.Order(16)
    public void testReplyToReview_Success(){
        // Create a reply
        String replyContent = "Thank you for your review!";
        String loggedInUsername = "owner";
        
        ReplyRequestDto replyRequestDto = new ReplyRequestDto(replyContent);

        ResponseEntity<ReplyResponseDto> response = client.postForEntity(
            "/games/reviews/" + review1ID + "/replies?loggedInUsername=" + loggedInUsername,
            replyRequestDto,
            ReplyResponseDto.class
        );

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        assertEquals(replyContent, response.getBody().getText());
        
        // Verify that the reply has been saved in the database
        Reply savedReply = replyRepository.findByReview_ReviewID(review1ID);
        assertNotNull(savedReply);
        assertEquals(replyContent, savedReply.getText());
    }

    @Test
    @org.junit.jupiter.api.Order(17)
    public void testReplyToReview_UserLacksPermission(){
        // Create a reply
        String replyContent = "Thank you for your review!";
        String loggedInUsername = "guest";

        ReplyRequestDto replyRequestDto = new ReplyRequestDto(replyContent);
        
        ResponseEntity<String> response = client.postForEntity(
            "/games/reviews/" + review1ID + "/replies?loggedInUsername=" + loggedInUsername,
            replyRequestDto,
            String.class
        );

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertTrue(response.getBody().contains("User does not have permission to reply to reviews."));
    }

    @Test
    @org.junit.jupiter.api.Order(18)
    public void testGetReplyToReview_Success(){
        // Create a reply
        String replyContent = "Thank you for your review!";
        String loggedInUsername = "owner";
        
        ReplyRequestDto replyRequestDto = new ReplyRequestDto(replyContent);

        ResponseEntity<ReplyResponseDto> response = client.postForEntity(
            "/games/reviews/" + review1ID + "/replies?loggedInUsername=" + loggedInUsername,
            replyRequestDto,
            ReplyResponseDto.class
        );

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        // Get a reply by its review ID
        ResponseEntity<ReplyResponseDto> response2 = client.getForEntity(
            "/games/reviews/" + review1ID + "/replies",
            ReplyResponseDto.class
        );

        // Assert
        assertNotNull(response2);
        assertEquals(HttpStatus.OK, response2.getStatusCode());
        assertNotNull(response2.getBody());

        assertEquals("Thank you for your review!", response2.getBody().getText());
    }

    @Test
    @org.junit.jupiter.api.Order(18)
    public void testGetReplyToReview_ReplyNotFound(){
        // Get a reply by its review ID
        ResponseEntity<String> response = client.getForEntity(
            "/games/reviews/" + review1ID + "/replies",
            String.class
        );

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().contains("No reply found for review with ID " + review1ID));
    }

    @Test
    @org.junit.jupiter.api.Order(19)
    public void testGetAllReviewsNoPendingReply_Success() {
        //Get rid of all the reviews and replies so we can accurately test the get all reviews endpoint
        replyRepository.deleteAll();
		reviewRepository.deleteAll();

        //Repopulate the database with 2 reviews
        setup();

        String loggedInUsername = "owner"; //Only owner can see all reviews
        boolean isPendingReply = false; //Not looking for reviews with pending replies just all reviews in general

        ResponseEntity<ReviewListResponseDto> response = client.getForEntity(
            "/games/reviews?isPendingReply=" + isPendingReply + "&loggedInUsername=" + loggedInUsername,
            ReviewListResponseDto.class
        );

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        ReviewListResponseDto responseBody = response.getBody();
        List<ReviewResponseDto> reviews = responseBody.getReviews();

        assertNotNull(reviews);
        assertEquals(2, reviews.size()); // Expecting 2 reviews from setup

        // Check that both reviews are present
        boolean foundReview1 = false;
        boolean foundReview2 = false;
        for (ReviewResponseDto review : reviews) {
            if (review.getReviewID() == review1ID) {
                assertEquals("Great game!", review.getDescription());
                assertEquals(5, review.getScore());
                assertEquals("john_doe", review.getReviewerUsername());
                foundReview1 = true;
            } else if (review.getReviewID() == review2ID) {
                assertEquals("Bad game.", review.getDescription());
                assertEquals(1, review.getScore());
                assertEquals("jane_doe", review.getReviewerUsername());
                foundReview2 = true;
            }
        }
        assertTrue(foundReview1, "Review 1 should be in the list");
        assertTrue(foundReview2, "Review 2 should be in the list");
    }

    @Test
    @org.junit.jupiter.api.Order(20)
    public void testGetAllReviewsWithPendingReply_Success() {
        //Get rid of all the reviews and replies so we can accurately test the get all reviews endpoint
        replyRepository.deleteAll();
        reviewRepository.deleteAll();

        //Repopulate the database with 2 reviews
        setup();

        // Create a reply that we will write to review 1 so only review 2 is pending reply
        String replyContent = "Thank you for your review!";
        String loggedInUsername = "owner";
        
        ReplyRequestDto replyRequestDto = new ReplyRequestDto(replyContent);

        // Create a reply for review 1
        ResponseEntity<ReplyResponseDto> response = client.postForEntity(
            "/games/reviews/" + review1ID + "/replies?loggedInUsername=" + loggedInUsername,
            replyRequestDto,
            ReplyResponseDto.class
        );

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        boolean isPendingReply = true; //Looking for reviews with pending replies only

        ResponseEntity<ReviewListResponseDto> responseForReviews = client.getForEntity(
            "/games/reviews?isPendingReply=" + isPendingReply + "&loggedInUsername=" + loggedInUsername,
            ReviewListResponseDto.class
        );

        // Assert
        assertNotNull(responseForReviews);
        assertEquals(HttpStatus.OK, responseForReviews.getStatusCode());
        assertNotNull(responseForReviews.getBody());

        ReviewListResponseDto responseBody = responseForReviews.getBody();
        List<ReviewResponseDto> reviews = responseBody.getReviews();

        assertNotNull(reviews);
        assertEquals(1, reviews.size()); // Expecting 2 reviews from setup

        Review expectedReview = reviewRepository.findByReviewID(review2ID);

        assertEquals(expectedReview.getDescription(), reviews.get(0).getDescription());
        assertEquals(expectedReview.getScore(), reviews.get(0).getScore());

        assertNotNull(expectedReview.getReviewer());
        assertEquals(expectedReview.getReviewer().getUsername(), reviews.get(0).getReviewerUsername());
    }

    @Test
    @org.junit.jupiter.api.Order(21)
    public void testGetAllReviews_UserLacksPermission() {
        String loggedInUsername = "guest"; //Only owner can see all reviews
        boolean isPendingReply = false; //Not looking for reviews with pending replies just all reviews in general

        ResponseEntity<String> response = client.getForEntity(
            "/games/reviews?isPendingReply=" + isPendingReply + "&loggedInUsername=" + loggedInUsername,
            String.class
        );

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertTrue(response.getBody().contains("User does not have permission to view reviews."));
    }

    @Test
    @org.junit.jupiter.api.Order(22)
    public void testHasLikedReview() {
        // Arrange
        String username = "tim_roma";
        int gameId = game1.getGameID();
        int reviewId = review1ID;

        // Act
        ResponseEntity<String> response = client.getForEntity(
            "/games/" + gameId + "/reviews/" + reviewId + "/likes?loggedInUsername=" + username,
            String.class
        );

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        assertEquals("false", response.getBody());

        // Like the review
        ResponseEntity<String> likeResponse = client.postForEntity(
            "/games/" + gameId + "/reviews/" + reviewId + "/likes?loggedInUsername=" + username,
            null,
            String.class
        );

        // Assert
        assertNotNull(likeResponse);
        assertEquals(HttpStatus.OK, likeResponse.getStatusCode());

        // Check if the user has liked the review
        ResponseEntity<String> response2 = client.getForEntity(
            "/games/" + gameId + "/reviews/" + reviewId + "/likes?loggedInUsername=" + username,
            String.class
        );

        // Assert
        assertNotNull(response2);
        assertEquals(HttpStatus.OK, response2.getStatusCode());
        assertNotNull(response2.getBody());

        assertEquals("true", response2.getBody());
    }

    
}
