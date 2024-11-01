package group_13.game_store.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.jpa.repository.Modifying;
import jakarta.transaction.Transactional;
//import org.springframework.data.jpa.repository.Query;
import group_13.game_store.model.CartItem;

public interface CartItemRepository extends CrudRepository<CartItem, CartItem.Key> {
    // allows instantiation of a CartItem instance that is stored in the local database by its composite key consisting of a unique CustomerID and unique GameID
    public CartItem findByKey(CartItem.Key key);

    public List<CartItem> findByKeyCustomerAccountUsername(String username);

    @Modifying
    @Transactional
    void deleteByKeyCustomerAccountUsername(String username);
}
