package group_13.game_store.dto;

public class ReplyRequestDto {
    private String text;

    // Default constructor
    public ReplyRequestDto() {}

    // Constructor
    public ReplyRequestDto(String text) {
        this.text = text;
    }

    // Getter and Setter
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}