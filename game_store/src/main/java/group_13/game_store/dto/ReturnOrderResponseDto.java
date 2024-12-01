package group_13.game_store.dto;

import java.sql.Date;

import group_13.game_store.model.Customer;
import group_13.game_store.model.Order;

public class ReturnOrderResponseDto {
    private Date returnDate;
    private double totalPrice;
    private boolean isReturned;
    private String customerUsername;
    private int orderId;

    protected ReturnOrderResponseDto() {

    }
    public ReturnOrderResponseDto(Order order) {
        this.returnDate = Date.valueOf(order.getReturnDate().toLocalDate().plusDays(1));
        this.totalPrice = order.getTotalPrice();
        this.isReturned = order.getIsReturned();
        this.customerUsername = order.getCustomer().getUsername();
        this.orderId = order.getOrderID();
    }

    public Date getReturnDate() {
        return returnDate;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public int getOrderId() {
        return orderId;
    }

    public boolean getIsReturned() {
        return isReturned;
    }

    public String getCustomerUsername() {
        return this.customerUsername;
    }
    
}
