package group_13.game_store.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import group_13.game_store.model.Review;

public interface ReviewRepository extends CrudRepository<Review, Integer> {
    // allows instantiation of an Review instance that is stored in the local database by its unique ID
    public Review findByReviewID(int reviewID);

    //Method to find all reviews associated to a gameID
    List<Review> findByReviewedGame_GameID(int gameID);
}
