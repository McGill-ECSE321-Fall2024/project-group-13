package group_13.game_store.repository;

import org.springframework.data.repository.CrudRepository;
import group_13.game_store.model.Game;

public interface GameRepository extends CrudRepository<Game, Integer> {
    public Game findByGameID(int gameID);
}
