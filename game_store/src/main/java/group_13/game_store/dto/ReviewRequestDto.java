package group_13.game_store.dto;

public class ReviewRequestDto {
    private String description;
    private int score;


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
}
