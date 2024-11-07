package group_13.game_store.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import group_13.game_store.model.Promotion;

public interface PromotionRepository extends CrudRepository<Promotion, Integer> {
    // allows instantiation of an Promotion instance that is stored in the local database by its unique ID
    public Promotion findByPromotionID(int promotionID);

    //Method to find all promotions associated to a gameID
    public List<Promotion> findByGame_GameID(int gameID);
}
