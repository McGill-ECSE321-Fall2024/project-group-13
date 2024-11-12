package group_13.game_store.dto;

import java.sql.Date;

import group_13.game_store.model.Customer;
import group_13.game_store.model.Order;

public class OrderResponseDto {
    private Date purchaseDate;
    private int totalPrice;
    private Date returnDate;
    private boolean isReturned;
    private Customer customer;
    
    public OrderResponseDto(Order order) {
        this.purchaseDate = order.getPurchaseDate();
        this.totalPrice = order.getTotalPrice();
        this.returnDate = order.getReturnDate();
        this.isReturned = order.getIsReturned();
        this.customer = order.getCustomer();
    }

    public Date getPurchaseDate() {
        return purchaseDate;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public Date getReturnDate() {
        return returnDate;
    }

    public boolean getIsReturned() {
        return isReturned;
    }

    public Customer getCustomer() {
        return customer;
    }
}

