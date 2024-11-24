package group_13.game_store.dto;

import java.sql.Date;
//import java.time.LocaDate;
//import java.time.ZoneId;

import group_13.game_store.model.Customer;
import group_13.game_store.model.Order;

public class OrderCreationResponseDto {
    private Date purchaseDate;
    private int totalPrice;
    private boolean isReturned;
    private Customer customer;
    private int orderId;
    
    protected OrderCreationResponseDto() {

    }
    public OrderCreationResponseDto(Order order) {
        this.purchaseDate = Date.valueOf(order.getPurchaseDate().toLocalDate().plusDays(1)); 
        this.totalPrice = order.getTotalPrice();
        this.isReturned = order.getIsReturned();
        this.customer = order.getCustomer();
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

    public Customer getCustomer() {
        return customer;
    }
}

