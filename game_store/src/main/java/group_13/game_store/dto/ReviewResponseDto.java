package group_13.game_store.dto;

import java.sql.Date;
import java.time.LocalDate;

import group_13.game_store.model.Review;

//Dto to format the response you would get from an http request to get a review
public class ReviewResponseDto {
    private int reviewID;
    private String description;
    private int score;
    private int likes;

    //Set the date to a LocalDate instead of a Date to make it easier to work with
    private LocalDate date;

    @SuppressWarnings("unused")
    private ReviewResponseDto() {
    }

    //Constructor for the ReviewResponseDto
    public ReviewResponseDto(Review review) {
        this.reviewID = review.getReviewID();
        this.description = review.getDescription();
        this.score = review.getScore();
        this.likes = review.getLikes();

        //Convert the Date to a LocalDate
        this.date = review.getDate().toLocalDate();
    }

    //Setter and Getter methods for the ReviewResponseDto
    public int getReviewID() {
        return reviewID;
    }

    public void setReviewID(int reviewID) {
        this.reviewID = reviewID;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date.toLocalDate();
    }
}
