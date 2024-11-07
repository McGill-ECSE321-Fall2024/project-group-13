package group_13.game_store.dto;

public class ReviewRequestDto {
    private String description;
    private int score;
    private int likes;

    public String getDescription() {
        return description;
    }

    public int getScore() {
        return score;
    }

    public int getLikes() {
        return likes;
    }
}
