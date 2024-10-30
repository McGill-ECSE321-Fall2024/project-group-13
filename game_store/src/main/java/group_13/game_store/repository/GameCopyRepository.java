package group_13.game_store.repository;

import org.springframework.data.repository.CrudRepository;
import group_13.game_store.model.GameCopy;

public interface GameCopyRepository extends CrudRepository<GameCopy, Integer> {
    // allows instantiation of an gameCopy instance that is stored in the local database by its unique ID
    public GameCopy findByCopyID(int copyID);
}