package group_13.game_store.dto;

import java.sql.Date;
import java.time.LocalDate;

public class PromotionRequestDto {
    private int percentage;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
    private String title;

    // Default constructor
    public PromotionRequestDto() {
    }

    public PromotionRequestDto(int percentage, String description, Date startDate, Date endDate, String title) {
        this.percentage = percentage;
        this.description = description;
        this.startDate = startDate.toLocalDate();
        this.endDate = endDate.toLocalDate();
        this.title = title;
    }

    public int getPercentage() {
        return percentage;
    }

    public String getDescription() {
        return description;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public String getTitle() {
        return title;
    }

    public void setPercentage(int percentage) {
        this.percentage = percentage;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    
}
