package group_13.game_store.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import group_13.game_store.model.Customer;
import group_13.game_store.model.Game;
import group_13.game_store.model.GameCategory;
import group_13.game_store.model.WishlistItem;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class WishlistItemRepositoryTests {

    @Autowired
    private WishlistItemRepository wishlistItemRepository;

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private GameCategoryRepository gameCategoryRepository;

    @BeforeEach
    @AfterEach
    public void clearDatabase() {
        wishlistItemRepository.deleteAll();
        gameRepository.deleteAll();
        customerRepository.deleteAll();
    }

    public void testReadAndWriteWishlistItem() {
        // Arrange
        GameCategory savedGameCategory = new GameCategory("This game category is a test", GameCategory.VisibilityStatus.Visible, "Test Category");

        Customer nicolas = new Customer("nicolas", "nicolasIsAmazing", "nick@gmail.com", "1234asd", "613-242-1325", 1);

        Game game = new Game("Call of Duty", "Shoot 'em Up", "GameImg", 100, 80, "14+", Game.VisibilityStatus.Visible, savedGameCategory);

        WishlistItem.Key key = new WishlistItem.Key(nicolas, game);

        WishlistItem savedWishlistItem = new WishlistItem(key);

        savedGameCategory = gameCategoryRepository.save(savedGameCategory);
        nicolas = customerRepository.save(nicolas);
        game = gameRepository.save(game);
        savedWishlistItem = wishlistItemRepository.save(savedWishlistItem);

        // Act
        WishlistItem readWishlistItem = wishlistItemRepository.findByKey(savedWishlistItem.getKey());

        // Assert
        assertNotNull(readWishlistItem);
        assertNotNull(readWishlistItem.getKey());
        assertNotNull(readWishlistItem.getKey().getUserAccount());
        assertNotNull(readWishlistItem.getKey().getGame());
        assertEquals(savedWishlistItem.getKey().getUserAccount().getUsername(), readWishlistItem.getKey().getUserAccount().getUsername());
        assertEquals(savedWishlistItem.getKey().getGame().getGameID(), readWishlistItem.getKey().getGame().getGameID());

    }

}
