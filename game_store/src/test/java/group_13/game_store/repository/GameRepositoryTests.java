package group_13.game_store.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import java.sql.Date;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

// will need GameCategory and Promotion for tests 
import group_13.game_store.model.Game;
import group_13.game_store.model.GameCategory;
import group_13.game_store.model.Promotion;

@SpringBootTest
public class GameRepositoryTests {
    // loading an instance of the local tables containing rows of Game, Promotion, and GameCategory from the local database
    @Autowired
    private GameRepository gameRepo;
    @Autowired
    private PromotionRepository promoRepo;
    @Autowired
    private GameCategoryRepository gameCategoryRepo;

    // clearing the Game, Promotion, and GameCategory tables that were loaded in before testing
    @BeforeEach
	@AfterEach
	public void clearDatabase() {
		gameRepo.deleteAll();
        promoRepo.deleteAll();
        gameCategoryRepo.deleteAll();
	}

    // first test: check if the Game actually exists in the database
    @Test
	public void testCreateAndReadGame() {
        // arrange
        // need to create fields for game to be instantiated
        GameCategory randomCategory = new GameCategory("this genre involves X and y", GameCategory.VisibilityStatus.Visible, "generic genre"); 
        Promotion randomPromo = new Promotion(15, Date.valueOf("2024-10-21"), Date.valueOf("2024-10-31"), "Halloween Deal", "Discounts, because Halloween is soon");
        
        Game randomGame = new Game("Generic title", "Long description", "Image of game", 50, 14.99, "18+", Game.VisibilityStatus.Visible, randomCategory);
        randomGame.setPromotion(randomPromo);
        
        // saving the above Game, Promotion, and GameCategory instances in the cleared Address and DeliveryInformation tables 
        randomCategory = gameCategoryRepo.save(randomCategory);
        randomPromo = promoRepo.save(randomPromo);
        randomGame = gameRepo.save(randomGame);
       
        int randomGameId = randomGame.getGameID();
        // act
        Game randomGameFromDB = gameRepo.findByGameID(randomGameId);
    

        //assert
        // ensuring the loaded Game, Promotion, and GameCategory row instances actually exist in the tables of the local database
        assertNotNull(randomGameFromDB);
        assertNotNull(randomGameFromDB.getPromotion());
        assertNotNull(randomGameFromDB.getCategory());
        // checking all fields of Game, its category and its promotion
        assertEquals(randomGame.getTitle(), randomGameFromDB.getTitle());
        assertEquals(randomGame.getDescription(), randomGameFromDB.getDescription());
        assertEquals(randomGame.getImg(), randomGameFromDB.getImg());
        assertEquals(randomGame.getStock(), randomGameFromDB.getStock());
        assertEquals(randomGame.getPrice(), randomGameFromDB.getPrice());
        assertEquals(randomGame.getParentalRating(), randomGameFromDB.getParentalRating());
        assertEquals(randomGame.getStatus(), randomGameFromDB.getStatus());

        assertEquals(randomGame.getCategory().getCategoryID(), randomGameFromDB.getCategory().getCategoryID());        
        assertEquals(randomGame.getPromotion().getPromotionID(), randomGameFromDB.getPromotion().getPromotionID());
        
    }


}
