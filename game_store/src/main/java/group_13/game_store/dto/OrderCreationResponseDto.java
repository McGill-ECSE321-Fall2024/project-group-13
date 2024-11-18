package group_13.game_store.dto;

//import java.sql.Date;
import java.time.LocalDate;
import java.time.ZoneId;

import group_13.game_store.model.Order;

public class OrderCreationResponseDto {
    private LocalDate purchaseDate;
    private int totalPrice;
    private boolean isReturned;
    private String customerUsername;
    private int orderId;
    
    protected OrderCreationResponseDto() {

    }
    public OrderCreationResponseDto(Order order) {
        this.purchaseDate = order.getPurchaseDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        this.totalPrice = order.getTotalPrice();
        this.isReturned = order.getIsReturned();
        this.customerUsername = order.getCustomer().getUsername();
        this.orderId = order.getOrderID();
    }

    public LocalDate getPurchaseDate() {
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

    public String getCustomer() {
        return customerUsername;
    }
}

