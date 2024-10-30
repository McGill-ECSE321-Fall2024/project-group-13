package group_13.game_store.repository;

import org.springframework.data.repository.CrudRepository;
import group_13.game_store.model.CartItem;

public interface CartItemRepository extends CrudRepository<CartItem, CartItem.Key> {
    // allows instantiation of a CartItem instance that is stored in the local database by its composite key consisting of a unique CustomerID and unique GameID
    public CartItem findByKey(CartItem.Key key);
}
