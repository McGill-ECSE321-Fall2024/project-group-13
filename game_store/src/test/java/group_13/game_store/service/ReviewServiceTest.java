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
import group_13.game_store.repository.CustomerRepository;
import group_13.game_store.repository.GameCategoryRepository;
import group_13.game_store.repository.GameRepository;
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
}
