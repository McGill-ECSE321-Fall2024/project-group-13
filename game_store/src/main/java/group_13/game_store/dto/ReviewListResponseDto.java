package group_13.game_store.dto;

import java.util.List;

import group_13.game_store.model.Review;

public class ReviewListResponseDto {
    private List<Review> reviews;

    public ReviewListResponseDto(List<Review> reviews) {
        this.reviews = reviews;
    }

    //No setters as it is a response object
    public List<Review> getGames() {
        return reviews;
    }
}
