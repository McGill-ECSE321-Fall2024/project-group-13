package group_13.game_store.repository;

import org.springframework.data.repository.CrudRepository;
import group_13.game_store.model.Promotion;

public interface PromotionRepository extends CrudRepository<Promotion, Integer> {
    public Promotion findByPromotionID(int promotionID);
}
