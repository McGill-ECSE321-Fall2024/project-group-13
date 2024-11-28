package group_13.game_store.dto;

public class CartItemResponseDto extends GameResponseDto{
    private int quantity;

    public CartItemResponseDto() {}

    public CartItemResponseDto(int gameID, String title, String description, String img, int stock, double price,
    String parentalRating, String status, int categoryId, String promotionName, String categoryName, int promotionPercentage, int rating, int quantity) {
        super(gameID, title, description, img, stock, price, parentalRating, status, categoryId, promotionName, categoryName, promotionPercentage, rating);
        this.quantity = quantity;
    }

    // Getters and setters

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    
}



