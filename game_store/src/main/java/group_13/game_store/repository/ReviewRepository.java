package group_13.game_store.repository;

import org.springframework.data.repository.CrudRepository;
import group_13.game_store.model.Review;

public interface ReviewRepository extends CrudRepository<Review, Integer> {
    public Review findByReviewID(int reviewID);
}
