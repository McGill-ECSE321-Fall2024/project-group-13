package group_13.game_store.dto;

import java.sql.Date;
import java.time.LocalDate;

public class PromotionResponseDto {
    private int percentage;
    private String name;
    private String description;
    private String code;
    private double discount;

    // Convert Date to LocalDate to make it easier to read
    private LocalDate startDate;
    private LocalDate endDate;

    public PromotionResponseDto(int percentage, String name, String description, String code, double discount, Date startDate, Date endDate) {
        this.percentage = percentage;
        this.name = name;
        this.description = description;
        this.code = code;
        this.discount = discount;

        this.startDate = startDate.toLocalDate();
        this.endDate = endDate.toLocalDate();
    }

    public int getPercentage() {
        return percentage;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getCode() {
        return code;
    }

    public double getDiscount() {
        return discount;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }
    
}
