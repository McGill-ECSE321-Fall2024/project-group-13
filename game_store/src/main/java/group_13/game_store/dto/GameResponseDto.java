package group_13.game_store.dto;

import java.util.Objects;

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
    private int categoryId;
    private String promotionName;

    // New fields
    private String categoryName;
    private int promotionPercentage;
    private int rating;

    public GameResponseDto() {
    }

    public GameResponseDto(int gameID, String title, String description, String img, int stock, double price,
            String parentalRating, String status, int categoryId, String promotionName, String categoryName, int promotionPercentage, int rating) {
        this.gameID = gameID;
        this.title = title;
        this.description = description;
        this.img = img;
        this.stock = stock;
        this.price = price;
        this.parentalRating = parentalRating;
        this.categoryId = categoryId;
        if (promotionName == null) {
            this.promotionName = "";
            this.promotionPercentage = 0;
        } else {
            this.promotionName = promotionName;
            this.promotionPercentage = promotionPercentage;
        }

        this.categoryName = categoryName;
        this.rating = rating;

        if (status.equals("Visible")) {
            this.status = VisibilityStatus.Visible;
        } else if (status.equals("PendingArchive")) {
            this.status = VisibilityStatus.PendingArchive;
        } else if (status.equals("Archived")) {
            this.status = VisibilityStatus.Archived;
        } else if (status.equals("PendingVisible")) {
            this.status = VisibilityStatus.PendingVisible;
        } else {
            // default status if not one of the above
            this.status = VisibilityStatus.Archived;
        }
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

    public String getStatus() {
        return status.toString();
    }

   
    public int getCategoryId() {
        return categoryId;
    }

    public String getPromotionName() {
        return promotionName;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public int getPromotionPercentage() {
        return promotionPercentage;
    }

    public int getRating() {
        return rating;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        GameResponseDto that = (GameResponseDto) obj;
        return gameID == that.gameID &&
                stock == that.stock &&
                Double.compare(that.price, price) == 0 &&
                title.equals(that.title) &&
                description.equals(that.description) &&
                img.equals(that.img) &&
                parentalRating.equals(that.parentalRating) &&
                status == that.status &&
                categoryId == that.categoryId &&
                Objects.equals(promotionName, that.promotionName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(gameID, title, description, img, stock, price, parentalRating, status, categoryId,
                promotionName);
    }
}
