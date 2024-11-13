package group_13.game_store.dto;

import java.sql.Date;

public class PromotionRequestDto {
    private int percentage;
    private String description;
    private Date startDate;
    private Date endDate;
    private String title;

    public PromotionRequestDto(int percentage, String description, Date startDate, Date endDate, String title) {
        this.percentage = percentage;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.title = title;
    }

    public int getPercentage() {
        return percentage;
    }

    public String getDescription() {
        return description;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
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

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    
}
