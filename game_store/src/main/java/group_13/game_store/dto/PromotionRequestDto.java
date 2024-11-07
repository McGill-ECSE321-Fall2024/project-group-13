package group_13.game_store.dto;

public class PromotionRequestDto {
    private int percentage;
    private String name;
    private String description;
    private String code;
    private double discount;

    public PromotionRequestDto(int percentage, String name, String description, String code, double discount) {
        this.percentage = percentage;
        this.name = name;
        this.description = description;
        this.code = code;
        this.discount = discount;
    }

    public int getPercentage() {
        return percentage;
    }

    public void setPercentage(int percentage) {
        this.percentage = percentage;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }
    
}
