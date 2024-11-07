package group_13.game_store.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import group_13.game_store.model.GameCategory;

public interface GameCategoryRepository extends CrudRepository<GameCategory, Integer> {
    // allows instantiation of an Category instance that is stored in the local database by its unique ID
    public GameCategory findByCategoryID(int categoryID);

    List<GameCategory> findByStatusIn(List<GameCategory.VisibilityStatus> statuses);
}