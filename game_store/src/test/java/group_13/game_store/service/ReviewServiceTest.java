package group_13.game_store.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.ArgumentCaptor;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import group_13.game_store.model.Customer;
import group_13.game_store.model.Game;
import group_13.game_store.model.GameCategory;
import group_13.game_store.model.Promotion;
import group_13.game_store.model.Review;
import group_13.game_store.model.ReviewLike;
import group_13.game_store.model.ReviewLike;
import group_13.game_store.repository.CustomerRepository;
import group_13.game_store.repository.GameCategoryRepository;
import group_13.game_store.repository.GameRepository;
import group_13.game_store.repository.ReviewLikeRepository;
import group_13.game_store.repository.ReviewRepository;

@SpringBootTest
public class ReviewServiceTest {
    @InjectMocks
    private ReviewService reviewService;

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private CustomerRepository customerRepo;

    @Mock
    private GameRepository gameRepository;

    @Mock
    private ReviewLikeRepository reviewLikeRepository;

    @Mock GameCategoryRepository gameCategoryRepository;

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

    private Game game1;
    private Game game2;
    private Game game3;
    private Game game4;
    private Game game5;

    @BeforeEach
    public void setup() {
        //Settup function to provide me with objects in my tests
        gameCategory1 = new GameCategory("Shooter game in the first person", GameCategory.VisibilityStatus.Visible, "FPS");
        gameCategory2 = new GameCategory("Strategy game,", GameCategory.VisibilityStatus.Visible, "Strat");
        gameCategory3 = new GameCategory("Adventure game", GameCategory.VisibilityStatus.Visible, "Adv");
        gameCategory4 = new GameCategory("Racing game", GameCategory.VisibilityStatus.Visible, "Race");
        gameCategory5 = new GameCategory("Puzzle game", GameCategory.VisibilityStatus.Visible, "Puz");

        customer1 = new Customer("Tim", "tim_roma", "tim@roma.ca", "tim123", "123-456-7890");
        customer2 = new Customer("John", "john_doe", "john_does@mail,cou", "john123", "987-654-3210");
        customer3 = new Customer("Jane", "jane_doe", "jane_does@mail,cou", "jane123", "456-789-0123");
        customer4 = new Customer("Alice", "alice_wonderland", "alice@mail.com", "alice123", "789-012-3456");
        customer5 = new Customer("Bob", "bob_builder", "bob@mail.com", "bob123", "012-345-6789");

        game1 = new Game("Call of Duty", "Shoot 'em Up", "GameImg", 100, 80, "14+", Game.VisibilityStatus.Visible, gameCategory1);
        game2 = new Game("Age of Empires", "Build and Conquer", "GameImg", 50, 40, "10+", Game.VisibilityStatus.Visible, gameCategory2);
        game3 = new Game("Uncharted", "Adventure", "GameImg", 60, 50, "12+", Game.VisibilityStatus.Visible, gameCategory3);
        game4 = new Game("Need for Speed", "Racing", "GameImg", 70, 60, "10+", Game.VisibilityStatus.Visible, gameCategory4);
        game5 = new Game("Tetris", "Puzzle", "GameImg", 20, 10, "E", Game.VisibilityStatus.Visible, gameCategory5);

    }

    /* 
        ************************** createReview Tests **************************
    */ 
    // Test case for when the review is successfully created
    @Test
    public void testCreateReview_Success() {
        // Arrange
        String description = "Great game!";
        int score = 5;
        String reviewerID = "tim_roma";
        int gameID = 1;

        // Mocking the customer and game repositories
        when(customerRepo.findByUsername(reviewerID)).thenReturn(customer1);
        when(gameRepository.findByGameID(gameID)).thenReturn(game1);

        // The customer has the game
        List<Game> gameList = new ArrayList<>();
        gameList.add(game1);
        when(gameRepository.findGamesByCustomer(customer1)).thenReturn(gameList);

        // Mocking the review repository to return the saved review
        when(reviewRepository.save(any(Review.class))).thenAnswer(invocation -> {
            Review savedReview = invocation.getArgument(0);
            savedReview.setReviewID(1); // Set an ID if needed
            return savedReview;
        });

        // Act
        Review review = reviewService.createReview(description, score, reviewerID, gameID);

        // Assert
        assertNotNull(review);
        assertEquals(description, review.getDescription());
        assertEquals(score, review.getScore());
        assertEquals(customer1, review.getReviewer());
        assertEquals(game1, review.getReviewedGame());

        // Verify that save was called once with any Review object
        verify(reviewRepository, times(1)).save(any(Review.class));
    }

