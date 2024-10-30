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
    // loading an instance of the local tables containing rows of GameCategory, Game, Customer, and Review from the local database
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
        
        // saving the above GameCategory, Game, Customer, and Review instances in the cleared GameCategory, Game, Customer, and Review tables 
        gameCategory = gameCategoryRepo.save(gameCategory);
        game = gameRepo.save(game);
        customer = customerRepo.save(customer);
        review = reviewRepo.save(review);

        int id = review.getReviewID();

        // Act
        Review reviewFromDb = reviewRepo.findByReviewID(id);

        // Assert
        // ensuring the loaded Review, Game, and Customer row instances actually exist in the tables of the local database
        assertNotNull(reviewFromDb);
        assertNotNull(reviewFromDb.getReviewedGame());
        assertNotNull(reviewFromDb.getReviewer());
        // verifying if all the fields of Review instance that was created before saving it into the local database matches the fields of the loaded row instance of Review from the table
        assertEquals(id, reviewFromDb.getReviewID());
        assertEquals(review.getReviewer().getUsername(), reviewFromDb.getReviewer().getUsername());
        assertEquals(review.getReviewedGame().getGameID(), reviewFromDb.getReviewedGame().getGameID());
        assertEquals(review.getDate(), reviewFromDb.getDate());
        assertEquals(review.getDescription(), reviewFromDb.getDescription());
        assertEquals(review.getLikes(), reviewFromDb.getLikes());
        assertEquals(review.getScore(), reviewFromDb.getScore());
    }
}
