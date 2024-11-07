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

    public void setGameID(int gameID) {
        this.gameID = gameID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getParentalRating() {
        return parentalRating;
    }

    public void setParentalRating(String parentalRating) {
        this.parentalRating = parentalRating;
    }

    public VisibilityStatus getStatus() {
        return status;
    }

    public void setStatus(VisibilityStatus status) {
        this.status = status;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getPromotionName() {
        return promotionName;
    }

    public void setPromotionName(String promotionName) {
        this.promotionName = promotionName;
    }
}
