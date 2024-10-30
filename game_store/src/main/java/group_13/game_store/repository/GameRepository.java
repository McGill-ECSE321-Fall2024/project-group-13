package group_13.game_store.repository;

import org.springframework.data.repository.CrudRepository;
import group_13.game_store.model.Game;

public interface GameRepository extends CrudRepository<Game, Integer> {
    // allows instantiation of an Game instance that is stored in the local database by its unique ID
    public Game findByGameID(int gameID);
}
