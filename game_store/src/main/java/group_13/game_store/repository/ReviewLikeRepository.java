package group_13.game_store.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import group_13.game_store.model.Review;
import group_13.game_store.model.ReviewLike;
import group_13.game_store.model.Customer;

public interface ReviewLikeRepository extends JpaRepository<ReviewLike, Long> {

    boolean existsByReviewAndCustomer(Review review, Customer customer);

    ReviewLike findByReviewAndCustomer(Review review, Customer customer);

    @Query("SELECT rl.review FROM ReviewLike rl WHERE rl.customer.username = :username")
    List<Review> findReviewsLikedByCustomer(@Param("username") String username);
}
