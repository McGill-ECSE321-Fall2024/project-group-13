package group_13.game_store.repository;

import org.springframework.data.repository.CrudRepository;
import group_13.game_store.model.GameCopy;

public interface GameCopyRepository extends CrudRepository<GameCopy, Integer> {
    public GameCopy findByCopyID(int copyID);
}