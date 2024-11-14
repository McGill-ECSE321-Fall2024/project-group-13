package group_13.game_store.dto;

import java.time.LocalDate;

import group_13.game_store.model.Review;

//Dto to format the response you would get from an http request to get a review
public class ReviewResponseDto {
    private int reviewID;
    private String description;
    private int score;
    private int likes;
    private String reviewerUsername;

    //Set the date to a LocalDate instead of a Date to make it easier to work with
    private LocalDate date;


    //Default constructor for the ReviewResponseDto
    public ReviewResponseDto() {
    }
    
    //Constructor for the ReviewResponseDto
    public ReviewResponseDto(Review review) {
        this.reviewID = review.getReviewID();
        this.description = review.getDescription();
        this.score = review.getScore();
        this.likes = review.getLikes();
        this.reviewerUsername = review.getReviewer().getUsername();

        //Convert the Date to a LocalDate
        this.date = review.getDate().toLocalDate();
    }

    //Only Getter methods for the ReviewResponseDto as response objects should not be modified
    public int getReviewID() {
        return reviewID;
    }

    public String getDescription() {
        return description;
    }

    public int getScore() {
        return score;
    }

    public int getLikes() {
        return likes;
    }

    public String getReviewerUsername() {
        return reviewerUsername;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setReviewID(int reviewID) {
        this.reviewID = reviewID;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public void setScore(int score) {
        this.score = score;
    }
    
    public void setLikes(int likes) {
        this.likes = likes;
    }
    
    public void setReviewerUsername(String reviewerUsername) {
        this.reviewerUsername = reviewerUsername;
    }
    
    public void setDate(LocalDate date) {
        this.date = date;
    }
    
}
