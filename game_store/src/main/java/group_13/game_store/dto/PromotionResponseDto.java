package group_13.game_store.dto;

import java.time.LocalDate;

import group_13.game_store.model.Promotion;

public class PromotionResponseDto {
    private int promotionID;
    private int percentage;
    private String title;
    private String description;

    // Convert Date to LocalDate to make it easier to read
    private LocalDate startDate;
    private LocalDate endDate;

    // Default constructor
    public PromotionResponseDto() {
    }

    public PromotionResponseDto(Promotion promotion) {
        this.promotionID = promotion.getPromotionID();
        this.percentage = promotion.getPercentage();
        this.title = promotion.getTitle();
        this.description = promotion.getDescription();

        this.startDate = promotion.getStartDate().toLocalDate();
        this.endDate = promotion.getEndDate().toLocalDate();
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

    public void setTitle(String title) {
        this.title = title;
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

    public int getPromotionID() {
        return promotionID;
    }

    public void setPromotionID(int promotionID) {
        this.promotionID = promotionID;
    }
    
}
