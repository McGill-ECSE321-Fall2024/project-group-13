package group_13.game_store.repository;

import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.jpa.repository.Modifying;
import jakarta.transaction.Transactional;
import group_13.game_store.model.CartItem;

public interface CartItemRepository extends CrudRepository<CartItem, CartItem.Key> {

    /**
     * Finds a specific CartItem by its composite key, which consists of a unique
     * CustomerID and GameID.
     * 
     * @param key The composite key used to identify the CartItem.
     * @return The CartItem associated with the given key, or null if not found.
     */
    public CartItem findByKey(CartItem.Key key);

    /**
     * Retrieves all CartItem instances associated with a specific customer username.
     * 
     * @param username The username of the customer.
     * @return A list of CartItem objects belonging to the specified customer.
     */
    public List<CartItem> findByKeyCustomerAccountUsername(String username);

    /**
     * Deletes all CartItem instances associated with a specific customer username.
     * This method is annotated as @Modifying and @Transactional to ensure changes 
     * are applied to the database and managed as a transaction.
     * 
     * @param username The username of the customer whose cart items are to be deleted.
     */
    @Modifying
    @Transactional
    void deleteByKeyCustomerAccountUsername(String username);
}
