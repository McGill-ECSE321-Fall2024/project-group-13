package group_13.game_store.repository;

import org.springframework.data.repository.CrudRepository;
import group_13.game_store.model.WishlistItem;

public interface WishlistItemRepository extends CrudRepository<WishlistItem, WishlistItem.Key> {
    public WishlistItem findByKey(WishlistItem.Key key);
}
