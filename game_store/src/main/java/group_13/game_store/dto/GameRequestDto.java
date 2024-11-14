package group_13.game_store.dto;

public class GameRequestDto {
    private String title;
    private String description;
    private String img;
    private int stock;
    private double price;
    private String parentalRating;
    private String status;
    private int categoryId;

    public GameRequestDto() {}

    public GameRequestDto(String title, String description, String img, int stock, double price, String parentalRating, String status, int categoryId) {
        this.title = title;
        this.description = description;
        this.img = img;
        this.stock = stock;
        this.price = price;
        this.parentalRating = parentalRating;
        this.categoryId = categoryId;
        this.status = status;
    }

    // Getters and setters

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

}