    // Test case for when the customer is not found
    @Test
    public void testCreateReviewCustomerNotFound() {
        // Arrange
        String description = "Great game!";
        int score = 5;
        String reviewerID = "non_existing_user"; // A username that doesn't exist
        int gameID = 1;

        // Mocking the customer repository to return null
        when(customerRepo.findByUsername(reviewerID)).thenReturn(null);

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            reviewService.createReview(description, score, reviewerID, gameID);
        });

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("Customer not found.", exception.getReason());
    }

    // Test case for when the game is not found
    @Test
    public void testCreateReviewGameNotFound() {
        // Arrange
        String description = "Great game!";
        int score = 5;
        String reviewerID = "non_existing_user"; // A username that doesn't exist
        int gameID = 1;

        // Mocking the customer repository to return a customer
        when(customerRepo.findByUsername(reviewerID)).thenReturn(customer1);

        // Mocking the game repository to return null
        when(gameRepository.findByGameID(gameID)).thenReturn(null);

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            reviewService.createReview(description, score, reviewerID, gameID);
        });

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("Reviewed game was not found.", exception.getReason());
    }

    @Test
    public void testCreateReviewCustomerDoesNotOwnGame() {
        // Arrange
        String description = "Great game!";
        int score = 5;
        String reviewerID = "john_doe";
        int gameID = 2; // A game that the customer doesn't own


        // Mocking the customer and game repositories
        when(customerRepo.findByUsername(reviewerID)).thenReturn(customer2);
        when(gameRepository.findByGameID(gameID)).thenReturn(game2);

        // The customer does not have the game
        List<Game> customerGames = new ArrayList<>();
        customerGames.add(game1); // customer1 owns game1, but not game2
        when(gameRepository.findGamesByCustomer(customer1)).thenReturn(customerGames);

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            reviewService.createReview(description, score, reviewerID, gameID);
        });

        assertEquals(HttpStatus.FORBIDDEN, exception.getStatusCode());
        assertEquals("Customer does not have the game.", exception.getReason());
    }

    /* 
        ************************** updateReview Tests **************************
    */ 

    @Test
    public void testUpdateReview_Success() {
        // Arrange
        int reviewID = 1;
        String newDescription = "Updated review description.";
        int newScore = 4;
        String reviewerID = "jane_doe";

        // Existing review
        Review existingReview = new Review("Original description", 5, Date.valueOf(LocalDate.now()), customer3, game1);
        existingReview.setReviewID(reviewID);

        // Mocking the review repository
        when(reviewRepository.findByReviewID(reviewID)).thenReturn(existingReview);
        when(reviewRepository.save(any(Review.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Review updatedReview = reviewService.updateReview(reviewID, newDescription, newScore, reviewerID);

        // Assert
        assertNotNull(updatedReview);
        assertEquals(reviewID, updatedReview.getReviewID());
        assertEquals(newDescription, updatedReview.getDescription());
        assertEquals(newScore, updatedReview.getScore());
        assertEquals(Date.valueOf(LocalDate.now()), updatedReview.getDate());

        verify(reviewRepository, times(1)).save(existingReview);
    }


    @Test
    public void testUpdateReview_ReviewNotFound() {
        // Arrange
        int reviewID = 99; // Non-existent review ID
        String newDescription = "Updated review description.";
        int newScore = 4;
        String reviewerID = "tim_roma";

        // Mocking the review repository to return null
        when(reviewRepository.findByReviewID(reviewID)).thenReturn(null);

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            reviewService.updateReview(reviewID, newDescription, newScore, reviewerID);
        });

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("Review not found.", exception.getReason());
    }

    @Test
    public void testUpdateReview_InvalidDescription() {
        // Arrange
        int reviewID = 1;
        String newDescription = ""; // Empty description
        int newScore = 4;
        String reviewerID = "tim_roma";

        // Existing review
        Review existingReview = new Review("Original description", 5, Date.valueOf(LocalDate.now()), customer1, game1);
        existingReview.setReviewID(reviewID);

        // Mocking the review repository
        when(reviewRepository.findByReviewID(reviewID)).thenReturn(existingReview);

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            reviewService.updateReview(reviewID, newDescription, newScore, reviewerID);
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("Description cannot be null or empty.", exception.getReason());
    }

    @Test
    public void testUpdateReview_ZeroScore() {
        // Arrange
        int reviewID = 1;
        String newDescription = "Updated review description.";
        int newScore = 0; // Invalid score
        String reviewerID = "tim_roma";

        // Existing review
        Review existingReview = new Review("Original description", 5, Date.valueOf(LocalDate.now()), customer1, game1);
        existingReview.setReviewID(reviewID);

        // Mocking the review repository
        when(reviewRepository.findByReviewID(reviewID)).thenReturn(existingReview);

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            reviewService.updateReview(reviewID, newDescription, newScore, reviewerID);
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("Score cannot be zero.", exception.getReason());
    }

    @Test
    public void testUpdateReview_NegativeScore() {
        // Arrange
        int reviewID = 1;
        String newDescription = "Updated review description.";
        int newScore = -5; // Invalid score
        String reviewerID = "tim_roma";

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            reviewService.updateReview(reviewID, newDescription, newScore, reviewerID);
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("Review ID and score must be greater than 0.", exception.getReason());
    }

    @Test
    public void testGetAllReviews() {
        // Arrange
        List<Review> reviews = new ArrayList<>();
        reviews.add(new Review("Great game!", 5, Date.valueOf(LocalDate.now()), customer1, game1));
        reviews.add(new Review("Not bad", 3, Date.valueOf(LocalDate.now()), customer2, game2));
        reviews.add(new Review("Could be better", 2, Date.valueOf(LocalDate.now()), customer3, game3));

        // Mocking the review repository
        when(reviewRepository.findAll()).thenReturn(reviews);

        // Act
        List<Review> allReviews = reviewService.getAllReviews();

        // Assert
        assertNotNull(allReviews);
        assertEquals(3, allReviews.size());
        assertEquals(reviews, allReviews);

        verify(reviewRepository, times(1)).findAll();
    }

    @Test
    public void testGetReview() {
        // Arrange
        int reviewID = 1;
        Review review = new Review("Great game!", 5, Date.valueOf(LocalDate.now()), customer1, game1);
        review.setReviewID(reviewID);

        // Mocking the review repository
        when(reviewRepository.findByReviewID(reviewID)).thenReturn(review);

        // Act
        Review foundReview = reviewService.getReview(reviewID);

        // Assert
        assertNotNull(foundReview);
        assertEquals(review, foundReview);
        verify(reviewRepository, times(1)).findByReviewID(reviewID);
    }

    @Test 
    public void testAddLike_Success() {
        Review review = new Review("Great game!", 5, Date.valueOf(LocalDate.now()), customer1, game1);
        review.setReviewID(1);

        // Mock the repositories
        when(reviewRepository.findByReviewID(1)).thenReturn(review);
        when(customerRepo.findByUsername("alice_wonderland")).thenReturn(customer4);
        
        when(reviewLikeRepository.existsByReviewAndCustomer(review, customer4)).thenReturn(false);

        when(reviewLikeRepository.save(any(ReviewLike.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Mock the save method
        when(reviewRepository.save(any(Review.class))).thenAnswer(invocation -> invocation.getArgument(0));

        int amountLikes = reviewService.addLike(1, "alice_wonderland");

        // Assert that both the function and the getLike methods return the expected amount of likes
        assertEquals(1, amountLikes);
        assertEquals(1, review.getLikes());

        //Make sure only one reviewLike is associated to our review
        assertEquals(1, review.getReviewLikes().size());

        // Verify that the save method was called once
        verify(reviewRepository, times(1)).save(review);

        // Verify that the find methods were called once
        verify(reviewRepository, times(1)).findByReviewID(1);
        verify(customerRepo, times(1)).findByUsername("alice_wonderland");
    }

    @Test
    public void testAddLike_CustomerNotFound() {
        // Mock the repositories
        when(customerRepo.findByUsername("unknown_user")).thenReturn(null);

        // Assert that the exception is thrown
        ResponseStatusException exception = assertThrows(
            ResponseStatusException.class,
            () -> reviewService.addLike(1, "unknown_user")
        );

        // Verify the exception message
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("Customer not found.", exception.getReason());

        // Verify that the reviewLikeRepository was not called
        verify(reviewLikeRepository,  times(0)).existsByReviewAndCustomer(any(), any());
    }

    @Test
    public void testAddLike_ReviewNotFound() {
        // Mock the repositories
        when(customerRepo.findByUsername("alice_wonderland")).thenReturn(customer4);
        when(reviewRepository.findByReviewID(1)).thenReturn(null);

        // Assert that the exception is thrown
        ResponseStatusException exception = assertThrows(
            ResponseStatusException.class,
            () -> reviewService.addLike(1, "alice_wonderland")
        );

        // Verify the exception message
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("Review not found.", exception.getReason());

        // Verify that the reviewLikeRepository was not called
        verify(reviewLikeRepository,  times(0)).existsByReviewAndCustomer(any(), any());
    }

    @Test
    public void testAddLike_CustomerAlreadyLiked() {
        
        Review review = new Review("Amazing game!", 4, Date.valueOf(LocalDate.now()), customer5, game5);
        review.setReviewID(1);

        // Mock the repositories
        when(customerRepo.findByUsername("bob_builder")).thenReturn(customer5);
        when(reviewRepository.findByReviewID(1)).thenReturn(review);
        when(reviewLikeRepository.existsByReviewAndCustomer(review, customer5)).thenReturn(true);

        // Assert that the exception is thrown
        ResponseStatusException exception = assertThrows(
            ResponseStatusException.class,
            () -> reviewService.addLike(1, "bob_builder")
        );

        // Verify the exception message
        assertEquals(HttpStatus.FORBIDDEN, exception.getStatusCode());
        assertEquals("Customer has already liked the review.", exception.getReason());

        // Verify that the save methods were not called
        verify(reviewLikeRepository, times(0)).save(any());
        verify(reviewRepository, times(0)).save(any());
    }

    // @Test
    // public void testRemoveLike_Success() {
    //     /*
    //     * FIRST WE ADD A LIKE TO THE REVIEW
    //     */

    //     // Set up the review and the customer that will like the review
    //     Review review = new Review("Great game!", 5, Date.valueOf(LocalDate.now()), customer1, game1);
    //     review.setReviewID(1);

    //     ReviewLike reviewLike = new ReviewLike(review, customer4);

    //     // Mock the repositories for adding a like
    //     when(reviewRepository.findByReviewID(1)).thenReturn(review);
    //     when(customerRepo.findByUsername("alice_wonderland")).thenReturn(customer4);
    //     when(reviewLikeRepository.existsByReviewAndCustomer(review, customer4)).thenReturn(false);
    //     when(reviewLikeRepository.save(any(ReviewLike.class))).thenAnswer(invocation -> invocation.getArgument(0));
    //     when(reviewRepository.save(any(Review.class))).thenAnswer(invocation -> invocation.getArgument(0));

    //     // Add a like to the review so that we can remove it later
    //     int amountLikes = reviewService.addLike(1, "alice_wonderland");

    //     // Assert that the like was added successfully
    //     assertEquals(1, amountLikes);
    //     assertEquals(1, review.getLikes());

    //     /*
    //     * THEN WE REMOVE THE LIKE
    //     */

    //     // Mock the repositories for removing a like
    //     when(reviewLikeRepository.existsByReviewAndCustomer(review, customer4)).thenReturn(true);
    //     when(reviewLikeRepository.findByReviewAndCustomer(review, customer4)).thenReturn(reviewLike);
    //     when(reviewRepository.save(any(Review.class))).thenAnswer(invocation -> invocation.getArgument(0));

    //     // Call the removeLike method
    //     int finalAmountLikes = reviewService.removeLike(1, "alice_wonderland");

    //     // Assert that the like was removed successfully
    //     assertEquals(0, finalAmountLikes);
    //     assertEquals(0, review.getLikes());

    //     // Verify that the review has no associated likes
    //     assertEquals(0, review.getReviewLikes().size());

    //     // Verify that the delete method was called on the reviewLikeRepository
    //     verify(reviewLikeRepository, times(1)).delete(reviewLike);

    //     // Verify that the save method was called twice (once for addLike and once for removeLike)
    //     verify(reviewRepository, times(2)).save(review);

    //     // Verify that the find methods were called as expected
    //     verify(reviewRepository, times(2)).findByReviewID(1);
    //     verify(customerRepo, times(2)).findByUsername("alice_wonderland");
    // }


}
