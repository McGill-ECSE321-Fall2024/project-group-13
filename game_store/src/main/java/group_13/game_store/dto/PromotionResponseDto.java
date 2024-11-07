package group_13.game_store.dto;

import java.sql.Date;
import java.time.LocalDate;

public class PromotionResponseDto {
    private int percentage;
    private String title;
    private String description;

    // Convert Date to LocalDate to make it easier to read
    private LocalDate startDate;
    private LocalDate endDate;

    public PromotionResponseDto(int percentage, String title, String description, Date startDate, Date endDate) {
        this.percentage = percentage;
        this.title = title;
        this.description = description;

        this.startDate = startDate.toLocalDate();
        this.endDate = endDate.toLocalDate();
    }

    public int getPercentage() {
        return percentage;
    }

    public String getTitle() {
        return title;
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

    public void setPercentage(int percentage) {
        this.percentage = percentage;
    }
    
}
