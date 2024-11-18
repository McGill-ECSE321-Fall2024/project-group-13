package group_13.game_store.dto;

import java.time.LocalDate;

public class OrderCreationRequestDto {
    private LocalDate purchaseDate;
    /* private int totalPrice;
    private Date returnDate;
    private boolean isReturned; */
    private String customerUsername;
    //private int orderId;
    
    protected OrderCreationRequestDto() {

    }

    public OrderCreationRequestDto(LocalDate purchaseDate, String customerUsername) {
        this.purchaseDate = purchaseDate;
        this.customerUsername = customerUsername;
        //this.orderId = orderId
    }

    public LocalDate getPurchaseDate() {
        return purchaseDate;
    }

    public String getCustomer() {
        return this.customerUsername;
    }

}
