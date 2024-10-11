package group_13.game_store;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import group_13.game_store.model.Game;
import group_13.game_store.model.GameCategory;
import group_13.game_store.model.Promotion;
// hopefully this line does not cause an issue...
import group_13.game_store.repository.GameRepository;

@SpringBootTest
public class GameRepositoryTests {

    @Autowired
    private GameRepository gameRepo;

    @BeforeEach
	@AfterEach
	public void clearDatabase() {
		gameRepo.deleteAll();
	}

    // first test: check if the Game actually exists in the database
    @Test
	public void testCreateAndReadGame() {
        // arrange
        

        //act

        //assert
    }


}
