package group_13.game_store.repository;

import org.springframework.data.repository.CrudRepository;
import group_13.game_store.model.GameCategory;

public interface GameCategoryRepository extends CrudRepository<GameCategory, Integer> {
    public GameCategory findByCategoryID(int categoryID);
}