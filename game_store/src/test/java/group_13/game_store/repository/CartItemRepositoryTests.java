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

    // loading an instance of the local tables containing rows of CartItem, Games, Customer, and GameCategory instances from the local database
    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private CustomerRepository customerRepository;
    
    @Autowired
    private GameCategoryRepository gameCategoryRepository;

    // clearing the CartItem, Games, Customer, and GameCategory tables that were loaded in before testing
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

        // saving the above CartItem, Games, Customer, and GameCategory instances in the cleared CartItem, Games, Customer, and GameCategory tables 
        savedGameCategory = gameCategoryRepository.save(savedGameCategory);
        nicolas = customerRepository.save(nicolas);
        game = gameRepository.save(game);
        savedCartItem = cartItemRepository.save(savedCartItem);

        // Act
        // loading the key instance of CartItem, bceause it's a composite key, and not a primary key that will identify a CartItem row
        CartItem readCartItem = cartItemRepository.findByKey(key);

        // Assert
        // ensuring the loaded cartItem row instance actually exists in the table of the local database
        assertNotNull(readCartItem);
        // ensuring that the composite key actually exists in the table of the local database
        assertNotNull(readCartItem.getKey());
        // verifying if all the fields of CartItem instance that was created before saving it into the local database matches the fields of the loaded row instance of CartItem from the table
        // not verifying the IDs of the other fields, because those fields are tested elsewhere, so it is redundant to test them again to check if their fields match the fields of the instances that were created before saving it in the local database
        assertEquals(savedCartItem.getQuantity(), readCartItem.getQuantity());
        assertNotNull(readCartItem.getKey().getCustomerAccount());
        assertNotNull(readCartItem.getKey().getGame());
        assertEquals(savedCartItem.getKey().getCustomerAccount().getUsername(), readCartItem.getKey().getCustomerAccount().getUsername());
        assertEquals(savedCartItem.getKey().getGame().getGameID(), readCartItem.getKey().getGame().getGameID());
    }
 
}
