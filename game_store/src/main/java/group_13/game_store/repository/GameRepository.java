package group_13.game_store.repository;

import org.springframework.data.repository.CrudRepository;
import group_13.game_store.model.Game;

import java.util.List;

public interface GameRepository extends CrudRepository<Game, Integer> {
    // allows instantiation of an Game instance that is stored in the local database by its unique ID
    public Game findByGameID(int gameID);

    List<Game> findByCategory_Name(String name);

    List<Game> findByTitleStartingWith(String title);

    List<Game> findByStockGreaterThanAndStatusIn(int stock, List<Game.VisibilityStatus> statuses);

    List<Game> findByCategory_NameAndStockGreaterThanAndStatusIn(String category, int stock, List<Game.VisibilityStatus> statuses);

    List<Game> findByTitleStartingWithAndStockGreaterThanAndStatusIn(String title, int stock, List<Game.VisibilityStatus> statuses);

    Game findByGameIDAndStockGreaterThanAndStatusIn(int gameID, int stock, List<Game.VisibilityStatus> statuses);


}
