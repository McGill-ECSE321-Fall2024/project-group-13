package group_13.game_store.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.sql.Date;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import group_13.game_store.model.GameCopy;
import group_13.game_store.model.Customer;
import group_13.game_store.model.Game;
import group_13.game_store.model.GameCategory;
import group_13.game_store.model.Order;

@SpringBootTest
public class GameCopyRepositoryTests {
    @Autowired
	private GameCopyRepository copyRepo;
    @Autowired
    private GameRepository gameRepo;
    @Autowired
    private OrderRepository orderRepo;
    @Autowired
    private GameCategoryRepository categoryRepo;
    @Autowired
    private CustomerRepository customerRepo;

	@BeforeEach
	@AfterEach
	public void clearDatabase() {
		copyRepo.deleteAll();
        gameRepo.deleteAll();
        orderRepo.deleteAll();
        categoryRepo.deleteAll();
        customerRepo.deleteAll();
	}

	@Test
	public void testCreateAndReadGameCopy() {
		// Arrange
        GameCategory gameCategory = new GameCategory("Shooter game in the first person", GameCategory.VisibilityStatus.Visible, "FPS");
        Game game = new Game("Call of Duty", "Shoot 'em Up", "GameImg", 100, 80, "14+", Game.VisibilityStatus.Visible, gameCategory);
        Customer customer = new Customer("Tim", "tim_roma", "tim@roma.ca", "tim123", "123-456-7890", 1);
        Order order = new Order(Date.valueOf("2024-02-09"), 15, Date.valueOf("2024-02-15"), true, customer);
        GameCopy gameCopy = new GameCopy(order, game);
		
        gameCategory = categoryRepo.save(gameCategory);
        game = gameRepo.save(game);
        customer = customerRepo.save(customer);
        order = orderRepo.save(order);
        gameCopy = copyRepo.save(gameCopy);
		int id = gameCopy.getCopyID();

		// Act
		GameCopy copyFromDb = copyRepo.findByCopyID(id);

		// Assert
		assertNotNull(copyFromDb);
		assertEquals(id, copyFromDb.getCopyID());
        assertNotNull(copyFromDb.getGame());
        assertEquals(gameCopy.getGame().getGameID(), copyFromDb.getGame().getGameID());
        assertNotNull(copyFromDb.getOrder());
		assertEquals(gameCopy.getOrder().getOrderID(), copyFromDb.getOrder().getOrderID());
	}
}
