package group_13.game_store.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import group_13.game_store.model.Customer;
import group_13.game_store.model.Game;

import java.util.List;

public interface GameRepository extends CrudRepository<Game, Integer> {
    // allows instantiation of an Game instance that is stored in the local database by its unique ID
    public Game findByGameID(int gameID);

    List<Game> findByStatusIn(Game.VisibilityStatus status);
    
    //Method to find all games associated to a customer
    @Query("SELECT DISTINCT gc.game FROM GameCopy gc JOIN gc.order o WHERE o.customer = :customer")
    List<Game> findGamesByCustomer(@Param("customer") Customer customer);

}
