package group_13.game_store.repository;

import org.springframework.data.repository.CrudRepository;
import group_13.game_store.model.CartItem;

public interface CartItemRepository extends CrudRepository<CartItem, CartItem.Key> {
    public CartItem findByKey(CartItem.Key key);
}
