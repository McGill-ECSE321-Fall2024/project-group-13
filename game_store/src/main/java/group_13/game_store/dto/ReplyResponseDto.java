package group_13.game_store.dto;

import java.time.LocalDate;

import group_13.game_store.model.Reply;

public class ReplyResponseDto {
    private String text;
    private LocalDate date;
    private String username;

    public ReplyResponseDto(Reply reply) {
        this.text = reply.getText();
        this.date = reply.getDate().toLocalDate();
        this.username = "Owner";
    }

    public String getText() {
        return text;
    }
    
    public LocalDate getDate() {
        return date;
    }

    public String getUsername() {
        return username;
    }
}
