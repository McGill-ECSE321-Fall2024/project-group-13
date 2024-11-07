package group_13.game_store.dto;

import java.util.List;

import group_13.game_store.model.Customer;

public class ReviewRequestDto {
    private String description;
    private int score;
    private List<Customer> likedByCustomers;


    //Create both setters and getters for this as its a request
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
    

    public List<Customer> getLikedByCustomers() {
        return likedByCustomers;
    }

    public void setLikedByCustomers(List<Customer> likedByCustomers) {
        this.likedByCustomers = likedByCustomers;
    }
}
