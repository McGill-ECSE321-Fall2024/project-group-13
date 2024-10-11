package group_13.game_store.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.sql.Date;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import group_13.game_store.model.Customer;
import group_13.game_store.model.Game;
import group_13.game_store.model.GameCategory;
import group_13.game_store.model.Review;

@SpringBootTest
public class ReviewRepositoryTests {
    // initialize repositories
    @Autowired
    private ReviewRepository reviewRepo;
    @Autowired
    private CustomerRepository customerRepo;
    @Autowired
    private GameRepository gameRepo;
    @Autowired
    private GameCategoryRepository gameCategoryRepo;

    
    // clear tables before/after each test
    @BeforeEach
    @AfterEach
    public void clearDatabase() {
        reviewRepo.deleteAll();
        customerRepo.deleteAll();
        gameRepo.deleteAll();
        gameCategoryRepo.deleteAll();
    }


    @Test
    public void testCreateAndReadReview() {
        // Arrange
        GameCategory gameCategory = new GameCategory("Shooter game in the first person", GameCategory.VisibilityStatus.Visible, "FPS");
        Customer customer = new Customer("Tim", "tim_roma", "tim@roma.ca", "tim123", "123-456-7890", 1);
        Game game = new Game("Call of Duty", "Shoot 'em Up", "GameImg", 100, 80, "14+", Game.VisibilityStatus.Visible, gameCategory);
        Review review = new Review("Very good game!", 4, 0, Date.valueOf("2024-10-11"), customer, game);

        gameCategory = gameCategoryRepo.save(gameCategory);
        game = gameRepo.save(game);
        customer = customerRepo.save(customer);
        review = reviewRepo.save(review);
        int id = review.getReviewID();

        // Act
        Review reviewFromDb = reviewRepo.findByReviewID(id);

        // Assert
        assertNotNull(reviewFromDb);
        assertEquals(id, reviewFromDb.getReviewID());
        assertNotNull(reviewFromDb.getReviewer());
        assertEquals(review.getReviewer().getUsername(), reviewFromDb.getReviewer().getUsername());
        assertNotNull(reviewFromDb.getReviewedGame());
        assertEquals(review.getReviewedGame().getGameID(), reviewFromDb.getReviewedGame().getGameID());
        assertEquals(review.getDate(), reviewFromDb.getDate());
        assertEquals(review.getDescription(), reviewFromDb.getDescription());
        assertEquals(review.getLikes(), reviewFromDb.getLikes());
        assertEquals(review.getScore(), reviewFromDb.getScore());
    }
}
