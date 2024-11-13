package group_13.game_store.dto;

public class ReplyRequestDto {
    private String text;

    public ReplyRequestDto(String text) {
        this.text = text;
    }

    //Getters abd setters for the text
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
