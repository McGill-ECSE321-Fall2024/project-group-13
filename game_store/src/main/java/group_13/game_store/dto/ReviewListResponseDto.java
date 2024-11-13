package group_13.game_store.dto;

import java.util.List;

import group_13.game_store.model.Review;

public class ReviewListResponseDto {
    private List<ReviewResponseDto> reviews;

    public ReviewListResponseDto() {
    }

    public ReviewListResponseDto(List<ReviewResponseDto> reviews) {
        this.reviews = reviews;
    }

    public List<ReviewResponseDto> getReviews() {
        return reviews;
    }

}
