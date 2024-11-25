package group_13.game_store.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import group_13.game_store.model.Customer;
import group_13.game_store.model.Game;

public interface GameRepository extends CrudRepository<Game, Integer> {
    // allows instantiation of an Game instance that is stored in the local database by its unique ID
    public Game findByGameID(int gameID);

    List<Game> findByCategory_Name(String name);

    List<Game> findByTitleStartingWith(String title);

    List<Game> findByStockGreaterThanAndStatusIn(int stock, List<Game.VisibilityStatus> statuses);

    List<Game> findByCategory_NameAndStockGreaterThanAndStatusIn(String category, int stock, List<Game.VisibilityStatus> statuses);

    List<Game> findByTitleStartingWithAndStockGreaterThanAndStatusIn(String title, int stock, List<Game.VisibilityStatus> statuses);

    Game findByGameIDAndStockGreaterThanAndStatusIn(int gameID, int stock, List<Game.VisibilityStatus> statuses);

    List<Game> findByStatusIn(List<Game.VisibilityStatus> statuses);
    
    //Method to find all games associated to a customer
    @Query("SELECT DISTINCT gc.game FROM GameCopy gc JOIN gc.order o WHERE o.customer = :customer")
    List<Game> findGamesByCustomer(@Param("customer") Customer customer);

    // find games by title
    Game findByTitle(String title);
}
