package group_13.game_store.dto;

import group_13.game_store.model.Game.VisibilityStatus;

public class GameResponseDto {

    private int gameID;
    private String title;
    private String description;
    private String img;
    private int stock;
    private double price;
    private String parentalRating;
    private VisibilityStatus status;
    private String categoryName;
    private String promotionName;

    public GameResponseDto() {
    }

    public GameResponseDto(int gameID, String title, String description, String img, int stock, double price, String parentalRating, VisibilityStatus status, String categoryName, String promotionName) {
        this.gameID = gameID;
        this.title = title;
        this.description = description;
        this.img = img;
        this.stock = stock;
        this.price = price;
        this.parentalRating = parentalRating;
        this.status = status;
        this.categoryName = categoryName;
        this.promotionName = promotionName;
    }

    public int getGameID() {
        return gameID;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getImg() {
        return img;
    }

    public int getStock() {
        return stock;
    }

    public double getPrice() {
        return price;
    }

    public String getParentalRating() {
        return parentalRating;
    }

    public VisibilityStatus getStatus() {
        return status;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public String getPromotionName() {
        return promotionName;
    }
}
