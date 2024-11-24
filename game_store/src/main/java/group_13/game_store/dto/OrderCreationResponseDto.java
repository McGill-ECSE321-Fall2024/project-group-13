package group_13.game_store.dto;

import java.sql.Date;
import group_13.game_store.model.Order;

public class OrderCreationResponseDto {
    private Date purchaseDate;
    private int totalPrice;
    private boolean isReturned;
    private String customerUsername;
    private int orderId;
    
    protected OrderCreationResponseDto() {

    }
    public OrderCreationResponseDto(Order order) {
        this.purchaseDate = Date.valueOf(order.getPurchaseDate().toLocalDate().plusDays(1)); 
        this.totalPrice = order.getTotalPrice();
        this.isReturned = order.getIsReturned();
        this.customerUsername = order.getCustomer().getUsername();
        this.orderId = order.getOrderID();
    }

    public Date getPurchaseDate() {
        return purchaseDate;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public int getOrderId() {
        return orderId;
    }

    public boolean getIsReturned() {
        return isReturned;
    }

    public String getCustomerUsername() {
        return customerUsername;
    }
}

