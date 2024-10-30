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
    // loading an instance of the local tables containing rows of WishlistItem, Games, Customer, and GameCategory instances from the local database
    @Autowired
    private WishlistItemRepository wishlistItemRepository;

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private GameCategoryRepository gameCategoryRepository;

    // clearing the WishlistItem, Games, Customer, and GameCategory tables that were loaded in before testing
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

        // saving the above WishlistItem, Games, Customer, and GameCategory instances in the cleared WishlistItem, Games, Customer, and GameCategory tables 
        savedGameCategory = gameCategoryRepository.save(savedGameCategory);
        nicolas = customerRepository.save(nicolas);
        game = gameRepository.save(game);
        savedWishlistItem = wishlistItemRepository.save(savedWishlistItem);

        // Act
        WishlistItem readWishlistItem = wishlistItemRepository.findByKey(savedWishlistItem.getKey());

        // Assert
        // ensuring the loaded WishlistItem row instance actually exists in the table of the local database
        assertNotNull(readWishlistItem);
        // ensuring that the composite key actually exists in the table of the local database
        assertNotNull(readWishlistItem.getKey());
        assertNotNull(readWishlistItem.getKey().getCustomerAccount());
        assertNotNull(readWishlistItem.getKey().getGame());
        // verifying if all the fields of WishlistItem instance that was created before saving it into the local database matches the fields of the loaded row instance of WishlistItem from the table
        assertEquals(savedWishlistItem.getKey().getCustomerAccount().getUsername(), readWishlistItem.getKey().getCustomerAccount().getUsername());
        assertEquals(savedWishlistItem.getKey().getGame().getGameID(), readWishlistItem.getKey().getGame().getGameID());

    }

}
