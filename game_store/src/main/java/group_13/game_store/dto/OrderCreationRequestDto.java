package group_13.game_store.dto;

import java.sql.Date;

public class OrderCreationRequestDto {
    private Date purchaseDate;
    /* private int totalPrice;
    private Date returnDate;
    private boolean isReturned; */
    private String customerUsername;
    
    protected OrderCreationRequestDto() {

    }

    public OrderCreationRequestDto(Date purchaseDate, String customerUsername) {
        this.purchaseDate = purchaseDate;
        this.customerUsername = customerUsername;
    }

    public Date getPurchaseDate() {
        return purchaseDate;
    }

    public String getCustomer() {
        return this.customerUsername;
    }

}
