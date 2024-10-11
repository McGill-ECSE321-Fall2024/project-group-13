package group_13.game_store.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import group_13.game_store.model.CartItem;
import group_13.game_store.model.Game;
import group_13.game_store.model.GameCategory;
import group_13.game_store.model.Customer;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class CartItemRepositoryTests {

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private GameCategoryRepository gameCategoryRepository;

    @BeforeEach
    @AfterEach
    public void clearDatabase() {
        cartItemRepository.deleteAll();
        gameRepository.deleteAll();
        customerRepository.deleteAll();
        gameCategoryRepository.deleteAll();
    }

    @Test
    public void testReadAndWriteCartItem() {
        // Arrange
        GameCategory savedGameCategory = new GameCategory("This game category is a test",
                GameCategory.VisibilityStatus.Visible, "Test Category");

        Customer nicolas = new Customer("nicolas", "nicolasIsAmazing", "nick@gmail.com", "1234asd", "613-242-1325", 1);

        Game game = new Game("Call of Duty", "Shoot 'em Up", "GameImg", 100, 80, "14+", Game.VisibilityStatus.Visible, savedGameCategory);

        CartItem.Key key = new CartItem.Key(nicolas, game);

        CartItem savedCartItem = new CartItem(key, 1);

        savedGameCategory = gameCategoryRepository.save(savedGameCategory);
        nicolas = customerRepository.save(nicolas);
        game = gameRepository.save(game);
        savedCartItem = cartItemRepository.save(savedCartItem);

        // Act
        CartItem readCartItem = cartItemRepository.findByKey(key);

        // Assert
        assertNotNull(readCartItem);
        assertNotNull(readCartItem.getKey());
        assertEquals(savedCartItem.getQuantity(), readCartItem.getQuantity());
        assertNotNull(readCartItem.getKey().getUserAccount());
        assertNotNull(readCartItem.getKey().getGame());
        assertEquals(savedCartItem.getKey().getUserAccount().getUsername(), readCartItem.getKey().getUserAccount().getUsername());
        assertEquals(savedCartItem.getKey().getGame().getGameID(), readCartItem.getKey().getGame().getGameID());
    }

}
