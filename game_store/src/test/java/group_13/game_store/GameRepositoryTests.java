package group_13.game_store;

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
import group_13.game_store.model.GameCategory.VisibilityStatus;
// need to modify this issue
//import group_13.game_store.model.Game.VisibilityStatus;
import group_13.game_store.model.Promotion;
// hopefully this line does not cause an issue...
import group_13.game_store.repository.GameRepository;

@SpringBootTest
public class GameRepositoryTests {

    @Autowired
    private GameRepository gameRepo;
    @Autowired
    private Promotion promo;
    @Autowired
    private GameCategory gameCategory;

    @BeforeEach
	@AfterEach
	public void clearDatabase() {
		gameRepo.deleteAll();
	}

    // first test: check if the Game actually exists in the database
    @Test
	public void testCreateAndReadGame() {
        // arrange

        GameCategory randomCategory = new GameCategory("this genre involves X and y", VisibilityStatus.Visible, "generic genre"); 
        Promotion limitedPromo = new Promotion(15, Date.valueOf("2024-10-21"), Date.valueOf("2024-10-31"), "Halloween Deal", "Discounts, because Halloween is soon");
        //Game randomGame = new Game("Generic title", "Long description", "Image of game", 50, 14.99, "18+", VisibilityStatus.Visible, randomCategory);

        //assert
    }


}
