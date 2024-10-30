package group_13.game_store.repository;

import org.springframework.data.repository.CrudRepository;
import group_13.game_store.model.WishlistItem;

public interface WishlistItemRepository extends CrudRepository<WishlistItem, WishlistItem.Key> {
    // allows instantiation of a WishlistItem instance that is stored in the local database by its composite key consisting of a unique CustomerID and unique GameID
    public WishlistItem findByKey(WishlistItem.Key key);
}
