package group_13.game_store.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.sql.Date;
import java.util.List;

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
        Customer customer = new Customer("Tim", "tim_roma", "tim@roma.ca", "tim123", "123-456-7890");
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

    @Test
    public void testFindReviewScoresWithGameId() {
        // Arrange
        GameCategory gameCategory = new GameCategory("Shooter game in the first person", GameCategory.VisibilityStatus.Visible, "FPS");
        gameCategory = gameCategoryRepo.save(gameCategory);

        Game game = new Game("COD", "Shoot shoot shoot", "templateIMG", 100, 80, "14+", Game.VisibilityStatus.Visible, gameCategory);
        game = gameRepo.save(game);

        Customer customer1 = new Customer("Tim", "tim_roma", "tim@roma.ca", "tim123", "123-456-7890");
        Customer customer2 = new Customer("John", "john_doe", "john@doe.ca", "john123", "987-654-3210");
        Customer customer3 = new Customer("Jane", "jane_doe", "jane@doe.ca", "jane123", "456-789-0123");

        customer1 = customerRepo.save(customer1);
        customer2 = customerRepo.save(customer2);
        customer3 = customerRepo.save(customer3);

        Review review1 = new Review("Great game!", 5, 0, Date.valueOf("2024-10-11"), customer1, game);
        Review review2 = new Review("Not bad", 3, 0, Date.valueOf("2024-10-12"), customer2, game);
        Review review3 = new Review("Could be better", 2, 0, Date.valueOf("2024-10-13"), customer3, game);

        review1 = reviewRepo.save(review1);
        review2 = reviewRepo.save(review2);
        review3 = reviewRepo.save(review3);

        // Act
        List<Integer> scores = reviewRepo.findScoresByGameId(game.getGameID());

        // Assert
        assertNotNull(scores);
        assertEquals(3, scores.size());
    }
}
